/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.lang.TextContainer;

public class PlayerQuitEvent
extends PlayerEvent {
    private static final HandlerList b = new HandlerList();
    protected TextContainer quitMessage;
    protected boolean autoSave;
    protected String reason;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerQuitEvent(Player player, TextContainer textContainer, String string) {
        this(player, textContainer, true, string);
    }

    public PlayerQuitEvent(Player player, TextContainer textContainer) {
        this(player, textContainer, true);
    }

    public PlayerQuitEvent(Player player, String string, String string2) {
        this(player, string, true, string2);
    }

    public PlayerQuitEvent(Player player, String string) {
        this(player, string, true);
    }

    public PlayerQuitEvent(Player player, String string, boolean bl, String string2) {
        this(player, new TextContainer(string), bl, string2);
    }

    public PlayerQuitEvent(Player player, String string, boolean bl) {
        this(player, new TextContainer(string), bl);
    }

    public PlayerQuitEvent(Player player, TextContainer textContainer, boolean bl) {
        this(player, textContainer, bl, "No reason");
    }

    public PlayerQuitEvent(Player player, TextContainer textContainer, boolean bl, String string) {
        this.player = player;
        this.quitMessage = textContainer;
        this.autoSave = bl;
        this.reason = string;
    }

    public TextContainer getQuitMessage() {
        return this.quitMessage;
    }

    public void setQuitMessage(TextContainer textContainer) {
        this.quitMessage = textContainer;
    }

    public void setQuitMessage(String string) {
        this.setQuitMessage(new TextContainer(string));
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave() {
        this.setAutoSave(true);
    }

    public void setAutoSave(boolean bl) {
        this.autoSave = bl;
    }

    public String getReason() {
        return this.reason;
    }
}

