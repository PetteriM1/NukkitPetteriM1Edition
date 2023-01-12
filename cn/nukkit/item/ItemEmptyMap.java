/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class ItemEmptyMap
extends Item {
    public ItemEmptyMap() {
        this((Integer)0, 1);
    }

    public ItemEmptyMap(Integer n) {
        this(n, 1);
    }

    public ItemEmptyMap(Integer n, int n2) {
        super(395, n, n2, "Empty Map");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (!player.isCreative()) {
            --this.count;
        }
        player.getInventory().addItem(Item.get(358));
        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        if (!player.isCreative()) {
            --this.count;
        }
        player.getInventory().addItem(Item.get(358));
        return true;
    }
}

