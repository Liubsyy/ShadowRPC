package com.liubs.shadowrpc.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Liubsyy
 * @date 2023/12/29
 */
public class QPSTracker {
    private AtomicInteger requestCount = new AtomicInteger(0);
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public QPSTracker() {
        scheduler.scheduleAtFixedRate(this::logQPS, 1, 1, TimeUnit.SECONDS);
    }

    public void incrementRequestCount() {
        requestCount.incrementAndGet();
    }

    private void logQPS() {
        int qps = requestCount.getAndSet(0);
        System.out.println("Current QPS: " + qps);
        // 这里可以将QPS数据记录到日志或监控系统
    }
}
