package com.liubs.shadowrpc.research.serialize.strategytest;

import com.liubs.shadowrpc.research.entity.User;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 序列化User性能比较
 * @author Liubsyy
 * @date 2023/12/2 10:10 AM
 */
public class UserSerialize {

    private static void runWithNum(int n) throws Exception {

        //先初始化序列化的数据
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<User> userList = new ArrayList<>();
        Random random = new Random();
        for(int i = 0;i<n ;i++) {
            User userItem = new User();
            userItem.setName("liubs"+random.nextInt(n));
            userItem.setWechatPub("微信号：liubsyy"+random.nextInt(n));
            userItem.setJob("优秀工程师"+i);

            userList.add(userItem);
        }

        for(ISerialize serialize : ISerialize.SERIALIZES){
            timeSizeStat.begin();
            for(int i = 0;i<n ;i++) {
                byte[] serializeBytes = serialize.serialize(userList.get(i));
                timeSizeStat.addSize(serializeBytes.length);
            }
            long useNs = timeSizeStat.getUseTime();
            System.out.printf("[%s]%d次累计用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),n,useNs,useNs/1000000,timeSizeStat.getSize());
        }
    }


    public static void main(String[] args) throws Exception {

        System.out.println("******** 预热 ********");
        runWithNum(1);


//        System.out.println("******** 单对象累计1次总时长比较 ********");
//        runWithNum(1);
//
//        System.out.println("******** 单对象累计10次总时长比较 ********");
//        runWithNum(10);
//
//        System.out.println("******** 单对象累计100次总时长比较 ********");
//        runWithNum(100);
//
//        System.out.println("******** 单对象累计1000次总时长比较 ********");
//        runWithNum(1000);
//
//        System.out.println("******** 单对象累计1w次总时长比较 ********");
//        runWithNum(10000);
//
//        System.out.println("******** 单对象累计10w次总时长比较 ********");
//        runWithNum(100000);
//
        System.out.println("******** 单对象累计100w次总时长比较 ********");
        runWithNum(1000000);




    }

}
