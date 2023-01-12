/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class BlockDirt
extends BlockSolidMeta {
    public BlockDirt() {
        this(0);
    }

    public BlockDirt(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public String getName() {
        return this.getDamage() == 0 ? "Dirt" : "Coarse Dirt";
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (item.isHoe()) {
            Block block2 = this.up();
            if (block2 instanceof BlockAir || block2 instanceof BlockFlowable) {
                item.useOn(this);
                this.getLevel().setBlock(this, this.getDamage() == 0 ? BlockDirt.get(60) : BlockDirt.get(3), true);
                if (player != null) {
                    player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
                }
                return true;
            }
        } else if (item.isShovel() && ((block = this.up()) instanceof BlockAir || block instanceof BlockFlowable)) {
            item.useOn(this);
            this.getLevel().setBlock(this, Block.get(198));
            if (player != null) {
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        int n = this.getDamage() & 1;
        return new Item[]{new ItemBlock(Block.get(3, n), n)};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

