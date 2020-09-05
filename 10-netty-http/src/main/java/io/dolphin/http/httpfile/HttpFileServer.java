/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.http.httpfile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: qianliang
 * @Since: 2018/10/11 19:18
 */
public class HttpFileServer {
    private static final String DEFAULT_URL = "/chapter10/src/main/java/com/eric/chapter10/";

    public void run(final int port, final String url) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // HTTP 请求消息解码器
                            socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            // 将多个消息转换为单一的FullHttpRequest或FullHttpResponse
                            socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            // HTTP 响应消息编码器
                            socketChannel.pipeline().addLast("http-encoder", new HttpRequestEncoder());
                            // Chunk handler,支持异步大的码流
                            socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            // 文件服务器的业务逻辑处理
                            socketChannel.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture f = b.bind("172.24.11.130", port).sync();
            System.out.println("HTTP 文件目录服务器启动,网址是: http://192.168.0.102");
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        String url = DEFAULT_URL;
        if (args.length > 1) {
            url = args[1];
        }

        new HttpFileServer().run(port, url);
    }
}
