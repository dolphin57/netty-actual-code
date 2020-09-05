/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.starter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: qianliang
 * @Since: 2018/10/8 8:16
 */
public class TimeServer {
    public void bind(int port) throws InterruptedException {
        // 配置服务端的NIO线程组(主要是处理线程,用于处理接收请求和IO处理)
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            // parentGroup接收accept请求,childGroup处理各个连接的请求
            b.group(bossGroup, workerGroup)
                    // 设置ServerBootStrap的ChannelFactory,
                    .channel(NioServerSocketChannel.class)
                    // 设置通道参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 处理网络IO事件:日志或编码
                    .childHandler(new ChildChannelHandler());
            // 绑定端口,等待同步成功
            ChannelFuture f = b.bind(port).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 最终释放掉线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        new TimeServer().bind(port);
    }
}
