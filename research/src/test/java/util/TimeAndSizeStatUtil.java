package util;

/**
 * @author Liubsyy
 * @date 2023/11/30
 */
public class TimeAndSizeStatUtil {
    private static long time1;
    private static long size;

    public static void  begin() {
        time1 = System.nanoTime();
        size = 0;
    }
    public static long getUseTime(){
        return System.nanoTime() - time1;
    }

    public static void addSize(int size){
        TimeAndSizeStatUtil.size += size;
    }

    public static long getSize() {
        return size;
    }
}
