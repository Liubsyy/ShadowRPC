package com.liubs.shadowrpc.research.serialize.strategy;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Liubsyy
 * @date 2023/12/2 3:50 PM
 */
public class HessianSerialize implements ISerialize {

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = null;
        try {
            hessian2Output = new Hessian2Output(bos);
            hessian2Output.writeObject(obj);
            hessian2Output.flush(); // 确保所有数据都写入到 ByteArrayOutputStream
        } catch (IOException e) {
            e.printStackTrace(); // 或者处理异常
        } finally {
            if (hessian2Output != null) {
                try {
                    hessian2Output.close(); // 关闭流
                } catch (IOException e) {
                    e.printStackTrace(); // 或者处理关闭流时的异常
                }
            }
        }
        return bos.toByteArray();
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(bis);
        Object obj = hessian2Input.readObject();
        return (T) obj;
    }
}
