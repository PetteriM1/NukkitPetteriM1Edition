/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;

public class ServerStopEvent
extends ServerEvent {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }
}

