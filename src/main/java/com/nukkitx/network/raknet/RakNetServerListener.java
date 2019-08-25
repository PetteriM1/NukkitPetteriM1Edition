package com.nukkitx.network.raknet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;

@ParametersAreNonnullByDefault
public interface RakNetServerListener {

    /**
     * @param address address of user requesting connection
     * @return whether the user should be accepted or not
     */
    boolean onConnectionRequest(InetSocketAddress address);

    /**
     * Called when an unconnected client pings the server to retrieve it's status and MOTD.
     *
     * @param address address of client pinging the server
     * @return custom user data sent back to the client
     */
    @Nullable
    byte[] onQuery(InetSocketAddress address);

    /**
     * Called when a session is established. This does not mean the session has fully connected but has started the
     * RakNet connection sequence. To find out when the session has finished connecting, use
     * {@link RakNetSessionListener}
     *
     * @param session session created
     */
    void onSessionCreation(RakNetServerSession session);

    /**
     * Called when an unknown session-less datagram is received which is not related to RakNet
     *
     * @param ctx channel context of datagram
     * @param packet unhandled datagram
     */
    void onUnhandledDatagram(ChannelHandlerContext ctx, DatagramPacket packet);
}
