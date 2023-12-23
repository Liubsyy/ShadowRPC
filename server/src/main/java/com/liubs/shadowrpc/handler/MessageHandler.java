package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.HeartBeatMessage;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * 消息的压缩和解压缩
 * @author Liubsyy
 * @date 2023/12/15 10:01 PM
 **/
public class MessageHandler extends ByteToMessageCodec<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data;
        if(msg instanceof HeartBeatMessage) {
            //心跳消息
            data = HeartBeatMessage.getHearBeatMsg();
        }else {
            data = SerializerManager.getInstance().serialize(msg);
        }

        int dataLength = data.length;
        out.writeInt(dataLength); // 先写入消息长度
        out.writeBytes(data); // 写入序列化后的数据
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if(readableBytes <= 0){
            return;
        }
        byte[] data = new byte[readableBytes];

        in.readBytes(data);

        if(HeartBeatMessage.isHeartBeatMsg(data)) {
            System.out.println("收到心跳消息...");
        }else {
            Object obj = SerializerManager.getInstance().deserializeRequest(data);
            out.add(obj);
        }
    }

}
