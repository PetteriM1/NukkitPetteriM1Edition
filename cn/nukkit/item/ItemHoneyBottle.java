/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemEdible;
import cn.nukkit.math.Vector3;

public class ItemHoneyBottle
extends ItemEdible {
    public ItemHoneyBottle() {
        this((Integer)0, 1);
    }

    public ItemHoneyBottle(Integer n) {
        this(n, 1);
    }

    public ItemHoneyBottle(Integer n, int n2) {
        super(737, n, n2, "Honey Bottle");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return true;
    }

    @Override
    public boolean onUse(Player player, int n) {
        if (n < 15) {
            return false;
        }
        super.onUse(player, n);
        if (player.hasEffect(19)) {
            player.removeEffect(19);
        }
        if (!player.isCreative()) {
            --this.count;
            player.getInventory().setItemInHand(this);
            player.getInventory().addItem(Item.get(374, 0, 1));
        }
        return true;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 389;
    }
}

