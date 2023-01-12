/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.lang.TextContainer;

public class PlayerKickEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected TextContainer quitMessage;
    protected final Reason reason;
    protected final String reasonString;
    private final String c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerKickEvent(Player player, String string, String string2) {
        this(player, Reason.UNKNOWN, string, new TextContainer(string2));
    }

    public PlayerKickEvent(Player player, String string, TextContainer textContainer) {
        this(player, Reason.UNKNOWN, string, textContainer);
    }

    public PlayerKickEvent(Player player, Reason reason, TextContainer textContainer) {
        this(player, reason, reason.toString(), textContainer);
    }

    public PlayerKickEvent(Player player, Reason reason, String string) {
        this(player, reason, new TextContainer(string));
    }

    public PlayerKickEvent(Player player, Reason reason, String string, TextContainer textContainer) {
        this(player, reason, string, textContainer, "");
    }

    public PlayerKickEvent(Player player, Reason reason, String string, TextContainer textContainer, String string2) {
        this.player = player;
        this.quitMessage = textContainer;
        this.reason = reason;
        this.reasonString = reason.name();
        this.c = string2;
    }

    public String getReason() {
        return this.reasonString;
    }

    public Reason getReasonEnum() {
        return this.reason;
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

    public String getExtraData() {
        return this.c;
    }

    public static enum Reason {
        NEW_CONNECTION,
        KICKED_BY_ADMIN,
        NOT_WHITELISTED,
        IP_BANNED,
        NAME_BANNED,
        INVALID_PVE,
        INVALID_PVP,
        LOGIN_TIMEOUT,
        SERVER_FULL,
        FLYING_DISABLED,
        INVALID_PACKET,
        UNKNOWN;


        public String toString() {
            return this.name();
        }
    }
}

