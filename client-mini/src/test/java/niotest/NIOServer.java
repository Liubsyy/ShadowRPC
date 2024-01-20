package niotest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Liubsyy
 * @date 2024/1/20
 *
 **/

public class NIOServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public NIOServer(int port) throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception {
        System.out.println("Server started on port: " + serverSocketChannel.socket().getLocalPort());

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    handleAccept(key);
                }

                if (key.isReadable()) {
                    handleRead(key);
                }
                iter.remove();
            }
        }
    }

    private void handleAccept(SelectionKey key) throws Exception {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        System.out.println("Accepted connection from " + clientChannel);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void respondToClient(SocketChannel clientChannel, String receivedString) throws IOException {
        byte[] responseBytes = receivedString.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(4 + responseBytes.length); // 4 bytes for length field
        writeBuffer.putInt(responseBytes.length); // Write length of the message
        writeBuffer.put(responseBytes); // Write message itself
        writeBuffer.flip();
        clientChannel.write(writeBuffer);
    }

    private void handleRead(SelectionKey key) throws Exception {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024); // 假设这个大小足以包含至少一个完整消息
        int numRead = clientChannel.read(buffer);

        if (numRead > 0) {
            buffer.flip();
            while (buffer.remaining() > 4) { // 确保有足够的数据读取长度字段
                buffer.mark();
                int length = buffer.getInt();
                if (buffer.remaining() < length) {
                    buffer.reset(); // 没有足够的数据，等待下一个包
                    break;
                }
                byte[] data = new byte[length];
                buffer.get(data);
                String receivedString = new String(data).trim();
                System.out.println("Received from client: " + receivedString);

                // 回复客户端
                respondToClient(clientChannel,"Echo: " + receivedString);


                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // 回复客户端
                        try {
                            respondToClient(clientChannel,"async message");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }.start();
            }
        } else if (numRead == -1) {
            clientChannel.close();
        }
    }

}
