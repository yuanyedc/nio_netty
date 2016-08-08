package com.example.priproto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuanye on 2016/7/29.
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Object> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
        log.info("接收请求数据:" + message);
        log.info("message type:"+message.getHeader().getType());
        if (message != null && message.getHeader().getType() == MessageType.HAND_REQ.value) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            log.info("=========nodeIndex:"+nodeIndex);
            NettyMessage loginResp = null;
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                log.info("=====ip:"+ip);
                boolean isOk = false;
                for (String WIP : whiteList) {
                    if (WIP.equals(ip)) {
                        isOk = true;
                        break;
                    }
                }
                loginResp = isOk ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOk) {
                    nodeCheck.put(nodeIndex, true);
                }
            }
            log.info("握手响应：" + loginResp);
            ctx.writeAndFlush(loginResp);
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
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte) MessageType.HAND_RES.value);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
