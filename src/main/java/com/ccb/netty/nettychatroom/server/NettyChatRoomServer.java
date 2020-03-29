package com.ccb.netty.nettychatroom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatRoomServer {

    private int port;

    public NettyChatRoomServer(int port) {
        this.port = port;
    }

    public void run() {
        //1.创建两个线程组bossGroup和workerGroup
        //NioEventLoopGroup默认初始化会创建 核数*2个线程,可以通过传参改变
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //2.创建服务端启动对象，利用其设置配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup) //设置父线程组和子线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个测试通道对象
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new NettyChatRoomServerHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器

            //绑定一个端口并且同步，生成ChannelFuture对象
            //启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(7777).sync();
            System.out.println("服务端启动完毕......");
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyChatRoomServer(6666).run();
    }

}
