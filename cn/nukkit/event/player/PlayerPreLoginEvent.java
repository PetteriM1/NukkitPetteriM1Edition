/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerPreLoginEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected String kickMessage;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerPreLoginEvent(Player player, String string) {
        this.player = player;
        this.kickMessage = string;
    }

    public void setKickMessage(String string) {
        this.kickMessage = string;
    }

    public String getKickMessage() {
        return this.kickMessage;
    }
}

