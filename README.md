### ShadowRPC
一个基于netty的rpc开源框架，简单易用，零配置，可同步和异步调用，可全双工通信和发消息，可保持状态，可上传大文件，直接引用jar包即可快速启动<br><br>
本rpc框架追求精简至上，不强制注册中心，不强制使用spring，尽可能少引入一些乱七八糟的包和依赖<br><br>

主要模块
- protocol : 协议层，包含应用层通信协议，以及序列化和反序列化，支持kryo和protobuf序列化/反序列化, 其中protobuf可实现跨语言
- registry : 注册模块，基于zookeeper作为注册中心，包含注册服务和服务发现
- server : 服务端
- client : 客户端

使用步骤
1. 定义实体, 加上注解
```java
@ShadowEntity
public class MyMessage {
    @ShadowField(1)
    private String content;

    @ShadowField(2)
    private int num;
}
```
如果是protobuf方式，编写proto文件
```proto
message MyMessage {
    string content = 1;
    int32 num = 2;
}
```
然后直接用maven插件protobuf-maven-plugin生成实体

<br>
2. 编写接口如果是protobuf方式，因为要跨语言，所以所有函数参数类型和返回类型都必须是proto文件定义的类型

```java
@ShadowInterface
public interface IHello {
    String hello(String msg);
    MyMessage say(MyMessage message);
}
```

编写服务实现类
```java
@ShadowService(serviceName = "helloservice")
public class HelloService implements IHello {
    @Override
    public String hello(String msg) {
        return "Hello,"+msg;
    }
    @Override
    public MyMessage say(MyMessage message) {
        MyMessage message1 = new MyMessage();
        message1.setContent("hello received "+"("+message.getContent()+")");
        message1.setNum(message.getNum()+1);
        return message1;
    }
}
```

<br>
3. 指定序列化类型和端口，启动服务端

<br>

  
单点启动模式如下: 
```java
SerializerManager.getInstance().setSerializer(SerializerEnum.KRYO);
ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(2023)
                .keep();
```

使用zk作为集群模式启动
```java
String ZK_URL = "localhost:2181";
SerializerManager.getInstance().setSerializer(SerializerEnum.KRYO);
ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(ZK_URL,2023)
                .keep();
```



4. 客户端用接口调用远程函数
   
```java
ShadowClient shadowClient = new ShadowClient();
shadowClient.init("127.0.0.1",2023);


IHello helloService = RemoteServerProxy.create(shadowClient.getChannel(),IHello.class,"helloservice");

System.out.println("发送 hello 消息");
String helloResponse = helloService.hello("Tom");
System.out.println("hello 服务端响应:"+helloResponse);

MyMessage message = new MyMessage();
message.setNum(100);
message.setContent("Hello, Server!");

System.out.printf("发送请求 : %s\n",message);
MyMessage response = helloService.say(message);
System.out.printf("接收服务端消息 : %s\n",response);
```

<br>

使用zk作为服务发现复杂均衡调用各个服务器：
```java
String ZK_URL = "localhost:2181";
ShadowClientsManager.getInstance().connectZk(ZK_URL);
List<ShadowClient> shadowClientList = ShadowClientsManager.getInstance().getShadowClients();

System.out.println("所有服务器: "+shadowClientList.stream().map(ShadowClient::getConnectionUrl).collect(Collectors.toList()));

IHello helloService = RemoteServerProxy.create(IHello.class,"helloservice");

int helloCount = shadowClientList.size() * 5;
for(int i = 0 ;i<helloCount; i++) {
    helloService.hello(i+"");
}
```


<br>
目前框架刚刚搭建，可以说只是demo级的，后续我将深入研究并打磨一款真正实用的RPC框架
