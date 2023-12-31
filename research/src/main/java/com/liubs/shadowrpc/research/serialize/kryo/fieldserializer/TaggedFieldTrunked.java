package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.VersionPerson;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:15 AM
 **/
public class TaggedFieldTrunked extends BaseSerializer {
    public TaggedFieldTrunked() {
        super(null);

        kryo = new Kryo();
        TaggedFieldSerializer.TaggedFieldSerializerConfig config = new TaggedFieldSerializer.TaggedFieldSerializerConfig();
        config.setChunkedEncoding(true);
        kryo.setDefaultSerializer(new SerializerFactory.TaggedFieldSerializerFactory.TaggedFieldSerializerFactory(config));

        kryo.register(Person.class);
        kryo.register(VersionPerson.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.HashMap.class);
    }
}
