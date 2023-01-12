/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.Vector3;

public class ItemFishingRod
extends ItemTool {
    public ItemFishingRod() {
        this((Integer)0, 1);
    }

    public ItemFishingRod(Integer n) {
        this(n, 1);
    }

    public ItemFishingRod(Integer n, int n2) {
        super(346, n, n2, "Fishing Rod");
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        if (player.fishing != null) {
            player.stopFishing(true);
        } else {
            player.startFishing(this);
            ++this.meta;
        }
        return true;
    }

    @Override
    public int getMaxDurability() {
        return 65;
    }

    @Override
    public boolean noDamageOnAttack() {
        return true;
    }

    @Override
    public boolean noDamageOnBreak() {
        return true;
    }
}

