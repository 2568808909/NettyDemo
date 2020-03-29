package com.ccb.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyBossServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //即使BossGroup中有多个EventLoop，多次连接时间都是使用同一个EventLoop，并不会想WorkerGroup那样调用next
        System.out.println(Thread.currentThread().getName()+" server ="+ctx.channel());
        super.channelRead(ctx, msg);
    }
}
