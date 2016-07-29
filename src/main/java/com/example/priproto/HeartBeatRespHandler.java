package com.example.priproto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yuanye on 2016/7/29.
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value) {
            log.info("接收客户端心跳：" + message);
            NettyMessage heartBeat = buildHeartBeat();
            log.info("返回心跳：" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage heartBeat = new NettyMessage();
        Header header = new Header();
        header.setType((byte) MessageType.HEARTBEAT_RES.value);
        heartBeat.setHeader(header);
        return heartBeat;
    }
}
