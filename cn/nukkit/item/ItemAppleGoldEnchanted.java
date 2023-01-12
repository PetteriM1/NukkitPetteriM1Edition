/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.ItemEdible;
import cn.nukkit.math.Vector3;

public class ItemAppleGoldEnchanted
extends ItemEdible {
    public ItemAppleGoldEnchanted() {
        this((Integer)0, 1);
    }

    public ItemAppleGoldEnchanted(Integer n) {
        this(n, 1);
    }

    public ItemAppleGoldEnchanted(Integer n, int n2) {
        super(466, n, n2, "Enchanted Apple");
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return true;
    }
}

