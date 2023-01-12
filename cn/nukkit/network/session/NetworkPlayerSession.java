/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.session;

import cn.nukkit.Player;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.protocol.DataPacket;

public interface NetworkPlayerSession {
    public void sendPacket(DataPacket var1);

    public void sendImmediatePacket(DataPacket var1, Runnable var2);

    public void flush();

    public void disconnect(String var1);

    public Player getPlayer();

    public void setCompression(CompressionProvider var1);

    public CompressionProvider getCompression();
}

