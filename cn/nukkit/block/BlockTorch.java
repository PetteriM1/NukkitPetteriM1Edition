/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockTorch
extends BlockFlowable
implements Faceable {
    private static final short[] e = new short[]{0, 5, 4, 3, 2, 1};
    private static final short[] d = new short[]{0, 4, 5, 2, 3, 0, 0};

    public BlockTorch() {
        this(0);
    }

    public BlockTorch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public int getId() {
        return 50;
    }

    @Override
    public int getLightLevel() {
        return 14;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Block block = this.down();
            int n2 = this.getDamage();
            Block block2 = this.getSide(BlockFace.fromIndex(d[n2]));
            int n3 = block2.getId();
            if (block2.isTransparent() && (n2 != 0 || !(block instanceof BlockFence) && block.getId() != 139) && n3 != 20 && n3 != 241 && n3 != 254) {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        short s2 = e[blockFace.getIndex()];
        int n = this.getSide(BlockFace.fromIndex(d[s2])).getId();
        if (!(block2.isTransparent() && n != 20 && n != 241 && n != 254 || blockFace == BlockFace.DOWN)) {
            this.setDamage(s2);
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        Block block3 = this.down();
        if (!block3.isTransparent() || block3 instanceof BlockFence || block3.getId() == 139 || block3.getId() == 20 || block3.getId() == 241 || block3.getId() == 254) {
            this.setDamage(0);
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.getBlockFace(this.getDamage() & 7);
    }

    public BlockFace getBlockFace(int n) {
        switch (n) {
            case 1: {
                return BlockFace.EAST;
            }
            case 2: {
                return BlockFace.WEST;
            }
            case 3: {
                return BlockFace.SOUTH;
            }
            case 4: {
                return BlockFace.NORTH;
            }
        }
        return BlockFace.UP;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

