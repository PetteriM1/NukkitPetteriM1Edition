/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.ItemEdible;
import cn.nukkit.math.Vector3;

public class ItemAppleGold
extends ItemEdible {
    public ItemAppleGold() {
        this((Integer)0, 1);
    }

    public ItemAppleGold(Integer n) {
        this(n, 1);
    }

    public ItemAppleGold(Integer n, int n2) {
        super(322, n, n2, "Golden Apple");
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return true;
    }
}

