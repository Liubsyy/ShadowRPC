import com.liubs.shadowrpc.registry.zk.ZooKeeperClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * zookeeper 单测：增删改查,获取子节点，监听节点
 * @author Liubsyy
 * @date 2023/12/18 11:39 PM
 **/
public class ZooKeeperClientTest {

    private static ZooKeeperClient client;

    @BeforeClass
    public static void setUp() throws Exception {
        client = new ZooKeeperClient("localhost:2181"); // 使用你的ZooKeeper连接字符串
        if(client.exist("/test")) {
            client.deleteChildren("/test");
        }
    }

    @Test
    public void testCreateAndRead() throws Exception {
        String path = "/test/create";
        byte[] data = "data".getBytes();
        path = client.create(path, data);
        assertArrayEquals(data, client.read(path));
    }

    @Test
    public void testUpdate() throws Exception {
        String path = "/test/update";
        byte[] data = "data".getBytes();
        path = client.create(path, data);
        byte[] newData = "newData".getBytes();
        client.update(path, newData);
        assertArrayEquals(newData, client.read(path));
    }

    @Test
    public void testDelete() throws Exception {
        String path = "/test/delete";
        byte[] data = "data".getBytes();
        path = client.create(path, data);
        client.delete(path);
        assertNull(client.read(path));
    }
    @Test
    public void testChildren() throws Exception {
        String path = "/test/children";
        int n = 10;
        for(int i =1;i<=n;i++) {
            client.create(path+"/node"+i);
        }
        List<String> children = client.getChildren(path);
        assertEquals(children.size(),n);
    }

    @Test
    public void testChildrenListener() throws Exception {
        String path = "/test/childrenListen";

        client.addChildrenListener(path, (client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("Child added: " + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("Child removed: " + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("Child updated: " + event.getData().getPath());
                    break;
            }
        });

        int n = 10;
        List<String> allPaths = new ArrayList<>();
        for(int i =1;i<=n;i++) {
            allPaths.add(client.create(path+"/node"+i, ("/node"+i).getBytes()));
        }
        client.delete(allPaths.get(1));
        client.delete(allPaths.get(2));
        client.delete(allPaths.get(3));

        client.update(allPaths.get(5), ("/newnode5").getBytes());

        Thread.sleep(5000);

    }


    @AfterClass
    public static void tearDown() throws Exception {
        client.close();
    }
}
