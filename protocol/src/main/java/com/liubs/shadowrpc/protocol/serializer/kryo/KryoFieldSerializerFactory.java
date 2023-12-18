package com.liubs.shadowrpc.protocol.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:43 PM
 **/
public class KryoFieldSerializerFactory extends SerializerFactory.BaseSerializerFactory<KryoFieldSerializer> {
    private final TaggedFieldSerializer.TaggedFieldSerializerConfig config;

    public KryoFieldSerializerFactory () {
        this.config = new TaggedFieldSerializer.TaggedFieldSerializerConfig();

        //chunkedEncoding设置为true, 写入字段长度，这样一方新增字段后，其他端序列化和反序列化都能识别并选择跳过
        this.config.setChunkedEncoding(true);

    }

    public TaggedFieldSerializer.TaggedFieldSerializerConfig getConfig () {
        return config;
    }

    public KryoFieldSerializer newSerializer (Kryo kryo, Class type) {
        return new KryoFieldSerializer(kryo, type, config.clone());
    }
}