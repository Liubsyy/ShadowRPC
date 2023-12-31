package serialize.compare;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import serialize.obj.Generator;
import serialize.obj.Person;
import serialize.obj.Worker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/1
 */
public class JavaSerialize3 {

    /**
     * Java Serialization Average Time: 25912 ns, Size: 498 bytes
     * Java Deserialization Average Time: 56439 ns
     * Kryo Serialization Average Time: 15269 ns, Size: 175 bytes
     * Kryo Deserialization Average Time: 6889 ns
     *
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //构建对象
        int count = 10000;
        List<Worker> workers = new ArrayList<>();
        for(int i=0;i<count;i++) {
            Worker worker = new Worker();
            worker.setWorkAddr(Generator.generateAddress());
            worker.setInfo(Person.generatePerson());

            worker.setExtendInfo(new HashMap<>());
            worker.getExtendInfo().put("asdasdasd","111");
            worker.getExtendInfo().put("qwewrtrhb","222");
            workers.add(worker);
        }

        Kryo kryo = new Kryo();

        long totalJavaSerializeTime = 0, totalJavaDeserializeTime = 0;
        long totalKryoSerializeTime = 0, totalKryoDeserializeTime = 0;
        int javaSerializedSize = 0, kryoSerializedSize = 0;

        for (int i = 0; i < count; i++) {
            // 创建一个略有差异的新对象
            Worker testObject = workers.get(i);

            // Java原生序列化
            long startJavaSerialize = System.nanoTime();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(testObject);
            oos.close();
            byte[] javaSerialized = bos.toByteArray();
            totalJavaSerializeTime += System.nanoTime() - startJavaSerialize;
            if (i == 0) {
                javaSerializedSize = javaSerialized.length;
            }

            // Java原生反序列化
            long startJavaDeserialize = System.nanoTime();
            ByteArrayInputStream bis = new ByteArrayInputStream(javaSerialized);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ois.readObject();
            totalJavaDeserializeTime += System.nanoTime() - startJavaDeserialize;

            // Kryo序列化
            long startKryoSerialize = System.nanoTime();
            ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
            Output output = new Output(kryoBos);
            kryo.writeObject(output, testObject);
            output.close();
            byte[] kryoSerialized = kryoBos.toByteArray();
            totalKryoSerializeTime += System.nanoTime() - startKryoSerialize;
            if (i == 0) {
                kryoSerializedSize = kryoSerialized.length;
            }

            // Kryo反序列化
            Input input = new Input(new ByteArrayInputStream(kryoSerialized));
            long startKryoDeserialize = System.nanoTime();
            kryo.readObject(input, JavaSerialize2.TestObject.class);
            input.close();
            totalKryoDeserializeTime += System.nanoTime() - startKryoDeserialize;
        }

        // 输出平均结果
        System.out.println("Java Serialization Average Time: " + (totalJavaSerializeTime / count) + " ns, Size: " + javaSerializedSize + " bytes");
        System.out.println("Java Deserialization Average Time: " + (totalJavaDeserializeTime / count) + " ns");

        System.out.println("Kryo Serialization Average Time: " + (totalKryoSerializeTime / count) + " ns, Size: " + kryoSerializedSize + " bytes");
        System.out.println("Kryo Deserialization Average Time: " + (totalKryoDeserializeTime / count) + " ns");
    }
}
