/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.network.Network;
import cn.nukkit.network.SourceInterface;
import io.netty.buffer.ByteBuf;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface AdvancedSourceInterface
extends SourceInterface {
    public void blockAddress(InetAddress var1);

    public void blockAddress(InetAddress var1, int var2);

    public void unblockAddress(InetAddress var1);

    public void setNetwork(Network var1);

    public void sendRawPacket(InetSocketAddress var1, ByteBuf var2);
}

