package com.example.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by yuanye on 2016/7/25.
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String body = (String) msg;
            log.info("接收数据:" + body + "---counter:" + ++counter);
            String currentTime = "query time".equalsIgnoreCase(body) ? new Date().toString() : "无此查询";
            currentTime += System.getProperty("line.separator");
            ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.write(resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.write("Error");
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
}
