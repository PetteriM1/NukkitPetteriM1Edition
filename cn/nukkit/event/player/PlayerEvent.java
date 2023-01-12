/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Event;

public abstract class PlayerEvent
extends Event {
    protected Player player;

    public Player getPlayer() {
        return this.player;
    }
}

