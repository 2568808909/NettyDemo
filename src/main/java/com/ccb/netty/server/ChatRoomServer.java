package com.ccb.netty.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatRoomServer implements Runnable, Closeable {

    private static final int DEFAULT_PORT = 9999;

    private Selector selector;

    private ServerSocketChannel server;

    public ChatRoomServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public ChatRoomServer(int port) throws IOException {
        this.selector = Selector.open();
        this.server = ServerSocketChannel.open();
        this.server.bind(new InetSocketAddress(port));
        this.server.configureBlocking(false);
        this.server.register(selector, SelectionKey.OP_ACCEPT);
    }


    @Override
    public void run() {
        System.out.println("服务端已开启.....");
        try {
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
                    if (key.isAcceptable()) {
                        SocketChannel socket = server.accept();
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        System.out.println(socket.getRemoteAddress() + "上线");
                        //TODO 通知其他客户端有人上线
                    }
                    if (key.isReadable()) {
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        SocketChannel channel = (SocketChannel) key.channel();
                        String address = channel.getRemoteAddress().toString();
                        channel.read(buffer);
                        buffer.flip();
                        String msg = new String(buffer.array(), 0, buffer.limit());
                        if ("exit".equals(msg)) {
                            System.out.println(address + "断开连接");
                            key.cancel();
                            channel.close();
                        } else {
                            msg = "message from " + address + " " + msg;
                            System.out.println(msg);
                            buffer.clear();
                            send(key, msg);
                        }
                        //TODO 转发消息给其他客户客户端
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

    private void send(SelectionKey self, String msg) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel && !key.equals(self)) {
                SocketChannel socket = (SocketChannel) channel;
                System.out.println("发送给 " + socket.getRemoteAddress());
                socket.write(buffer);
                //如果要用同一个buffer的数据多次发送，每次write完之后都要对position置0
                buffer.position(0);
            }
        }
    }

    private void release() throws IOException {
        selector.close();
        server.close();
    }

    @Override
    public void close() throws IOException {
        Thread.currentThread().interrupt();
        selector.wakeup();
    }
}
