/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.plugin;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.plugin.Plugin;

public class PluginEvent
extends Event {
    private static final HandlerList c = new HandlerList();
    private final Plugin b;

    public PluginEvent(Plugin plugin) {
        this.b = plugin;
    }

    public static HandlerList getHandlers() {
        return c;
    }

    public Plugin getPlugin() {
        return this.b;
    }
}

