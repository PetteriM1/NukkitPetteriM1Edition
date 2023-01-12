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
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockEndPortalFrame
extends BlockTransparentMeta
implements Faceable {
    private static final int[] d = new int[]{2, 3, 0, 1};

    public BlockEndPortalFrame() {
        this(0);
    }

    public BlockEndPortalFrame(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 120;
    }

    @Override
    public double getResistance() {
        return 1.8E7;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "End Portal Frame";
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getMaxY() {
        return this.y + ((this.getDamage() & 4) > 0 ? 1.0 : 0.8125);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        return (this.getDamage() & 4) != 0 ? 15 : 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if ((this.getDamage() & 4) == 0 && player != null && item.getId() == 381 && !player.isSneaking()) {
            this.setDamage(this.getDamage() + 4);
            this.getLevel().setBlock(this, this, true, false);
            this.getLevel().addLevelSoundEvent(this, 173);
            for (int k = 0; k < 4; ++k) {
                for (int i2 = -1; i2 <= 1; ++i2) {
                    Block block = this.getSide(BlockFace.fromHorizontalIndex(k), 2).getSide(BlockFace.fromHorizontalIndex((k + 1) % 4), i2);
                    if (!BlockEndPortalFrame.a(block)) continue;
                    for (int i3 = -1; i3 <= 1; ++i3) {
                        for (int i4 = -1; i4 <= 1; ++i4) {
                            this.getLevel().setBlock(block.add(i3, 0.0, i4), Block.get(119), true);
                        }
                    }
                    this.getLevel().addLevelSoundEvent(this, 174);
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(d[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    private static boolean a(Block block) {
        for (int k = 0; k < 4; ++k) {
            for (int i2 = -1; i2 <= 1; ++i2) {
                Block block2 = block.getSide(BlockFace.fromHorizontalIndex(k), 2).getSide(BlockFace.fromHorizontalIndex((k + 1) % 4), i2);
                if (block2.getId() == 120 && (block2.getDamage() & 4) != 0) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GREEN_BLOCK_COLOR;
    }
}

