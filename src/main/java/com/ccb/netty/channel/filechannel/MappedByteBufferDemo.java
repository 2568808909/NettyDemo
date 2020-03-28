package com.ccb.netty.channel.filechannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;

/**
 * MappedByteBuffer 通过RandomAccessFile建立直接内存映射(mmap)，会在对外内存开辟一块内存空间，
 * 这块内存空间会与磁盘中的文件建立一个映射，在这块内存中的修改回反应到磁盘中的文件中，
 * 因此减少了一次数据从用户空间到内核空间的拷贝
 */
public class MappedByteBufferDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile=new RandomAccessFile("FileChannelTest.txt","rw");
        //NonReadableChannelException
        //FileOutputStream randomAccessFile=new FileOutputStream("FileChannelTest.txt");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(1,(byte) 'E');
        mappedByteBuffer.put(2,(byte) '6');
        channel.close();
        randomAccessFile.close();

        System.out.println("Finish");
    }
}
