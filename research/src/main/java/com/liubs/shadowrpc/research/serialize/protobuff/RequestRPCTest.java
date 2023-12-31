package com.liubs.shadowrpc.research.serialize.protobuff;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.liubs.shadowrpc.research.entity.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/12/18
 */
public class RequestRPCTest {

    public static byte[] serializeKryo(Kryo kryo, Object obj)  {
        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }


    public static void main(String[] args) {
        SimplePerson person = new SimplePerson();
        person.setName("Tom");
        person.setAge(18);
        person.setHeight(170);
        person.setWeight(60);
        person.setMoney(10000);

        Request request = new Request();
        request.setClassName("TestClass");
        request.setMethodName("testMethod");
        request.setParamTypes(new Class<?>[]{SimplePerson.class});
        request.setParams(new Object[]{person});


        TaggedFieldSerializer.TaggedFieldSerializerConfig config = new TaggedFieldSerializer.TaggedFieldSerializerConfig();
        config.setChunkedEncoding(true);

        //kryo最优方案的序列化大小
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(new SerializerFactory.TaggedFieldSerializerFactory(config));
        kryo.setRegistrationRequired(false);

        kryo.register(Class.class);
        kryo.register(Class[].class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(Request.class);

        System.out.println("kryo序列化person大小:" + serializeKryo(kryo,person).length);
        System.out.println("kryo序列化总大小:" +serializeKryo(kryo,request).length);


        //protobuff的大小
        SimplePersonProto.SimplePerson personProto = SimplePersonProto.SimplePerson.newBuilder()
                .setAge(person.getAge())
                .setHeight(person.getHeight())
                .setWeight(person.getWeight())
                .setMoney(person.getMoney())
                .setName(person.getName())
                .build();


        RequestProto.Request requestProto = RequestProto.Request.newBuilder()
                .setClassName("TestClass")
                .setMethodName("testMethod")
                .addParams(com.google.protobuf.Any.pack(personProto))
                .build();

        System.out.println("protobuff序列化person大小:" + personProto.toByteArray().length);
        System.out.println("protobuff序列化params大小:" + com.google.protobuf.Any.pack(personProto).toByteArray().length);
        System.out.println("protobuff序列化总大小:" + requestProto.toByteArray().length);


        List<SimplePerson> personList = new ArrayList<>();
        List<SimplePersonProto.SimplePerson> personList2 = new ArrayList<>();

        Random random = new Random();
        for(int i = 0;i<100;i++){
            SimplePerson p = new SimplePerson();
            p.setName(Generator.generateName());
            p.setAge(10+random.nextInt(30));
            p.setHeight(150+random.nextInt(30));
            p.setWeight(60+random.nextInt(20));
            p.setMoney(10000+random.nextInt(10000));

            personList.add(p);
            personList2.add(SimplePersonProto.SimplePerson.newBuilder()
                    .setAge(p.getAge())
                    .setHeight(p.getHeight())
                    .setWeight(p.getWeight())
                    .setMoney(p.getMoney())
                    .setName(p.getName())
                    .build());
        }

        System.out.println("***** List 比较 *****");

        System.out.println("kryo序列化list<person>大小:" + serializeKryo(kryo,personList).length);
        System.out.println("protobuff序列化list<person>大小:" + personList2.stream().mapToInt(e->e.toByteArray().length).sum());

        request = new Request();
        request.setClassName("TestClass");
        request.setMethodName("testMethod");
        request.setParamTypes(new Class<?>[]{List.class});
        request.setParams(new Object[]{personList});

        SimplePersonProto.SimplePersons persons = SimplePersonProto.SimplePersons.newBuilder().addAllItems(personList2).build();
        requestProto = RequestProto.Request.newBuilder()
                .setClassName("TestClass")
                .setMethodName("testMethod")
                .addParams(com.google.protobuf.Any.pack(persons))
                .build();

        System.out.println("kryo序列化request::list大小:" + serializeKryo(kryo,request).length);
        System.out.println("protobuff序列化request::list大小:" +requestProto.toByteArray().length);



    }

}
