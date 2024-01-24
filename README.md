## ShadowRPC
一个基于netty的rpc开源框架，简单易用，零配置，可同步和异步调用，不断更新完善中...<br><br>

### 主要模块
- protocol : 协议层，包含应用层通信协议，以及序列化/反序列化，支持kryo和protobuf
- registry : 注册模块，基于zookeeper作为注册中心，包含注册服务和服务发现
- server : 服务端
- client : 客户端
- client-mini : 不依赖任何包的客户端，基于NIO

### 使用步骤

1.定义实体, 加上注解
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

2.编写接口加上@ShadowInterface注解

```java
@ShadowInterface
public interface IHello {
    String hello(String msg);
    MyMessage say(MyMessage message);
}
```

如果采用protobuf作为通信协议，由于可实现跨语言，所以所有函数参数类型和返回类型都必须是proto文件定义的类型
```java
@ShadowInterface
public interface IHelloProto {
    MyMessageProto.MyMessage say(MyMessageProto.MyMessage message);
}
```


然后编写服务实现类
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

3.指定序列化类型和端口，启动服务端<br>

  
单点启动模式如下: 
```java
ServerConfig serverConfig = new ServerConfig();
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setPort(2023);

ServerBuilder.newBuilder()
        .serverConfig(serverConfig)
        .addPackage("rpctest.hello")
        .build()
        .start();
```

使用zk作为集群模式启动
```java
String ZK_URL = "localhost:2181";
ServerConfig serverConfig = new ServerConfig();
serverConfig.setGroup("DefaultGroup");
serverConfig.setPort(2023);
serverConfig.setRegistryUrl(ZK_URL);
serverConfig.setQpsStat(true); //统计qps
serverConfig.setSerializer(SerializerEnum.KRYO.name());
ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start();
```



4.客户端用调用远程rpc接口
   
```java
ModulePool.getModule(ClientModule.class).init(new ClientConfig());

ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
shadowClient.init();

IHello helloService = shadowClient.createRemoteProxy(IHello.class,"shadowrpc://DefaultGroup/helloservice");

logger.info("发送 hello 消息");
String helloResponse = helloService.hello("Tom");
logger.info("hello 服务端响应:"+helloResponse);

MyMessage message = new MyMessage();
message.setNum(100);
message.setContent("Hello, Server!");

System.out.printf("发送请求 : %s\n",message);
MyMessage response = helloService.say(message);
System.out.printf("接收服务端消息 : %s\n",response);
```

<br>

使用zk作为服务发现负载均衡调用各个服务器：
```java
ClientConfig config = new ClientConfig();
config.setSerializer(SerializerStrategy.KRYO.name());
ModulePool.getModule(ClientModule.class).init(config);
String ZK_URL="localhost:2181";
ShadowClientGroup shadowClientGroup = new ShadowClientGroup(ZK_URL);
shadowClientGroup.init();

IHello helloService = shadowClientGroup.createRemoteProxy(IHello.class, "shadowrpc://DefaultGroup/helloservice");
List<ShadowClient> shadowClientList = shadowClientGroup.getShadowClients("DefaultGroup");

System.out.println("所有服务器: "+shadowClientList.stream().map(c-> c.getRemoteIp()+":"+c.getRemotePort()).collect(Collectors.toList()));

for(int i = 0 ;i<shadowClientList.size() * 5; i++) {
    String hello = helloService.hello(i + "");
    System.out.println(hello);
}
```


