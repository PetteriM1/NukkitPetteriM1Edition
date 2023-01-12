/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerFoodLevelChangeEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected int foodLevel;
    protected float foodSaturationLevel;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerFoodLevelChangeEvent(Player player, int n, float f2) {
        this.player = player;
        this.foodLevel = n;
        this.foodSaturationLevel = f2;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public void setFoodLevel(int n) {
        this.foodLevel = n;
    }

    public float getFoodSaturationLevel() {
        return this.foodSaturationLevel;
    }

    public void setFoodSaturationLevel(float f2) {
        this.foodSaturationLevel = f2;
    }
}

