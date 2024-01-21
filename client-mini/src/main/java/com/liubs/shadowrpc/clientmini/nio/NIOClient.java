package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.exception.ConnectTimeOutException;
import com.liubs.shadowrpc.clientmini.logger.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.*;


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

    private final Queue<MessageSendFuture> writeQueue;

    private CompletableFuture<Void> waitConnection;


    public NIOClient(String host, int port,IMessageListener receiveMessageCallBack)  {
        this.host = host;
        this.port = port;
        this.receiveMessageCallBack = receiveMessageCallBack;

        this.writeQueue = new ConcurrentLinkedQueue<>();
        waitConnection = new CompletableFuture<>();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(host, port));
        isRunning = true;

        //reactor模式
        NIOReactor nioReactor = new NIOReactor(this);
        nioReactor.start();

        //等待连接完成
        try{
            waitConnection.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            isRunning = false;
            throw new ConnectTimeOutException(String.format("连接服务器%s:%d超时",host,port));
        }
    }

    public void finishConnection(){
        waitConnection.complete(null);
    }


    public MessageSendFuture sendMessage(byte[] bytes) {
        if(null == bytes || bytes.length == 0) {
            return null;
        }

        ByteBuffer writeBuffer = ByteBuffer.allocate(4 + bytes.length); // 4 bytes for length field
        writeBuffer.putInt(bytes.length); // Write length of the message
        writeBuffer.put(bytes); // Write message itself
        writeBuffer.flip();

        // Add to write queue
        MessageSendFuture future = new MessageSendFuture(writeBuffer);
        writeQueue.add(future);

        // Change interest to OP_WRITE
        SelectionKey key = socketChannel.keyFor(selector);
        if(!key.isValid()) {
            return null;
        }
        key.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();

        return future;
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

    public Queue<MessageSendFuture> getWriteQueue() {
        return writeQueue;
    }
}
