/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindow;

public class PlayerFormRespondedEvent
extends PlayerEvent {
    private static final HandlerList b = new HandlerList();
    protected int formID;
    protected FormWindow window;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerFormRespondedEvent(Player player, int n, FormWindow formWindow) {
        this.player = player;
        this.formID = n;
        this.window = formWindow;
    }

    public int getFormID() {
        return this.formID;
    }

    public FormWindow getWindow() {
        return this.window;
    }

    public FormResponse getResponse() {
        return this.window.getResponse();
    }

    public boolean wasClosed() {
        return this.window.wasClosed();
    }
}

