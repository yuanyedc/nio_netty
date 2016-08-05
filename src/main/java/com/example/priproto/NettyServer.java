package com.example.priproto;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yuanye on 2016/8/1.
 */
@Slf4j
public class NettyServer {
    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyMessageDecoder(1024, 4, 4));
                        socketChannel.pipeline().addLast(new NettyMessageEncoder());
                        socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                        socketChannel.pipeline().addLast(new LoginAuthRespHandler());
                        socketChannel.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                    }
                });
        serverBootstrap.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        log.info("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
    }

    public static void main(String[] args) {
        try {
            new NettyServer().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
