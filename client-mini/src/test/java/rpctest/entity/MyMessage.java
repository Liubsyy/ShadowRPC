package rpctest.entity;


import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:58 PM
 **/
public class MyMessage implements Serializable {

    private String content;

    private int num;


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


    @Override
    public String toString() {
        return "MyMessage{" +
                "content='" + content + '\'' +
                ", num=" + num +
                '}';
    }
}