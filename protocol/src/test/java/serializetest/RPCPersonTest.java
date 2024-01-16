package serializetest;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.base.config.BaseConfig;
import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.protocol.SerializeModule;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestAnyProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.protocol.serializer.kryo.KryoSerializer;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
import org.junit.BeforeClass;
import org.junit.Test;
import serializetest.entity.Person;
import serializetest.entity.PersonProto;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 一次RPC请求的序列化和反序列化用时
 * @author Liubsyy
 * @date 2023/12/23 3:09 PM
 **/
public class RPCPersonTest {
    private static List<String> names = Arrays.asList("Mr. Edison Zulauf","Mr. Leif Brown","Dr. Jeromy O'Connell","Ms. Sherley Champlin","Eddy Konopelski DVM","Narcisa Considine Jr.","Maria Donnelly V","Devin Walsh IV","Lorita Senger","Ferdinand Schumm","Lyle Wyman","Gabriele Braun","Ms. Katelin Greenfelder","Fidel Kemmer","Hans Grimes IV","Benton Steuber IV","Darby Becker","Renaldo Reichel","Leeann Nolan","Quyen Toy","France Thompson","Jenni Ledner","Caleb Batz V","Darron Stroman","Mr. Leonel Wisozk","Brain Schmitt","Leah Hermiston","Waneta Feil","Maia Tillman PhD","Andres Harris","Kymberly Weber","Colby Schowalter","Carey Ledner","Mrs. Melynda White","Elden Wolf","Shera Ortiz","Justin Mann","Percy Monahan V","Samatha Weimann","Aurelio Rodriguez","Era Bogan","Calista Shanahan","Miss Waylon Bins","Nicky Wiza","Deann Miller","Abdul Bailey","Lyndon Hyatt","Lyman Bins","Glenn Maggio","Mr. Azalee Hessel","Mrs. Joslyn Wiegand","Blaine DuBuque","Mr. Anamaria Brekke","Mayra Strosin","Bobbie Prohaska","Soo Legros","King Kulas","Mr. Kory Rosenbaum","Lenard Weissnat","Mr. Woodrow Rowe","Miss Shelby Gaylord","Geraldo Terry","Norbert Heathcote","Deidra Mueller","Miss Royce Wiegand","Edgardo Rippin","Terence Oberbrunner","Travis Waters","Lilian Bogisich","Terese Orn","Errol Rempel","Miss Chauncey Schaden","Miss Raymond Schamberger","Lottie Lang","Claud Dietrich II","Johnnie Kemmer Jr.","Ms. Rosalva Nicolas","Whitney Hirthe","Lizeth Nitzsche","Alona Price","Valerie Torphy","Eduardo Baumbach","Jude Carter","Mrs. Ezequiel Considine","Keely Jaskolski","Clemente Fahey","Marshall Kilback","Rashad Vandervort","Arla Leuschke Jr.","Saul McCullough PhD","Mrs. Ethan Wolf","Leilani Nader","Dr. Daryl Koelpin","Rodger Prosacco MD","Sylvester Gutkowski","Jolanda Marks","Keli Bergnaum","Pearlene Wunsch","Dr. Rocky Heaney","Tobias Zieme DDS");

    private static KryoSerializer kryoSerializer = new KryoSerializer();
    private static List<Person> persons = new ArrayList<>();
    private static List<PersonProto.Person> personsProto = new ArrayList<>();
    private static SerializeModule serializeModule = ModulePool.getModule(SerializeModule.class);


    final static int count = 10000;

    @BeforeClass
    public static void init(){

        Random random = new Random();

        for(int i = 0;i < count ;i++) {
            Person person = new Person();
            person.setName(  names.get(new Random().nextInt(names.size())) );
            person.setAge(10+random.nextInt(30));
            person.setHeight(150+random.nextInt(30));
            person.setWeight(60+random.nextInt(20));
            person.setMoney(10000+random.nextInt(10000));

            PersonProto.Person personProto = PersonProto.Person.newBuilder().setName(person.getName())
                    .setAge(person.getAge())
                    .setHeight(person.getHeight())
                    .setWeight(person.getWeight())
                    .setMoney(person.getMoney())
                    .setAge(person.getAge()).build();

            persons.add(person);
            personsProto.add(personProto);
        }

        serializeModule.init(new BaseConfig(), Collections.singletonList("serializetest.entity"));
    }

    @Test
    public void test(){
        System.out.println("*** kryo序列化和反序列化 ***");

        PersonTest.runSerialize(count,persons,
                p-> {
                    ShadowRPCRequest request = new ShadowRPCRequest();
                    request.setTraceId("asd123");
                    request.setServiceName("PersonService");
                    request.setMethodName("getPerson");
                    request.setParamTypes(new Class[]{Person.class});
                    request.setParams(new Object[]{p});
                    return kryoSerializer.serialize(request);
                },
                bytes-> {
                    ShadowRPCRequest deserialize = kryoSerializer.deserialize(bytes, ShadowRPCRequest.class);
                    return (Person) deserialize.getParams()[0];
                });


        System.out.println("*** protobuf(any)序列化和反序列化 *** ");
        PersonTest.runSerialize(count,personsProto,p-> {
                    ShadowRPCRequestAnyProto.ShadowRPCRequestAny request = ShadowRPCRequestAnyProto.ShadowRPCRequestAny
                            .newBuilder()
                            .setTraceId("asd123")
                            .setServiceName("PersonService")
                            .setMethodName("getPerson")
                            .addParams(com.google.protobuf.Any.pack(p))
                            .build();

                    return request.toByteArray();
                },

                bytes -> {
                    try {
                        ShadowRPCRequestAnyProto.ShadowRPCRequestAny request = ShadowRPCRequestAnyProto.ShadowRPCRequestAny.parseFrom(bytes);
                        PersonProto.Person person = request.getParamsList().get(0).unpack(PersonProto.Person.class);
                        return person;
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                });

        System.out.println("*** protobuf序列化和反序列化(反射person参数) *** ");
        PersonTest.runSerialize(count,personsProto,p-> {
                    ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest
                            .newBuilder()
                            .setTraceId("asd123")
                            .setServiceName("PersonService")
                            .setMethodName("getPerson")
                            .addParamTypes(p.getClass().getName())
                            .addParams(p.toByteString())
                            .build();
                    return request.toByteArray();
                },

                bytes -> {
                    try {
                        ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest.parseFrom(bytes);

                        ByteString data = request.getParams(0);

                        //反射严重影响性能
                        Class<?> classz = Class.forName(request.getParamTypes(0));
                        Method parseFrom = classz.getDeclaredMethod("parseFrom", ByteString.class);
                        Object obj = parseFrom.invoke(null, data);

                        PersonProto.Person person = (PersonProto.Person)obj;
                        return person;

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        System.out.println("*** protobuf序列化和反序列化 *** ");

        PersonTest.runSerialize(count,personsProto,p-> {
                    ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest
                            .newBuilder()
                            .setTraceId("asd123")
                            .setServiceName("PersonService")
                            .setMethodName("getPerson")
                            .addParamTypes(p.getClass().getName())
                            .addParams(p.toByteString())
                            .build();
                    return request.toByteArray();
                },

                bytes -> {
                    try {
                        ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest.parseFrom(bytes);

                        MessageLite messageLite = ParserForType.getMessage(request.getParamTypes(0));
                        ByteString data = request.getParams(0);
                        return ParserForType.parseFrom(messageLite, data.toByteArray());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });




    }




}
