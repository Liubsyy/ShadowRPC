package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.logger.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * reactor模式
 * @author Liubsyy
 * @date 2024/1/21
 **/

public class NIOReactor extends Thread {
    private static Logger logger = Logger.getLogger(NIOClient.class);

    //缓存区大小
    private static final int BUFFER_SIZE = 65535;

    //写最多重试次数
    private static final int MAX_WRITE_FAIL_COUNT = 10;

    //写失败次数
    private AtomicInteger writeFailCount = new AtomicInteger(0);

    private final NIOClient nioClient;
    private final Selector selector;
    private final SocketChannel socketChannel;

    //写通道
    private final Queue<MessageSendFuture> writeQueue;


    //读通道
    private final ByteBuffer readByteBuffer;

    public NIOReactor(NIOClient nioClient) {
        this.nioClient = nioClient;
        this.selector = nioClient.getSelector();
        this.socketChannel = nioClient.getSocketChannel();
        this.writeQueue = nioClient.getWriteQueue();
        this.readByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    @Override
    public void run() {
        while (nioClient.isRunning()) {
            try {
                if (selector.select() > 0) {
                    processSelectedKeys();
                }
            } catch (IOException e) {
                logger.error("selector err",e);
            }
        }
    }

    private void processSelectedKeys() throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> iter = selectedKeys.iterator();

        while (nioClient.isRunning() && iter.hasNext()) {
            SelectionKey key = iter.next();

            if (key.isConnectable()) {
                handleConnect(key);
            }
            if (key.isWritable()) {
                handleWrite(key);
            }
            if (key.isReadable()) {
                handleRead(key);
            }
            iter.remove();
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        if (socketChannel.finishConnect()) {
            key.interestOps(SelectionKey.OP_READ);
            nioClient.finishConnection();
        }
    }

    private void handleWrite(SelectionKey key)  {
        while (!writeQueue.isEmpty()) {
            MessageSendFuture sendFuture = writeQueue.peek();
            ByteBuffer buffer = sendFuture.getBuffer();
            int writeResult = -1;
            try {
                writeResult = socketChannel.write(buffer);
            } catch (IOException e) {
                //logger.error("socket write err",e);
//                if(writeFailCount.addAndGet(1) >= MAX_WRITE_FAIL_COUNT){   //n次失败估计服务器失去联系了
//                    logger.error("socket write fail for {} times,close chanel",MAX_WRITE_FAIL_COUNT);
//                    handleClose(key);
//                    break;
//                }
                if(!sendFuture.isCancelled()) {
                    sendFuture.completeExceptionally(e);
                }

                key.interestOps(SelectionKey.OP_READ);
                break;
            }
            if (buffer.hasRemaining()) {
                //没有写完，下一次再写，position还会保留
                key.interestOps(SelectionKey.OP_WRITE);
                break;
            }
            writeQueue.remove(); // Remove the buffer after it's fully written
            sendFuture.complete(writeResult);
        }

        if (writeQueue.isEmpty()) {
            // If no more data to write, change interest back to OP_READ
            key.interestOps(SelectionKey.OP_READ);
        }

    }

    private void handleRead(SelectionKey key)  {

        ByteBuffer buffer = readByteBuffer;

        int numRead = 0;
        try {
            numRead = socketChannel.read(buffer);
        } catch (IOException e) {
            handleClose(key);
            return;
        }

        if (numRead > 0) {
            buffer.flip(); // 切换到读模式
            // 处理缓冲区中的所有数据
            while (buffer.remaining() > 4) { // 确保有足够的数据读取长度字段
                buffer.mark();
                int length = buffer.getInt();
                //System.out.printf("read length=%d,remain=%d\n",length,buffer.remaining());

                if (length <= buffer.remaining()) {
                    byte[] data = new byte[length];
                    buffer.get(data);
                    nioClient.getReceiveMessageCallBack().handleMessage(data);
                } else {
                    // 数据长度不足以构成一个完整的消息，重置并退出循环
                    buffer.reset();
                    break;
                }
            }

            if (buffer.hasRemaining()) {
                buffer.compact(); // 移动未处理数据到缓冲区开始位置
            } else {
                buffer.clear(); // 如果没有剩余数据，清空缓冲区
            }
        } else if (numRead < 0) {
            //接收到-1表示服务器关闭
            handleClose(key);
        }
    }


    private void handleClose(SelectionKey key) {
        try {
            key.cancel();
            socketChannel.close();
        } catch (IOException e) {
            logger.error("close socket channel fail", e);
        }
        logger.error("服务器连接已关闭或发生错误");
        nioClient.setRunning(false); // 设置客户端为非运行状态
    }


}
