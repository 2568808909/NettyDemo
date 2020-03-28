package com.ccb.netty.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ChatRoomClient implements Closeable, Runnable {

    private Selector selector;

    private SocketChannel socket;

    private ByteBuffer buffer;

    private final static int DEFAULT_PORT = 9999;

    private final static String DEFAULT_HOST = "localhost";

    public ChatRoomClient(String host, int port) throws IOException {
        this.selector = Selector.open();
        this.socket = SocketChannel.open();
        this.socket.configureBlocking(false);
        this.socket.connect(new InetSocketAddress("localhost", 9999));
        this.socket.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        this.buffer = ByteBuffer.allocate(1024);
        while (!socket.finishConnect()) ;
        System.out.println("连接完成");
    }

    public ChatRoomClient() throws IOException {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public void send(String msg) throws IOException {
        this.buffer.put(msg.getBytes());
        this.buffer.flip();
        socket.write(buffer);
        buffer.clear();
    }

    @Override
    public void close() throws IOException {
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void release() throws IOException {
        if (selector.isOpen()) {
            selector.close();
            socket.close();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println(socket.getLocalAddress() + "客户端已开启.....");
            while (true) {
                if (Thread.interrupted()) {
                    break;
                }
                if (selector.select() == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.read(buffer);
                        buffer.flip();
                        String msg = new String(buffer.array(), 0, buffer.limit());
                        System.out.println(msg);
                        buffer.clear();
                    }
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
