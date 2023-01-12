/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.Food;

public class FoodMilk
extends Food {
    @Override
    protected boolean onEatenBy(Player player) {
        super.onEatenBy(player);
        player.getInventory().addItem(Item.get(325));
        player.removeAllEffects();
        return true;
    }
}

