/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.Food;

public class FoodInBowl
extends Food {
    public FoodInBowl(int n, float f2) {
        this.setRestoreFood(n);
        this.setRestoreSaturation(f2);
    }

    @Override
    protected boolean onEatenBy(Player player) {
        super.onEatenBy(player);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), Item.get(281));
        return true;
    }
}

