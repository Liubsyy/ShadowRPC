package com.liubs.shadowrpc.protocol.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.util.IntMap;
import com.liubs.shadowrpc.protocol.annotation.ShadowField;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.esotericsoftware.minlog.Log.TRACE;
import static com.esotericsoftware.minlog.Log.trace;

/**
 *  基于TaggedFieldSerializer做序列化
 *  使用自定义注解 @ShadowField 标记序列化的字段，能解决增加字段或者缺失字段另一端反序列化的问题
 *  同时性能也比CompatibleFieldSerializer高
 * @author Liubsyy
 * @date 2023/12/18 10:42 PM
 **/
public class KryoFieldSerializer<T> extends TaggedFieldSerializer<T> {

    public KryoFieldSerializer(Kryo kryo, Class type, TaggedFieldSerializerConfig config) {
        super(kryo, type, config);
    }

    @Override
    protected void initializeCachedFields () {
        CachedField[] fields = super.getFields();
        // Remove untagged fields.
        for (int i = 0, n = fields.length; i < n; i++) {
            Field field = fields[i].getField();
            if (field.getAnnotation(ShadowField.class) == null) {
                if (TRACE) trace("kryo", "Ignoring field without tag: " + fields[i]);
                super.removeField(fields[i]);
            }
        }
        fields = super.getFields(); // removeField changes cached field array.

        // Cache tag values.
        ArrayList writeTags = new ArrayList(fields.length);
        IntMap readTags = new IntMap((int)(fields.length / 0.8f));
        super.setReadTags(readTags);
        for (CachedField cachedField : fields) {
            Field field = CachedFieldAccess.getField(cachedField);
            int tag = field.getAnnotation(ShadowField.class).value();
            if (readTags.containsKey(tag))
                throw new KryoException(String.format("Duplicate tag %d on fields: %s and %s", tag, field, writeTags.get(tag)));
            readTags.put(tag, cachedField);
            if (field.getAnnotation(Deprecated.class) == null) writeTags.add(cachedField);
            CachedFieldAccess.setTag(cachedField,tag);
        }
        super.setWriteTags((CachedField[])writeTags.toArray(new CachedField[writeTags.size()]));
    }

}