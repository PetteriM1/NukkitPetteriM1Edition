/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.lang.TextContainer;

public class PlayerJoinEvent
extends PlayerEvent {
    private static final HandlerList b = new HandlerList();
    protected TextContainer joinMessage;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerJoinEvent(Player player, TextContainer textContainer) {
        this.player = player;
        this.joinMessage = textContainer;
    }

    public PlayerJoinEvent(Player player, String string) {
        this.player = player;
        this.joinMessage = new TextContainer(string);
    }

    public TextContainer getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(TextContainer textContainer) {
        this.joinMessage = textContainer;
    }

    public void setJoinMessage(String string) {
        this.setJoinMessage(new TextContainer(string));
    }
}

