/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockPumpkin
extends BlockSolidMeta
implements Faceable {
    public BlockPumpkin() {
        this(0);
    }

    public BlockPumpkin(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Pumpkin";
    }

    @Override
    public int getId() {
        return 86;
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(player != null ? player.getDirection().getOpposite().getHorizontalIndex() : 0);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.isShears() && player != null && (player.isSurvival() || player.isCreative())) {
            item.useOn(this);
            this.getLevel().setBlock(this, Block.get(410, this.getDamage()), true, true);
            this.getLevel().dropItem(this.add(0.5, 0.5, 0.5), Item.get(361));
            return true;
        }
        return false;
    }
}

