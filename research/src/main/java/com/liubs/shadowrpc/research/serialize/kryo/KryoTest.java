package com.liubs.shadowrpc.research.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.liubs.shadowrpc.research.entity.User;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.serialize.strategy.KryoSerialize;
import com.liubs.shadowrpc.research.serialize.strategy.KryoSerializeBuffer;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/12/2 11:37 AM
 */
public class KryoTest {

    //序列化方式比较
    public static void main(String[] args) throws Exception {
        ISerialize kryoSerialize = new KryoSerialize();
        ISerialize kryoBufferSerialize = new KryoSerializeBuffer();


        System.out.println("******** 预热 ********");
        runWithNum(kryoSerialize,1);
        runWithNum(kryoBufferSerialize,1);

        System.out.println("******** 单对象累计100w次总时长比较 ********");
        runWithNum(kryoSerialize,1000000);
        runWithNum(kryoBufferSerialize,1000000);

        System.out.println("******** 序列化class和不序列化class对比 ********");
        User user = new User();
        user.setName("liubs");
        user.setWechatPub("微信号：liubsyy");
        user.setJob("优秀工程师");

        Kryo kryo = KryoSerialize.kryoThreadLocal.get();

        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, user);
        output.close();
        byte[] byte1 = kryoBos.toByteArray();

        kryoBos = new ByteArrayOutputStream();
        output = new Output(kryoBos);
        kryo.writeObject(output, user);
        output.close();
        byte[] byte2 = kryoBos.toByteArray();

        System.out.printf("writeClassAndObject:%d,writeObject:%d",byte1.length,byte2.length);

    }

    private static void runWithNum(ISerialize serialize,int n) throws Exception {

        //先初始化序列化的数据
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<User> userList = new ArrayList<>();
        Random random = new Random();
        for(int i = 0;i<n ;i++) {
            User userItem = new User();
            userItem.setName("liubs"+random.nextInt(n));
            userItem.setWechatPub("微信号：liubsyy"+random.nextInt(n));
            userItem.setJob("优秀工程师"+i);

            userList.add(userItem);
        }

        timeSizeStat.begin();
        for(int i = 0;i<n ;i++) {
            byte[] serializeBytes = serialize.serialize(userList.get(i));
            timeSizeStat.addSize(serializeBytes.length);
        }
        long useNs = timeSizeStat.getUseTime();
        System.out.printf("[%s]%d次累计用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),n,useNs,useNs/1000000,timeSizeStat.getSize());
    }
}
