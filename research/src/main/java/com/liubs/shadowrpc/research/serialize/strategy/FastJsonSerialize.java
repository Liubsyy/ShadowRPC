package com.liubs.shadowrpc.research.serialize.strategy;

import com.alibaba.fastjson2.JSON;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:14 AM
 */
public class FastJsonSerialize implements ISerialize{
    @Override
    public byte[] serialize(Object obj) throws Exception {
        String json = JSON.toJSONString(obj);
        return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public <T> T unSerialize(Class<T> classz,byte[] bytes) throws Exception {
        return JSON.parseObject(bytes ,classz);
    }
}
