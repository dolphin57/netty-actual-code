package io.dolphin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Copyright 2017 - 2025 Evergrande Group
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            // 打开ServerSocketChannel通道
            serverChannel = ServerSocketChannel.open();
            // 绑定对应端口
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 设置为非阻塞模式
            serverChannel.configureBlocking(false);
            // 设置完后将channel注册到selector
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 打开一个监听器
            selector = Selector.open();

            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        // 进行轮询遍历
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;

                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) {

    }
}
