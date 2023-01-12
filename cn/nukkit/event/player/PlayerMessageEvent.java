/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.event.player.PlayerEvent;

public abstract class PlayerMessageEvent
extends PlayerEvent {
    protected String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String string) {
        this.message = string;
    }
}

