package com.example.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yuanye on 2016/7/27.
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private int counter;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String ECHO_REQ = "netty request.$";
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("第" + ++counter + "次接收服务器返回数据:[" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
