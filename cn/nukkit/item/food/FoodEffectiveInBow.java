/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.FoodEffective;

public class FoodEffectiveInBow
extends FoodEffective {
    public FoodEffectiveInBow(int n, float f2) {
        super(n, f2);
    }

    @Override
    protected boolean onEatenBy(Player player) {
        super.onEatenBy(player);
        player.getInventory().addItem(Item.get(281));
        return true;
    }
}

