/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;
import cn.nukkit.network.protocol.DataPacket;

public class BatchPacketsEvent
extends ServerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Player[] e;
    private final DataPacket[] c;
    private final boolean d;

    public static HandlerList getHandlers() {
        return b;
    }

    public BatchPacketsEvent(Player[] playerArray, DataPacket[] dataPacketArray, boolean bl) {
        this.e = playerArray;
        this.c = dataPacketArray;
        this.d = bl;
    }

    public Player[] getPlayers() {
        return this.e;
    }

    public DataPacket[] getPackets() {
        return this.c;
    }

    public boolean isForceSync() {
        return this.d;
    }
}

