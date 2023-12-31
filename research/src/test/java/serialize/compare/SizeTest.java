package serialize.compare;

import com.alibaba.fastjson.JSON;
import serialize.obj.Person;
import serialize.obj.UserDTO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Liubsyy
 * @date 2023/12/1
 */
public class SizeTest {

    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static byte[] jdkSerialize(Object obj) throws IOException {
        // 字节输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 将对象序列化为二进制字节流
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        // 获取二进制字节数组
        byte[] bytes = byteArrayOutputStream.toByteArray();
        //  关闭流
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return bytes;
    }
    public static <T> T jdkDeSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
        // 字节输入流
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 将二进制字节流反序列化为对象
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final T object = (T) objectInputStream.readObject();
        // 关闭流
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("雷小帅");
        userDTO.setWechatPub("微信公众号：爱笑的架构师");
        userDTO.setJob("优秀码农");

        System.out.println("--- 1. jdk 原生测试 ---");
        byte[] bytes = jdkSerialize(userDTO);
        System.out.println("序列化成功：" + Arrays.toString(bytes));
        System.out.println("byte size=" + bytes.length);
        UserDTO userDTO1 = jdkDeSerialize(bytes);
        System.out.println("反序列化成功：" + userDTO1);


        System.out.println(serializeObject(userDTO).length);

        Person initPerson = Person.generatePerson();
        System.out.println(serializeObject(initPerson).length);


        System.out.println("--- 2. fastjson 测试 ---");
        String jsonString = JSON.toJSONString(userDTO);
        System.out.println("序列化成功： " + jsonString);
        System.out.println("string size=" + jsonString.length());
        System.out.println("byte size=" + jsonString.getBytes(StandardCharsets.UTF_8).length);
        UserDTO userDTO2 = JSON.parseObject(jsonString, UserDTO.class);
        System.out.println("反序列化成功：" + userDTO2);


    }
}
