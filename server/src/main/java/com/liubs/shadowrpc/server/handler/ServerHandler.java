package com.liubs.shadowrpc.server.handler;


import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.protocol.SerializeModule;
import com.liubs.shadowrpc.protocol.constant.ResponseCode;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.server.ServerModule;
import com.liubs.shadowrpc.server.service.ServiceLookUp;
import com.liubs.shadowrpc.server.service.ServiceTarget;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Liubsyy
 * @date 2023/12/3 10:23 PM
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private SerializeModule serializeModule = ModulePool.getModule(SerializeModule.class);
    private ServerModule serverModule = ModulePool.getModule(ServerModule.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端{} 已连接",ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端{} 断开连接",ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 打印验证影响速度，压测时去掉
        //logger.info("Server received: " + msg);

        IModelParser modelParser = serializeModule.getSerializer().getModelParser();

        RequestModel requestModel = modelParser.fromRequest(msg);

        executorService.execute(()->{
            try {

                ServiceLookUp serviceLookUp = new ServiceLookUp();
                serviceLookUp.setServiceName(requestModel.getServiceName());
                serviceLookUp.setMethodName(requestModel.getMethodName());
                serviceLookUp.setParamTypes(requestModel.getParamTypes());
                ServiceTarget targetRPC = serverModule.getRPC(serviceLookUp);

                Object result = targetRPC.invoke(requestModel.getParams());

                ResponseModel responseModel = new ResponseModel();
                responseModel.setTraceId(requestModel.getTraceId());
                responseModel.setCode(ResponseCode.SUCCESS.getCode());
                responseModel.setResult(result);

                // 响应客户端
                ctx.writeAndFlush(modelParser.toResponse(responseModel));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
       logger.error("exceptionCaught",cause);
        ctx.close();
    }


}
