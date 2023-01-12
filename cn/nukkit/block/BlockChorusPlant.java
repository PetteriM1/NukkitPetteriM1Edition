/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEndStone;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockChorusPlant
extends BlockTransparent {
    @Override
    public int getId() {
        return 240;
    }

    @Override
    public String getName() {
        return "Chorus Plant";
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 0.4;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = block.down();
        if (!(block3 instanceof BlockEndStone || block3 instanceof BlockChorusPlant || block.north() instanceof BlockChorusPlant || block.east() instanceof BlockChorusPlant || block.south() instanceof BlockChorusPlant || block.west() instanceof BlockChorusPlant)) {
            return false;
        }
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (!(n != 1 || !this.down().isTransparent() || this.down() instanceof BlockChorusPlant || this.north() instanceof BlockChorusPlant || this.east() instanceof BlockChorusPlant || this.south() instanceof BlockChorusPlant || this.west() instanceof BlockChorusPlant)) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        Item[] itemArray;
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        if (Utils.rand()) {
            Item[] itemArray2 = new Item[1];
            itemArray = itemArray2;
            itemArray2[0] = Item.get(432, 0, 1);
        } else {
            itemArray = new Item[]{};
        }
        return itemArray;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

