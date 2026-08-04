package com.nukkitx.network.raknet.pipeline;

import com.nukkitx.network.raknet.RakNet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class RakOutboundHandler extends ChannelOutboundHandlerAdapter {

    public static final String NAME = "rak-outbound-handler";

    public RakOutboundHandler(RakNet rakNet) {
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }
}
