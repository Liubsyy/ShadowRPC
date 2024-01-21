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

/**
 * reactor模式
 * @author Liubsyy
 * @date 2024/1/21
 **/

public class NIOReactor extends Thread {
    private static Logger logger = Logger.getLogger(NIOClient.class);

    //缓存区大小
    public static final int BUFFER_SIZE = 65535;

    private final NIOClient nioClient;
    private final Selector selector;
    private final SocketChannel socketChannel;

    //写通道
    private final Queue<ByteBuffer> writeQueue;

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

        while (iter.hasNext()) {
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
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
//        ByteBuffer buffer = (ByteBuffer) key.attachment();
//        if (buffer != null) {
//            socketChannel.write(buffer);
//            if (!buffer.hasRemaining()) {
//                // Write operation completed, clear the attachment
//                key.attach(null);
//                // Change interest back to OP_READ
//                key.interestOps(SelectionKey.OP_READ);
//                System.out.println("write flush...");
//            }
//        }

        while (!writeQueue.isEmpty()) {
            ByteBuffer buffer = writeQueue.peek();
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
                logger.error("write msg err",e);
            }
            if (buffer.hasRemaining()) {
                //没有写完，下一次再写，position还会保留
                key.interestOps(SelectionKey.OP_WRITE);
                break;
            }
            writeQueue.remove(); // Remove the buffer after it's fully written
        }

        if (writeQueue.isEmpty()) {
            // If no more data to write, change interest back to OP_READ
            key.interestOps(SelectionKey.OP_READ);
        }

    }

    private void handleRead(SelectionKey key) throws IOException {

        ByteBuffer buffer = readByteBuffer;

        int numRead = socketChannel.read(buffer);

        // 遍历缓冲区并打印每个字节的16进制表示
//        System.out.print("buffer: ");
//        for (int i = 0; i < buffer.limit(); i++) {
//            byte b = buffer.get(i); // 读取位置i的字节
//            System.out.printf("0x%02X ", b);
//        }
//        System.out.println();

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

//                    System.out.print("reset: ");
//                    for (int i = buffer.position(); i < buffer.limit(); i++) {
//                        byte b = buffer.get(i); // 读取位置i的字节
//                        System.out.printf("0x%02X ", b);
//                    }
//                    System.out.println();

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
            // 对端链路关闭
            key.cancel();
            socketChannel.close();
        }
    }


}
