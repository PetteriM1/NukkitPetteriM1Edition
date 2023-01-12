/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.form.window.FormWindow;
import java.util.Map;

public class PlayerServerSettingsRequestEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private Map<Integer, FormWindow> b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerServerSettingsRequestEvent(Player player, Map<Integer, FormWindow> map) {
        this.player = player;
        this.b = map;
    }

    public Map<Integer, FormWindow> getSettings() {
        return this.b;
    }

    public void setSettings(Map<Integer, FormWindow> map) {
        this.b = map;
    }

    public void setSettings(int n, FormWindow formWindow) {
        this.b.put(n, formWindow);
    }
}

