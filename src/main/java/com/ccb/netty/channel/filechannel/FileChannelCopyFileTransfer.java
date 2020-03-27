package com.ccb.netty.channel.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelCopyFileTransfer {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("FileChannelTest.txt");
        FileOutputStream outputStream = new FileOutputStream("CopyFile_Transfer.txt");
        FileChannel sourceChannel = inputStream.getChannel();
        FileChannel targetChannel = outputStream.getChannel();
        targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    }
}
