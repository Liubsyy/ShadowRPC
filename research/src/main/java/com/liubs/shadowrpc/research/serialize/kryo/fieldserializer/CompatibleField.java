package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:12 AM
 **/
public class CompatibleField  extends BaseSerializer {
    public CompatibleField() {
        super(CompatibleFieldSerializer.class);
    }
}
