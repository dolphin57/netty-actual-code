/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.starter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: qianliang
 * @Since: 2018/10/8 9:16
 */
public class TimeClient {
    public void connect(int port, String host) throws InterruptedException {
        // 配置客户端的NIO线程组(客户端处理IO读写)
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 辅助启动类,并对其进行配置
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端连接关闭
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        new TimeClient().connect(port, "127.0.0.1");
    }
}
