package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class NIOClient {
    private static Logger logger = Logger.getLogger(NIOClient.class);


    private String host;
    private int port;

    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean isRunning;

    private IMessageListener receiveMessageCallBack;

    private final Queue<ByteBuffer> writeQueue;



    public NIOClient(String host, int port,IMessageListener receiveMessageCallBack)  {
        this.host = host;
        this.port = port;
        this.receiveMessageCallBack = receiveMessageCallBack;

        this.writeQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(host, port));
        isRunning = true;

        //reactor模式
        new NIOReactor(this).start();

        //等待连接完成
        while(!socketChannel.finishConnect());
    }



    public void sendMessage(byte[] bytes) {
        if(null == bytes || bytes.length == 0) {
            return;
        }

        ByteBuffer writeBuffer = ByteBuffer.allocate(4 + bytes.length); // 4 bytes for length field
        writeBuffer.putInt(bytes.length); // Write length of the message
        writeBuffer.put(bytes); // Write message itself
        writeBuffer.flip();

        // Add to write queue
        writeQueue.add(writeBuffer);

        // Change interest to OP_WRITE
        SelectionKey key = socketChannel.keyFor(selector);
        key.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();

//        try {
//            synchronized (this) {
//                while (writeBuffer.hasRemaining()) {
//                    socketChannel.write(writeBuffer);
//                }
//                // Optionally, register for OP_READ to receive server response
//                socketChannel.register(selector, SelectionKey.OP_READ);
//            }
//
//        } catch (IOException e) {
//            logger.error("sendMessage err",e);
//        }
    }

    public Selector getSelector() {
        return selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
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

    public IMessageListener getReceiveMessageCallBack() {
        return receiveMessageCallBack;
    }

    public Queue<ByteBuffer> getWriteQueue() {
        return writeQueue;
    }
}
