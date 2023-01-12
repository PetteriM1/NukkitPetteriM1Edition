/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;
import cn.nukkit.network.protocol.DataPacket;

public class DataPacketSendEvent
extends ServerEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final DataPacket c;
    private final Player b;

    public static HandlerList getHandlers() {
        return d;
    }

    public DataPacketSendEvent(Player player, DataPacket dataPacket) {
        this.c = dataPacket;
        this.b = player;
    }

    public Player getPlayer() {
        return this.b;
    }

    public DataPacket getPacket() {
        return this.c;
    }
}

