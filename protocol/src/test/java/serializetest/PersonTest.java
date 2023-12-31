
package serializetest;


import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.liubs.shadowrpc.protocol.serializer.kryo.KryoSerializer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import serializetest.entity.Person;
import serializetest.entity.PersonProto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * 序列化 Person
 * @author Liubsyy
 * @date 2023/12/23 2:12 PM
 **/

public class PersonTest {

    private static List<String> names = Arrays.asList("Mr. Edison Zulauf","Mr. Leif Brown","Dr. Jeromy O'Connell","Ms. Sherley Champlin","Eddy Konopelski DVM","Narcisa Considine Jr.","Maria Donnelly V","Devin Walsh IV","Lorita Senger","Ferdinand Schumm","Lyle Wyman","Gabriele Braun","Ms. Katelin Greenfelder","Fidel Kemmer","Hans Grimes IV","Benton Steuber IV","Darby Becker","Renaldo Reichel","Leeann Nolan","Quyen Toy","France Thompson","Jenni Ledner","Caleb Batz V","Darron Stroman","Mr. Leonel Wisozk","Brain Schmitt","Leah Hermiston","Waneta Feil","Maia Tillman PhD","Andres Harris","Kymberly Weber","Colby Schowalter","Carey Ledner","Mrs. Melynda White","Elden Wolf","Shera Ortiz","Justin Mann","Percy Monahan V","Samatha Weimann","Aurelio Rodriguez","Era Bogan","Calista Shanahan","Miss Waylon Bins","Nicky Wiza","Deann Miller","Abdul Bailey","Lyndon Hyatt","Lyman Bins","Glenn Maggio","Mr. Azalee Hessel","Mrs. Joslyn Wiegand","Blaine DuBuque","Mr. Anamaria Brekke","Mayra Strosin","Bobbie Prohaska","Soo Legros","King Kulas","Mr. Kory Rosenbaum","Lenard Weissnat","Mr. Woodrow Rowe","Miss Shelby Gaylord","Geraldo Terry","Norbert Heathcote","Deidra Mueller","Miss Royce Wiegand","Edgardo Rippin","Terence Oberbrunner","Travis Waters","Lilian Bogisich","Terese Orn","Errol Rempel","Miss Chauncey Schaden","Miss Raymond Schamberger","Lottie Lang","Claud Dietrich II","Johnnie Kemmer Jr.","Ms. Rosalva Nicolas","Whitney Hirthe","Lizeth Nitzsche","Alona Price","Valerie Torphy","Eduardo Baumbach","Jude Carter","Mrs. Ezequiel Considine","Keely Jaskolski","Clemente Fahey","Marshall Kilback","Rashad Vandervort","Arla Leuschke Jr.","Saul McCullough PhD","Mrs. Ethan Wolf","Leilani Nader","Dr. Daryl Koelpin","Rodger Prosacco MD","Sylvester Gutkowski","Jolanda Marks","Keli Bergnaum","Pearlene Wunsch","Dr. Rocky Heaney","Tobias Zieme DDS");

    private static KryoSerializer kryoSerializer = new KryoSerializer();
    private static List<Person> persons = new ArrayList<>();
    private static List<PersonProto.Person> personsProto = new ArrayList<>();

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
    }

    public static  <T> void runSerialize(int count,List<T> persons,Function<T,byte[]> serialize, Function<byte[],T> deserialize) {

        long serializeTime = 0;
        long deserializeTime = 0;
        int  bytesSize = 0;


        for(int i = 0;i<count ;i++) {
            T person = persons.get(i);

            //序列化
            long serializeTime1 = System.nanoTime();
            byte[] bytes = serialize.apply(person);
            long serializeTime2 = System.nanoTime();
            serializeTime += serializeTime2 - serializeTime1;
            bytesSize += bytes.length;

            //反序列化
            long deserializeTime1 = System.nanoTime();
            T deserializePerson = deserialize.apply(bytes);
            long deserializeTime2 = System.nanoTime();
            deserializeTime += deserializeTime2 - deserializeTime1;

            Assert.assertEquals(person,deserializePerson);
        }

        System.out.printf("执行%d次, 序列化大小 %d bytes,用时 %d ns,反序列化用时 %d ns\n",count,bytesSize,serializeTime,deserializeTime);

    }

    @Test
    public void test(){

        System.out.println("*** kryo序列化和反序列化 ***");
        runSerialize(count,persons, p-> kryoSerializer.serialize(p),
                bytes-> kryoSerializer.deserialize(bytes, Person.class) );

        System.out.println("*** protobuf序列化和反序列化 *** ");
        runSerialize(count,personsProto, AbstractMessageLite::toByteArray,
                bytes -> {
                    try {
                        return PersonProto.Person.parseFrom(bytes);
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                });

    }



}
