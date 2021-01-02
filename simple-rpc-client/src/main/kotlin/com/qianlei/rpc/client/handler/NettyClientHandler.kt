package com.qianlei.rpc.client.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class NettyClientHandler : ChannelInboundHandlerAdapter() {
    private lateinit var context: ChannelHandlerContext
    private lateinit var result: String

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.close()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        this.context = ctx
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        result = msg.toString()
    }
}