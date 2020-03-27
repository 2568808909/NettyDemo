package com.ccb.netty.channel.filechannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class OutputFileChannel {

    public static void main(String[] args) throws IOException {
        String string = "我只是要测试一下FileChannel";
        FileOutputStream outputStream = new FileOutputStream("FileChannelTest.txt");
        //通过FileOutputStream获取FileChannel
        FileChannel channel = outputStream.getChannel();
        //给ByteBuffer分配1024字节的空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将字符串放入ByteBuffer中
        byteBuffer.put(string.getBytes());
        //读取ByteBuffer前反转一下 limit=position,position=0
        byteBuffer.flip();
        //将数据通过FileChannel写出到文件
        channel.write(byteBuffer);
        channel.close();
    }
}
