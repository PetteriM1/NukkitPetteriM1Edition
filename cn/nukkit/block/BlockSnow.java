/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockSnow
extends BlockSolid {
    @Override
    public String getName() {
        return "Snow";
    }

    @Override
    public int getId() {
        return 80;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShovel() && item.getTier() >= 1) {
            if (item.hasEnchantment(16)) {
                return new Item[]{this.toItem()};
            }
            return new Item[]{Item.get(332, 0, 4)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SNOW_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.isShovel() && !Server.getInstance().suomiCraftPEMode() && (player == null || (player.gamemode & 2) == 0)) {
            item.useOn(this);
            this.level.useBreakOn(this, item.clone().clearNamedTag(), null, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

