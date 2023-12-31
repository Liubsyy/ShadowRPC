package com.liubs.shadowrpc.research.serialize.strategy;

import com.google.gson.Gson;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:19 AM
 */
public class GsonSerialize  implements ISerialize{
    private static Gson gson = new Gson();

    @Override
    public byte[] serialize(Object obj) throws Exception {
        String json = gson.toJson(obj);
        return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        return gson.fromJson(new String(bytes), classz);
    }
}
