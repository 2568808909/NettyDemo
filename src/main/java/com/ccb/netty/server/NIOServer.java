package com.ccb.netty.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));
        server.configureBlocking(false);
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select() == 0) {
                System.out.println("没有时间产生继续select");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    System.out.println("接收到客户端的连接请求");
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("连接完成");
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    buffer.flip();
                    //读取数据时，不应该就只用buffer.array()读取，可以的话还是使用buffer.hasRemaining(),
                    // 然后读取，因为buffer.clear方法并不会真正清除数据，只是将position=0,limit=capacity而已，
                    // 这样读取数据会读出上次的数据，所以应该flip一下，按数据长度来读取
                    String msg = new String(buffer.array(), 0, buffer.limit());
                    System.out.println("从客户端中获取到数据: " + msg);
                    buffer.clear();
                    if ("exit".equals(msg)) {
                        System.out.println("客户端断开连接");
                        key.cancel();
                        channel.close();
                        System.out.println("key 的数量" + +selector.keys().size());
                    }
                }
            }
            selectionKeys.clear();
        }
    }
}
