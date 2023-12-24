package rpctest.hello;

import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;
import rpctest.entity.MyMessageProto;

/**
 * @author Liubsyy
 * @date 2023/12/25 12:48 AM
 **/
@ShadowInterface
public interface IHelloProto {
    MyMessageProto.MyMessage say(MyMessageProto.MyMessage message);
}
