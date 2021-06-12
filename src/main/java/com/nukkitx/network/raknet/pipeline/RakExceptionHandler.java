package com.nukkitx.network.raknet.pipeline;

import cn.nukkit.Server;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class RakExceptionHandler extends ChannelDuplexHandler {

    public static final String NAME = "rak-exception-handler";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Server.getInstance().getLogger().error("An exception occurred in RakNet", cause);
    }
}
