package serialize.obj;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/1
 */
public class UserDTO implements Serializable {

    private String name;
    private String wechatPub;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechatPub() {
        return wechatPub;
    }

    public void setWechatPub(String wechatPub) {
        this.wechatPub = wechatPub;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
