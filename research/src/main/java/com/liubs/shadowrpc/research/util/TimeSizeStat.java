package com.liubs.shadowrpc.research.util;

/**
 * @author Liubsyy
 * @date 2023/12/2
 */
public class TimeSizeStat {
    private long time1;
    private long size;


    private TimeSizeStat(){}

    public static TimeSizeStat newInstance(){
        return new TimeSizeStat();
    }

    public void  begin() {
        time1 = System.nanoTime();
        size = 0;
    }
    public long getUseTime(){
        return System.nanoTime() - time1;
    }

    public void addSize(int size){
        this.size += size;
    }

    public long getSize() {
        return size;
    }
}
