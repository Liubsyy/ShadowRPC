package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class NIOClient {
    private static Logger logger = Logger.getLogger(NIOClient.class);

    private static final int BUFFER_SIZE = 1024;

    private String host;
    private int port;

    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean isRunning;

    private IMessageListener receiveMessageCallBack;
    private CountDownLatch connectFinish;


    public NIOClient(String host, int port,IMessageListener receiveMessageCallBack)  {
        this.host = host;
        this.port = port;
        this.receiveMessageCallBack = receiveMessageCallBack;
    }


    public void connect() throws IOException, InterruptedException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(host, port));
        connectFinish = new CountDownLatch(1);
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                try {
                    if (selector.select() > 0) {
                        processSelectedKeys();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        connectFinish.await();
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

    public void sendMessage(byte[] bytes) {
        if(null == bytes || bytes.length == 0) {
            return;
        }

        ByteBuffer writeBuffer = ByteBuffer.allocate(4 + bytes.length); // 4 bytes for length field
        writeBuffer.putInt(bytes.length); // Write length of the message
        writeBuffer.put(bytes); // Write message itself
        writeBuffer.flip();

        try {
            synchronized (this) {
                while (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }
                // Optionally, register for OP_READ to receive server response
                socketChannel.register(selector, SelectionKey.OP_READ);
            }

        } catch (IOException e) {
            logger.error("sendMessage err",e);
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        if (socketChannel.finishConnect()) {
            key.interestOps(SelectionKey.OP_READ);
            connectFinish.countDown();
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            buffer.clear();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int numRead = socketChannel.read(buffer);
        if (numRead > 0) {
            buffer.flip(); // 切换到读模式

            // 处理缓冲区中的所有数据
            while (buffer.remaining() >= 4) { // 确保有足够的数据读取长度字段
                buffer.mark();
                int length = buffer.getInt();

                if (length <= buffer.remaining()) {
                    byte[] data = new byte[length];
                    buffer.get(data);
                    receiveMessageCallBack.handleMessage(data);
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
            // 对端链路关闭
            key.cancel();
            socketChannel.close();
        }
    }


    public void close() {
        isRunning = false;
        try {
            selector.close();
            socketChannel.close();
        } catch (IOException e) {
            logger.error("close connection err",e);
        }
    }
}
