package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.exception.ConnectTimeoutException;
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

    private NIOConfig nioConfig;

    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean isRunning;

    private IMessageListener receiveMessageCallBack;

    private final Queue<MessageSendFuture> writeQueue;

    private CompletableFuture<Void> waitConnection;

    private NIOReactor nioReactor;


    public NIOClient(String host, int port,NIOConfig nioConfig,IMessageListener receiveMessageCallBack)  {
        this.host = host;
        this.port = port;
        this.nioConfig = nioConfig;
        this.receiveMessageCallBack = receiveMessageCallBack;

        this.writeQueue = new ConcurrentLinkedQueue<>();
        this.waitConnection = new CompletableFuture<>();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void connect() throws IOException, ConnectTimeoutException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(host, port));
        isRunning = true;

        //reactor模式
        this.nioReactor = new NIOReactor(this);
        nioReactor.start();

        //等待连接完成
        try{
            waitConnection.get(nioConfig.getConnectTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            isRunning = false;
            throw new ConnectTimeoutException(String.format("连接服务器%s:%d超时",host,port));
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
        if(null !=nioReactor && nioReactor.isAlive()){
            nioReactor.interrupt();
        }
    }

    public IMessageListener getReceiveMessageCallBack() {
        return receiveMessageCallBack;
    }

    public Queue<MessageSendFuture> getWriteQueue() {
        return writeQueue;
    }

    public NIOConfig getNioConfig() {
        return nioConfig;
    }
}
