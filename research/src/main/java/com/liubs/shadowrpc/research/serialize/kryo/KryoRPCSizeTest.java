package com.liubs.shadowrpc.research.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.Request;
import com.liubs.shadowrpc.research.entity.RequestSimple;

import java.io.ByteArrayOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/4 12:05 PM
 */
public class KryoRPCSizeTest {
    public static byte[] serialize(Kryo kryo, Object obj) throws Exception {
        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }
    

    private static void register(Kryo kryo,boolean whole) {
        kryo.register(Class.class);
        kryo.register(Class[].class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(Request.class);
        kryo.register(RequestSimple.class);

        if(whole) {
            kryo.register(Person.class);
        }
    }


    public static void main(String[] args) throws Exception {

        /**
         * 127（ 序列化com.liubs.shadowrpc.research.entity.Person的时候投机取巧用了简化名Person，严格上算应该是163 ）
         正常序列化大小:95
         正常序列化(注册)序列化大小:51
         CompatibleFieldSerializer序列化大小:191
         CompatibleFieldSerializer(注册)序列化大小:147
         TaggedFieldSerializer序列化大小:111
         TaggedFieldSerializer(注册)序列化大小:67
         TaggedFieldSerializer(trunked)序列化大小:148
         TaggedFieldSerializer(trunked注册)序列化大小:104
         VersionFieldSerializer序列化大小:97
         VersionFieldSerializer(注册)序列化大小:53
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

        Request request = new Request();
        request.setClassName("TestClass");
        request.setMethodName("testMethod");
        request.setParamTypes(new Class<?>[]{Person.class});
        request.setParams(new Object[]{person});

        RequestSimple requestSimple = new RequestSimple();
        requestSimple.setClassName("TestClass");
        requestSimple.setMethodName("testMethod");
        requestSimple.setParamTypes(new String[]{(Person.class.getName())});
        requestSimple.setParams(new Object[]{person});


        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("正常序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        register(kryo,true);
        System.out.println("正常序列化(注册)序列化大小:" + serialize(kryo,request).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("CompatibleFieldSerializer序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        register(kryo,true);
        System.out.println("CompatibleFieldSerializer(注册)序列化大小:" + serialize(kryo,request).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("TaggedFieldSerializer序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        register(kryo,true);
        System.out.println("TaggedFieldSerializer(注册)序列化大小:" + serialize(kryo,request).length);

        TaggedFieldSerializer.TaggedFieldSerializerConfig config = new TaggedFieldSerializer.TaggedFieldSerializerConfig();
        config.setChunkedEncoding(true);

        kryo = new Kryo();
        kryo.setDefaultSerializer(new SerializerFactory.TaggedFieldSerializerFactory(config));
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("TaggedFieldSerializer(trunked)序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(new SerializerFactory.TaggedFieldSerializerFactory.TaggedFieldSerializerFactory(config));
        register(kryo,true);
        System.out.println("TaggedFieldSerializer(trunked注册)序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(new SerializerFactory.TaggedFieldSerializerFactory.TaggedFieldSerializerFactory(config));
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("TaggedFieldSerializer(trunked)序列化requestSimple大小:" + serialize(kryo,requestSimple).length);


        kryo = new Kryo();
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        kryo.setRegistrationRequired(false); register(kryo,false);
        System.out.println("VersionFieldSerializer序列化大小:" + serialize(kryo,request).length);

        kryo = new Kryo();
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        register(kryo,true);
        System.out.println("VersionFieldSerializer(注册)序列化大小:" + serialize(kryo,request).length);

    }
}
