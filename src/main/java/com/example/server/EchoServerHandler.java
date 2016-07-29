package com.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yuanye on 2016/7/27.
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String body = (String) msg;
        log.info("第" + ++counter + "次接收客户端数据:[" + body + "]" + "==length:" + body.length());
       /* String respStr = "netty response.$";
        ByteBuf echo = Unpooled.copiedBuffer(respStr.getBytes());
        ctx.writeAndFlush(echo);*/
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
