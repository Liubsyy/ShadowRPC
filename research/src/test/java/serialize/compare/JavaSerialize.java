package serialize.compare;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import serialize.obj.Generator;
import serialize.obj.Person;
import serialize.obj.Worker;
import util.TimeAndSizeStatUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Liubs
 * @date 2023/11/30
 */
public class JavaSerialize {
    private static Gson gson = new Gson();
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 对Kryo实例进行配置
        return kryo;
    });

    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static byte[] serializeFastJson(Object obj) {
        String json = JSON.toJSONString(obj);
        return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public static byte[] serializeGson(Object obj) {
        String json = gson.toJson(obj);
        return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
    public static byte[] serializeKryo(Object obj) {
        Kryo kryo = kryoThreadLocal.get();
        try (Output output = new ByteBufferOutput(4096, -1)) {
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        }
    }

//    public static byte[] serializeKryo(Object obj) {
//        Kryo kryo = new Kryo();
//        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(1024);
//        Output output = new Output(byteOutputStream);
//        kryo.writeClassAndObject(output, obj);
//        output.close();
//        return byteOutputStream.toByteArray();
//    }

    /**
     * 1
     * [java原生序列化] 用时:3,大小:491
     * [fastjson序列化] 用时:42,大小:244
     * [gson序列化] 用时:2,大小:246
     * [kryo序列化] 用时:1,大小:190
     *
     * 10
     * [java原生序列化] 用时:3,大小:4869
     * [fastjson序列化] 用时:44,大小:2391
     * [gson序列化] 用时:3,大小:2411
     * [kryo序列化] 用时:2,大小:1859
     *
     * 100
     * [java原生序列化] 用时:24,大小:48841
     * [fastjson序列化] 用时:52,大小:24094
     * [gson序列化] 用时:26,大小:24299
     * [kryo序列化] 用时:24,大小:18743
     *
     * 1000
     * [java原生序列化] 用时:80,大小:488366
     * [fastjson序列化] 用时:102,大小:240776
     * [gson序列化] 用时:98,大小:242946
     * [kryo序列化] 用时:111,大小:187384
     *
     * 10000
     * [java原生序列化] 用时:197,大小:4884831
     * [fastjson序列化] 用时:219,大小:2408939
     * [gson序列化] 用时:228,大小:2429909
     * [kryo序列化] 用时:170,大小:1875031
     *
     * 100000
     * [java原生序列化] 用时:554,大小:48841540
     * [fastjson序列化] 用时:339,大小:24081529
     * [gson序列化] 用时:798,大小:24291354
     * [kryo序列化] 用时:462,大小:18743532
     *
     * 1000000
     * [java原生序列化] 用时:3824,大小:488400719
     * [fastjson序列化] 用时:1003,大小:240800634
     * [gson序列化] 用时:2346,大小:242900839
     * [kryo序列化] 用时:3217,大小:187420977
     *
     * 1000000 有List
     * [java原生序列化] 用时:9046,大小:1073550016
     * [fastjson序列化] 用时:4262,大小:1445823527
     * [gson序列化] 用时:14879,大小:1468422057
     * [kryo序列化] 用时:5406,大小:610503778
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //先预热一下，主要是初始化序列化需要时间
        Person initPerson = Person.generatePerson();
        serializeObject(initPerson);
        serializeFastJson(initPerson);
        serializeGson(initPerson);
        serializeKryo(initPerson);

        //构建对象
        int count = 10000;
        List<Worker> workers = new ArrayList<>();
        for(int i=0;i<count;i++) {
            Worker worker = new Worker();
            worker.setWorkAddr(Generator.generateAddress());
            worker.setInfo(Person.generatePerson());
//            worker.setFriends(new ArrayList<>());
//            for(int j = 0;j<10;j++){
//                worker.getFriends().add(Person.generatePerson());
//            }
            worker.setExtendInfo(new HashMap<>());
            worker.getExtendInfo().put("asdasdasd","111");
            worker.getExtendInfo().put("qwewrtrhb","222");
            workers.add(worker);
        }

        //java原生序列化
        TimeAndSizeStatUtil.begin();
        for(Worker worker :  workers) {
            byte[] bytes1 = serializeObject(worker);
            TimeAndSizeStatUtil.addSize(bytes1.length);
        }
        System.out.printf("[java原生序列化] 用时:%d,大小:%d\n", TimeAndSizeStatUtil.getUseTime(),TimeAndSizeStatUtil.getSize());

        //fastjson序列化
        TimeAndSizeStatUtil.begin();
        for(Worker worker :  workers) {
            byte[] fastJsonBytes = serializeFastJson(worker);
            TimeAndSizeStatUtil.addSize(fastJsonBytes.length);
        }
        System.out.printf("[fastjson序列化] 用时:%d,大小:%d\n", TimeAndSizeStatUtil.getUseTime(),TimeAndSizeStatUtil.getSize());

        //gson序列化
        TimeAndSizeStatUtil.begin();
        for(Worker worker :  workers) {
            byte[] gsonBytes = serializeGson(worker);
            TimeAndSizeStatUtil.addSize(gsonBytes.length);
        }
        System.out.printf("[gson序列化] 用时:%d,大小:%d\n", TimeAndSizeStatUtil.getUseTime(),TimeAndSizeStatUtil.getSize());

        //kryo序列化
        TimeAndSizeStatUtil.begin();
        for(Worker worker :  workers) {
            byte[] kryoBytes = serializeKryo(worker);
            TimeAndSizeStatUtil.addSize(kryoBytes.length);
        }
        System.out.printf("[kryo序列化] 用时:%d,大小:%d\n", TimeAndSizeStatUtil.getUseTime(),TimeAndSizeStatUtil.getSize());

    }

}
