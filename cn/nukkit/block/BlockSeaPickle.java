/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public class BlockSeaPickle
extends BlockTransparentMeta {
    public BlockSeaPickle() {
        this(0);
    }

    public BlockSeaPickle(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Sea Pickle";
    }

    @Override
    public int getId() {
        return 411;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().isTransparent()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block2.getId() == 411 && (block2.getDamage() & 3) < 3) {
            block2.setDamage(block2.getDamage() + 1);
            this.getLevel().setBlock(block2, block2, true, true);
            return true;
        }
        if (!this.down().isTransparent()) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(411), 0);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(411), 0, (this.getDamage() & 3) + 1)};
    }

    @Override
    public int getLightLevel() {
        return (this.getDamage() + 1) * 3;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

