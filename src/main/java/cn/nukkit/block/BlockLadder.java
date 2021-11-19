package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemLadder;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

/**
 * Created on 2015/12/8 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockLadder extends BlockTransparentMeta implements Faceable {

    private static final int[] faces = {
            0, //never use
            1, //never use
            3,
            2,
            5,
            4
    };

    public BlockLadder() {
        this(0);
    }

    public BlockLadder(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Ladder";
    }

    @Override
    public int getId() {
        return LADDER;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 2;
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    public boolean sticksToPiston() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {

        double f = 0.1875;

        if (this.getDamage() == 2) {
            return new SimpleAxisAlignedBB(
                    this.x,
                    this.y,
                    this.z + 1 - f,
                    this.x + 1,
                    this.y + 1,
                    this.z + 1
            );
        } else if (this.getDamage() == 3) {
            return new SimpleAxisAlignedBB(
                    this.x,
                    this.y,
                    this.z,
                    this.x + 1,
                    this.y + 1,
                    this.z + f
            );
        } else if (this.getDamage() == 4) {
            return new SimpleAxisAlignedBB(
                    this.x + 1 - f,
                    this.y,
                    this.z,
                    this.x + 1,
                    this.y + 1,
                    this.z + 1
            );
        } else if (this.getDamage() == 5) {
            return new SimpleAxisAlignedBB(
                    this.x,
                    this.y,
                    this.z,
                    this.x + f,
                    this.y + 1,
                    this.z + 1
            );
        }
        return null;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return super.recalculateBoundingBox();
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (!target.isTransparent()) {
            if (face.getIndex() >= 2 && face.getIndex() <= 5) {
                this.setDamage(face.getIndex());
                this.getLevel().setBlock(block, this, true, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!this.getSide(BlockFace.fromIndex(faces[this.getDamage()])).isSolid()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{
            new ItemLadder(0, 1)
        };
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
