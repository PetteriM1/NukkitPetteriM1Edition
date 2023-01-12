/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.ItemEdible;
import cn.nukkit.math.Vector3;

public class ItemChorusFruit
extends ItemEdible {
    public ItemChorusFruit() {
        this((Integer)0, 1);
    }

    public ItemChorusFruit(Integer n) {
        this(n, 1);
    }

    public ItemChorusFruit(Integer n, int n2) {
        super(432, 0, n2, "Chorus Fruit");
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return player.getServer().getTick() - player.getLastChorusFruitTeleport() >= 20;
    }

    @Override
    public boolean onUse(Player player, int n) {
        if (n < 15) {
            return false;
        }
        boolean bl = super.onUse(player, n);
        if (bl) {
            player.onChorusFruitTeleport();
        }
        return bl;
    }
}

