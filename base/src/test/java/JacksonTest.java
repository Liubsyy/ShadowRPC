import com.fasterxml.jackson.core.JsonProcessingException;
import com.liubs.shadowrpc.base.util.JsonUtil;
import org.junit.Test;

/**
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class JacksonTest {


    @Test
    public void test() throws JsonProcessingException {
        MyObject obj = new MyObject();
        obj.setField1("value1");
        obj.setField2(123);

        String json = JsonUtil.serialize(obj);
        System.out.println(json);

        MyObject newObj = JsonUtil.deserialize(json, MyObject.class);
        System.out.println(newObj.getField1());
    }


    class MyObject {
        private String field1;
        private int field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public int getField2() {
            return field2;
        }

        public void setField2(int field2) {
            this.field2 = field2;
        }
    }
}
