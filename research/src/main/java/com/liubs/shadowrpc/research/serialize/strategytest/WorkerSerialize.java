package com.liubs.shadowrpc.research.serialize.strategytest;

import com.liubs.shadowrpc.research.entity.Generator;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.Worker;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/2 12:03 PM
 */
public class WorkerSerialize {

    public static void main(String[] args) throws Exception {
        int count = 1000000;

        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<Worker> workers = new ArrayList<>();
        for(int i=0;i<count;i++) {
            Worker worker = new Worker();
            worker.setWorkAddr(Generator.generateAddress());
            worker.setInfo(Person.generatePerson());
            worker.setFriends(new ArrayList<>());
            for(int j = 0;j<10;j++){
                worker.getFriends().add(Person.generatePerson());
            }
            worker.setExtendInfo(new HashMap<>());
            worker.getExtendInfo().put("asdasdasd","111");
            worker.getExtendInfo().put("qwewrtrhb","222");
            workers.add(worker);
        }


        for(ISerialize serialize : ISerialize.SERIALIZES){
            timeSizeStat.begin();
            for(int i = 0;i<count ;i++) {
                byte[] serializeBytes = serialize.serialize(workers.get(i));
                timeSizeStat.addSize(serializeBytes.length);
            }
            long useNs = timeSizeStat.getUseTime();
            System.out.printf("[%s]%d次累计用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),count,useNs,useNs/1000000,timeSizeStat.getSize());
        }

    }
}
