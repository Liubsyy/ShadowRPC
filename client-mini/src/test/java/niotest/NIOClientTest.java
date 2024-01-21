package niotest;

import com.liubs.shadowrpc.clientmini.nio.IMessageListener;
import com.liubs.shadowrpc.clientmini.nio.NIOClient;
import com.liubs.shadowrpc.clientmini.nio.NIOConfig;
import org.junit.Test;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class NIOClientTest implements IMessageListener {

    /**
     * 开启服务器
     * @throws Exception
     */
    @Test
    public void runServer() throws Exception {
        NIOServer server = new NIOServer(8080);
        server.start();
    }

    /**
     * 连接服务器
     * @throws Exception
     */
    @Test
    public void testClient() throws Exception {
        NIOClient client = new NIOClient("localhost", 8080,new NIOConfig(),this);
        client.connect();

        client.sendMessage("Hello, Server!".getBytes());

        Thread.sleep(3000);

        client.close();
    }

    @Override
    public void handleMessage(byte[] bytes) {
        String message = new String(bytes).trim();
        System.out.println("Received complete message: " + message);
    }
}
