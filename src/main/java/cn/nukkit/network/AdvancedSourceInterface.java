package cn.nukkit.network;

import io.netty.buffer.ByteBuf;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface AdvancedSourceInterface extends SourceInterface {

    void blockAddress(InetAddress address);

    void blockAddress(InetAddress address, int timeout);

    void sendRawPacket(InetSocketAddress socketAddress, ByteBuf payload);

    void setNetwork(Network network);

    void unblockAddress(InetAddress address);
}
