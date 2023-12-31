package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:16 AM
 **/
public class VersionField extends BaseSerializer {
    public VersionField() {
        super(VersionFieldSerializer.class);
    }
}
