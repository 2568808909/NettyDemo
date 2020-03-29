package com.ccb.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler，这个Handler要继承自netty规定好的某个ChannelAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送来的数据
     *
     * @param ctx 上下文对象，可以获取很多信息如，pipeline，channel等
     * @param msg 客户端发送来的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转成一个ByteBuf，这个对象是由netty提供的
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送来的信息为 " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址 " + ctx.channel().remoteAddress());
    }

    /**
     * 读取完成后触发的操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush = write +flush 将数据写入缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));
    }

    /**
     * 出现异常后触发
     * @param ctx   上下文对象
     * @param cause 异常对象
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
