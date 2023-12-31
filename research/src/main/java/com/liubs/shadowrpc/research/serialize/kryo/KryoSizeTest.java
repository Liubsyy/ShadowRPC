package com.liubs.shadowrpc.research.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;
import com.liubs.shadowrpc.research.entity.Person;

import java.io.ByteArrayOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/4 11:51 AM
 */
public class KryoSizeTest {

    public static byte[] serialize(Kryo kryo,Object obj) throws Exception {
        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }

    public static void main(String[] args) throws Exception {

        /**
         *  47
         正常序列化大小:69
         正常序列化(注册)序列化大小:26
         CompatibleFieldSerializer序列化大小:119
         CompatibleFieldSerializer(注册)序列化大小:76
         TaggedFieldSerializer序列化大小:79
         TaggedFieldSerializer(注册)序列化大小:36
         TaggedFieldSerializer(trunked)序列化大小:97
         TaggedFieldSerializer(trunked注册)序列化大小:54
         VersionFieldSerializer序列化大小:70
         VersionFieldSerializer(注册)序列化大小:27
         */


        Person person = new Person();
        person.setSex((byte)1);
        person.setLike((short)500);
        person.setHair('h');
        person.setMan(person.getSex()==1);
        person.setName("Tom");
        person.setAge(18);
        person.setHeight(170);
        person.setWeight(60);
        person.setMoney(10000);

        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        System.out.println("正常序列化大小:" + serialize(kryo,person).length);

        kryo = new Kryo();
        kryo.register(Person.class);
        System.out.println("正常序列化(注册)序列化大小:" + serialize(kryo,person).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setRegistrationRequired(false);
        System.out.println("CompatibleFieldSerializer序列化大小:" + serialize(kryo,person).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.register(Person.class);
        System.out.println("CompatibleFieldSerializer(注册)序列化大小:" + serialize(kryo,person).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.setRegistrationRequired(false);
        System.out.println("TaggedFieldSerializer序列化大小:" + serialize(kryo,person).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.register(Person.class);
        System.out.println("TaggedFieldSerializer(注册)序列化大小:" + serialize(kryo,person).length);

        TaggedFieldSerializer.TaggedFieldSerializerConfig config = new TaggedFieldSerializer.TaggedFieldSerializerConfig();
        config.setChunkedEncoding(true);

        kryo = new Kryo();
        kryo.setDefaultSerializer(new TaggedFieldSerializerFactory.TaggedFieldSerializerFactory(config));
        kryo.setRegistrationRequired(false);
        System.out.println("TaggedFieldSerializer(trunked)序列化大小:" + serialize(kryo,person).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(new TaggedFieldSerializerFactory.TaggedFieldSerializerFactory(config));
        kryo.register(Person.class);
        System.out.println("TaggedFieldSerializer(trunked注册)序列化大小:" + serialize(kryo,person).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        kryo.setRegistrationRequired(false);
        System.out.println("VersionFieldSerializer序列化大小:" + serialize(kryo,person).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        kryo.register(Person.class);
        System.out.println("VersionFieldSerializer(注册)序列化大小:" + serialize(kryo,person).length);

    }
}
