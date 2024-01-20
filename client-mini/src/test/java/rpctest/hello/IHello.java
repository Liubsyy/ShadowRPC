package rpctest.hello;

import com.liubs.shadowrpc.base.annotation.ShadowInterface;
import rpctest.entity.MyMessage;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:59 PM
 **/
@ShadowInterface
public interface IHello {
    String hello(String msg);
    MyMessage say(MyMessage message);
}
