package com.nukkitx.network.raknet.pipeline;

import cn.nukkit.Server;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import com.nukkitx.network.raknet.RakNet;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class RakExceptionHandler extends ChannelDuplexHandler {

    public static final String NAME = "rak-exception-handler";

    public RakExceptionHandler(RakNet rakNet) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Server.getInstance().getLogger().error("An exception occurred in RakNet", cause);
        ExceptionHandler.handleSilently(cause);
    }
}
