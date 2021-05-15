package com.nukkitx.network.raknet.pipeline;

import com.nukkitx.network.raknet.RakNetServer;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.raknet.RakNetUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import static com.nukkitx.network.raknet.RakNetConstants.*;

@ChannelHandler.Sharable
public class ServerDatagramHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    public static final String NAME = "rak-server-datagram-handler";
    private final RakNetServer server;

    public ServerDatagramHandler(RakNetServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf buffer = packet.content();
        short packetId = buffer.readByte();

        // These packets don't require a session
        switch (packetId) {
            case ID_UNCONNECTED_PING:
                this.onUnconnectedPing(ctx, packet);
                return;
            case ID_OPEN_CONNECTION_REQUEST_1:
                this.server.onOpenConnectionRequest1(ctx, packet);
                return;
        }

        buffer.readerIndex(0);

        RakNetServerSession session = this.server.getSession(packet.sender());
        if (session != null) {
            if (session.getEventLoop().inEventLoop()) {
                session.onDatagram(buffer.retain());
            } else {
                ByteBuf buf = buffer.retain();
                session.getEventLoop().execute(() -> session.onDatagram(buf));
            }
        }

        if (this.server.getListener() != null) {
            this.server.getListener().onUnhandledDatagram(ctx, packet);
        }
    }

    private void onUnconnectedPing(ChannelHandlerContext ctx, DatagramPacket packet) {
        if (!packet.content().isReadable(24)) {
            return;
        }

        long pingTime = packet.content().readLong();
        if (!RakNetUtils.verifyUnconnectedMagic(packet.content())) {
            return;
        }

        byte[] userData = null;
        if (this.server.getListener() != null) {
            userData = this.server.getListener().onQuery(packet.sender());
        }

        if (userData == null) {
            userData = new byte[0];
        }

        int packetLength = 35 + userData.length;

        ByteBuf buffer = ctx.alloc().ioBuffer(packetLength, packetLength);
        buffer.writeByte(ID_UNCONNECTED_PONG);
        buffer.writeLong(pingTime);
        buffer.writeLong(this.server.getGuid());
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeShort(userData.length);
        buffer.writeBytes(userData);
        ctx.writeAndFlush(new DatagramPacket(buffer, packet.sender()));
    }
}
