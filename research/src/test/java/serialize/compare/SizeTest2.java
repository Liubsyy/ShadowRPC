package serialize.compare;

import com.alibaba.fastjson.JSON;
import serialize.obj.Generator;
import serialize.obj.Person;
import serialize.obj.Worker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Liubsyy
 * @date 2023/12/1
 */
public class SizeTest2 {

    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static void main(String[] args) throws IOException {

        Worker worker = new Worker();
        worker.setWorkAddr(Generator.generateAddress());
        worker.setInfo(Person.generatePerson());
        worker.setFriends(new ArrayList<>());
//        for(int j = 0;j<100;j++){
//            worker.getFriends().add(Person.generatePerson());
//        }
        worker.setExtendInfo(new HashMap<>());
        worker.getExtendInfo().put("asdasdasd","111");
        worker.getExtendInfo().put("qwewrtrhb","222");

        //Person person = Person.generatePerson();

        System.out.println("--- 1. jdk 原生测试 ---");
        byte[] bytes = serializeObject(worker);
        //System.out.println("序列化成功：" + Arrays.toString(bytes));
        System.out.println("byte size=" + bytes.length);


        System.out.println("--- 2. fastjson 测试 ---");
        String jsonString = JSON.toJSONString(worker);
        //System.out.println("序列化成功： " + jsonString);
        System.out.println("string size=" + jsonString.length());
        System.out.println("byte size=" + jsonString.getBytes(StandardCharsets.UTF_8).length);
    }
}
