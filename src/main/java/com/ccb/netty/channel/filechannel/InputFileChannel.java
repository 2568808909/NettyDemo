package com.ccb.netty.channel.filechannel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class InputFileChannel {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("FileChannelTest.txt");
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
        channel.close();
    }
}
