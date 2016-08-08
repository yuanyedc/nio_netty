package com.example.priproto;

import com.example.client.EchoClientHandler;
import com.example.client.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuanye on 2016/8/1.
 */
@Slf4j
public class NettyClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            socketChannel.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            socketChannel.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            socketChannel.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(
                    new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCALIP,
                            NettyConstant.LOCAL_PORT)).sync();
            // 当对应的channel关闭的时候，就会返回对应的channel。
            // Returns the ChannelFuture which will be notified when this channel is closed. This method always returns the same future instance.
            future.channel().closeFuture().sync();
        } finally {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        try {
                            connect(NettyConstant.REMOTEIP, NettyConstant.PORT);// 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        try {
            new NettyClient().connect(NettyConstant.REMOTEIP, NettyConstant.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
