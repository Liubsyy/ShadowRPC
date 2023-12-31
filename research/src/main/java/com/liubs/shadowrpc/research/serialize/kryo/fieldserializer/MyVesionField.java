package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;

import java.lang.reflect.Field;

import static com.esotericsoftware.kryo.Kryo.NULL;
import static com.esotericsoftware.minlog.Log.*;

/**
 * @author Liubsyy
 * @date 2023/12/3 1:34 PM
 **/
public class MyVesionField extends BaseSerializer {
    public MyVesionField() {
        super(MyVersionFieldSerializer.class);
    }

    public static class MyVersionFieldSerializer <T> extends FieldSerializer<T> {
        private final VersionFieldSerializer.VersionFieldSerializerConfig config;
        private int typeVersion; // Version of the type being serialized.
        private int[] fieldVersion; // Version of each field.

        public MyVersionFieldSerializer (Kryo kryo, Class type) {
            this(kryo, type, new VersionFieldSerializer.VersionFieldSerializerConfig());
        }

        public MyVersionFieldSerializer (Kryo kryo, Class type, VersionFieldSerializer.VersionFieldSerializerConfig config) {
            super(kryo, type, config);
            this.config = config;
            setAcceptsNull(true);
            // Make sure this is done before any read/write operations.
            initializeCachedFields();
        }

        protected void initializeCachedFields () {
            CachedField[] fields = getFields();
            fieldVersion = new int[fields.length];
            for (int i = 0, n = fields.length; i < n; i++) {
                Field field = fields[i].getField();
                VersionFieldSerializer.Since since = field.getAnnotation(VersionFieldSerializer.Since.class);
                if (since != null) {
                    fieldVersion[i] = since.value();
                    // Use the maximum version among fields as the entire type's version.
                    typeVersion = Math.max(fieldVersion[i], typeVersion);
                } else {
                    fieldVersion[i] = 0;
                }
            }
            if (DEBUG) debug("Version for type " + getType().getName() + ": " + typeVersion);
        }

        public void removeField (String fieldName) {
            super.removeField(fieldName);
            initializeCachedFields();
        }

        public void removeField (CachedField field) {
            super.removeField(field);
            initializeCachedFields();
        }

        public void write (Kryo kryo, Output output, T object) {
            if (object == null) {
                output.writeByte(NULL);
                return;
            }

            int pop = pushTypeVariables();

            CachedField[] fields = getFields();
            // Write type version.
            output.writeVarInt(typeVersion + 1, true);
            // Write fields.
            for (int i = 0, n = fields.length; i < n; i++) {
                if (TRACE) log("Write", fields[i], output.position());
                fields[i].write(output, object);
            }

            popTypeVariables(pop);
        }

        public T read (Kryo kryo, Input input, Class<? extends T> type) {
            int version = input.readVarInt(true);
            if (version == NULL) return null;
            version--;
            if (!config.getCompatible() && version != typeVersion)
                throw new KryoException("Version is not compatible: " + version + " != " + typeVersion);

            int pop = pushTypeVariables();

            T object = create(kryo, input, type);
            kryo.reference(object);

            CachedField[] fields = getFields();
            for (int i = 0, n = fields.length; i < n; i++) {
                // Field is not present in input, skip it.
                if (fieldVersion[i] > version) {
                    if (DEBUG) debug("Skip field: " + fields[i].getField().getName());
                    continue;
                }
                if (TRACE) log("Read", fields[i], input.position());
                fields[i].read(input, object);
            }

            popTypeVariables(pop);
            return object;
        }

        public VersionFieldSerializer.VersionFieldSerializerConfig getVersionFieldSerializerConfig () {
            return config;
        }

    }
}
