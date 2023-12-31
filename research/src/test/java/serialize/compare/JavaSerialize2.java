package serialize.compare;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.*;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/12/1
 */
public class JavaSerialize2 {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final int iterations = 1000000;
        Kryo kryo = new Kryo();
        kryo.register(TestObject.class);
        Random random = new Random();

        long totalJavaSerializeTime = 0, totalJavaDeserializeTime = 0;
        long totalKryoSerializeTime = 0, totalKryoDeserializeTime = 0;
        int javaSerializedSize = 0, kryoSerializedSize = 0;

        for (int i = 0; i < iterations; i++) {
            // 创建一个略有差异的新对象
            TestObject testObject = new TestObject("Test" + i, random.nextInt());

            // Java原生序列化
            long startJavaSerialize = System.nanoTime();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(testObject);
            //oos.flush();
            oos.close();
            bos.close();
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
            ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
            Output output = new Output(kryoBos);
            long startKryoSerialize = System.nanoTime();
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
            kryo.readObject(input, TestObject.class);
            input.close();
            totalKryoDeserializeTime += System.nanoTime() - startKryoDeserialize;
        }

        System.out.println(totalJavaSerializeTime);
        System.out.println(totalKryoSerializeTime);

        // 输出平均结果
        System.out.println("Java Serialization Average Time: " + (totalJavaSerializeTime / iterations) + " ns, Size: " + javaSerializedSize + " bytes");
        System.out.println("Java Deserialization Average Time: " + (totalJavaDeserializeTime / iterations) + " ns");

        System.out.println("Kryo Serialization Average Time: " + (totalKryoSerializeTime / iterations) + " ns, Size: " + kryoSerializedSize + " bytes");
        System.out.println("Kryo Deserialization Average Time: " + (totalKryoDeserializeTime / iterations) + " ns");
    }


    static class TestObject implements Serializable {
        String name;
        int value;

        public TestObject() {
        }

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
