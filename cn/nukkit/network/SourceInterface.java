/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.session.NetworkPlayerSession;
import java.net.InetSocketAddress;

public interface SourceInterface {
    default public Integer putPacket(Player player, DataPacket dataPacket) {
        player.getNetworkSession().sendPacket(dataPacket);
        return null;
    }

    default public Integer putPacket(Player player, DataPacket dataPacket, boolean bl) {
        player.getNetworkSession().sendPacket(dataPacket);
        return null;
    }

    default public Integer putPacket(Player player, DataPacket dataPacket, boolean bl, boolean bl2) {
        player.getNetworkSession().sendPacket(dataPacket);
        return null;
    }

    public NetworkPlayerSession getSession(InetSocketAddress var1);

    public int getNetworkLatency(Player var1);

    public void close(Player var1);

    public void close(Player var1, String var2);

    public void setName(String var1);

    public boolean process();

    public void shutdown();

    public void emergencyShutdown();
}

