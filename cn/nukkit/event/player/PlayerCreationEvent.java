/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.network.SourceInterface;
import java.net.InetSocketAddress;

public class PlayerCreationEvent
extends Event {
    private static final HandlerList f = new HandlerList();
    private final SourceInterface b;
    private final Long d;
    private final InetSocketAddress e;
    private Class<? extends Player> c;
    private Class<? extends Player> g;

    public static HandlerList getHandlers() {
        return f;
    }

    public PlayerCreationEvent(SourceInterface sourceInterface, Class<? extends Player> clazz, Class<? extends Player> clazz2, Long l, InetSocketAddress inetSocketAddress) {
        this.b = sourceInterface;
        this.d = l;
        this.e = inetSocketAddress;
        this.c = clazz;
        this.g = clazz2;
    }

    public SourceInterface getInterface() {
        return this.b;
    }

    public String getAddress() {
        return this.e.getAddress().toString();
    }

    public int getPort() {
        return this.e.getPort();
    }

    public InetSocketAddress getSocketAddress() {
        return this.e;
    }

    public Long getClientId() {
        return this.d;
    }

    public Class<? extends Player> getBaseClass() {
        return this.c;
    }

    public void setBaseClass(Class<? extends Player> clazz) {
        this.c = clazz;
    }

    public Class<? extends Player> getPlayerClass() {
        return this.g;
    }

    public void setPlayerClass(Class<? extends Player> clazz) {
        this.g = clazz;
    }
}

