package com.liubs.shadowrpc.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Liubsyy
 * @date 2023/12/29
 */
public class QpsStatHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(QpsStatHandler.class);

    //请求数量
    private static AtomicLong activeRequests = new AtomicLong(0);

    //每秒请求量
    private static AtomicInteger perSecondsRequests = new AtomicInteger(0);

    // 定时任务，用于计算QPS
    static {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            int qps = perSecondsRequests.getAndSet(0);
            if(qps > 0) {
                logger.info("Current QPS: " + qps);
            }

            // 可以进一步将QPS记录到日志或监控系统
        }, 1, 1, TimeUnit.SECONDS);
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise).addListener((f)->{
            if(f.isSuccess()) {
                perSecondsRequests.incrementAndGet();
                activeRequests.incrementAndGet();
            }
        });
    }
}
