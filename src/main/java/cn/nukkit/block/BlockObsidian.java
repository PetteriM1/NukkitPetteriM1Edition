package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/12/2 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockObsidian extends BlockSolid {

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.OBSIDIAN_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_DIAMOND) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public double getHardness() {
        return 50; //35
    }

    @Override
    public int getId() {
        return OBSIDIAN;
    }

    @Override
    public String getName() {
        return "Obsidian";
    }

    @Override
    public double getResistance() {
        return 6000;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean onBreak(Item item) {
        //destroy the nether portal
        Block[] nearby = {
                this.up(), this.down(),
                this.north(), south(),
                this.west(), this.east(),
        };
        for (Block aNearby : nearby) {
            if (aNearby != null && aNearby.getId() == NETHER_PORTAL) {
                aNearby.onBreak(item);
            }
        }
        return super.onBreak(item);
    }
}
