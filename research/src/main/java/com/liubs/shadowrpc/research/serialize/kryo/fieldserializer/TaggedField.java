package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:15 AM
 **/
public class TaggedField extends BaseSerializer {
    public TaggedField() {
        super(TaggedFieldSerializer.class);
    }
}
