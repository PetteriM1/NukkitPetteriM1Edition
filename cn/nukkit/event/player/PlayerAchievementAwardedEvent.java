/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerAchievementAwardedEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final String achievement;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerAchievementAwardedEvent(Player player, String string) {
        this.player = player;
        this.achievement = string;
    }

    public String getAchievement() {
        return this.achievement;
    }
}

