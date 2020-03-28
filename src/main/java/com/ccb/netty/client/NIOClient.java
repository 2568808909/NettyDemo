package com.ccb.netty.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        System.out.println(socketChannel.connect(new InetSocketAddress("localhost", 9999)));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (!socketChannel.finishConnect());
        System.out.println("连接完成");
        while (true) {
            String msg = scanner.nextLine();
            buffer.put(msg.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            if ("exit".equals(msg)) {
                break;
            }
            buffer.clear();
        }
        scanner.close();
        socketChannel.close();
    }
}
