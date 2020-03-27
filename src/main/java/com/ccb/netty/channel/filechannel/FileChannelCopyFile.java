package com.ccb.netty.channel.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopyFile {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("FileChannelTest.txt");
        FileOutputStream outputStream = new FileOutputStream("CopyFile.txt");
        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        while (true) {
            int read = inputChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            outputChannel.write(byteBuffer);
            //每次操作完后清空一下ByteBuffer中的数据，主要是要将position重新置为0，
            // 不然下次read的时候发现ByteBuffer满了就没有办法读入数据，read一直都是0，就会陷入死循环
            byteBuffer.clear();
        }
        inputChannel.close();
        outputChannel.close();
        inputChannel.close();
        outputChannel.close();
    }
}
