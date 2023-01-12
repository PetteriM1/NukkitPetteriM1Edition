/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.food.Food;

public class PlayerEatFoodEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private Food b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerEatFoodEvent(Player player, Food food) {
        this.player = player;
        this.b = food;
    }

    public Food getFood() {
        return this.b;
    }

    public void setFood(Food food) {
        this.b = food;
    }
}

