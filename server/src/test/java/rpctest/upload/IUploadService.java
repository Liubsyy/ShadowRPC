package rpctest.upload;

import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;

/**
 * @author Liubsyy
 * @date 2024/1/14
 **/
@ShadowInterface
public interface IUploadService {
    boolean upload(String name,byte[] bytes);
}
