/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockNetherWart
extends BlockFlowable {
    public BlockNetherWart() {
        this(0);
    }

    public BlockNetherWart(int n) {
        super(n);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        if (block3.getId() == 88) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.down().getId() == 88) return 0;
            this.getLevel().useBreakOn(this);
            return 1;
        }
        if (n != 2) return 0;
        if (Utils.random.nextInt(10) != 1) return 2;
        if (this.getDamage() >= 3) return 0;
        BlockNetherWart blockNetherWart = (BlockNetherWart)this.clone();
        blockNetherWart.setDamage(blockNetherWart.getDamage() + 1);
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockNetherWart);
        Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
        if (blockGrowEvent.isCancelled()) return 2;
        this.getLevel().setBlock(this, blockGrowEvent.getNewState(), true, true);
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public String getName() {
        return "Nether Wart Block";
    }

    @Override
    public int getId() {
        return 115;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() == 3) {
            return new Item[]{Item.get(372, 0, 2 + (int)(Math.random() * 3.0))};
        }
        return new Item[]{Item.get(372)};
    }

    @Override
    public Item toItem() {
        return Item.get(372);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

