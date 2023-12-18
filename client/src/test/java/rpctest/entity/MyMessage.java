package rpctest.entity;

import com.liubs.shadowrpc.protocol.annotation.ShadowEntity;
import com.liubs.shadowrpc.protocol.annotation.ShadowField;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:58 PM
 **/
@ShadowEntity
public class MyMessage {

    @ShadowField(1)
    private String content;

    @ShadowField(2)
    private int num;

    /**
     * 客户端新增字段
     * request: 客户端新增字段，服务端没有这个字段，服务端会跳过这个字段进行反序列化
     * response: 服务端没有这个字段，客户端能缺省默认值反序列化
     */
    @ShadowField(3)
    private String addField = "addField1";

    public MyMessage() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAddField() {
        return addField;
    }

    public void setAddField(String addField) {
        this.addField = addField;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "content='" + content + '\'' +
                ", num=" + num +
                ", addField=" + addField +
                '}';
    }
}