package com.ccb.netty.nettychatroom.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyChatRoomServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String msg = String.format("[客户端/%s] 上线", channel.remoteAddress());
        channelGroup.writeAndFlush(msg);
        channelGroup.add(channel);
        System.out.println(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String msg = String.format("[客户端/%s] 离线", channel.remoteAddress());
        channelGroup.writeAndFlush(msg);
        System.out.println("size of channelGroup: " + channelGroup.size());
        System.out.println(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        String toOther = String.format("[客户端/%s] : %s", channel.remoteAddress(), msg);
        System.out.println(toOther);
        String toSelf = String.format("[我/%s] : %s", channel.remoteAddress(), msg);
        for (Channel c : channelGroup) {
            c.writeAndFlush(c == channel ? toSelf : toOther);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
