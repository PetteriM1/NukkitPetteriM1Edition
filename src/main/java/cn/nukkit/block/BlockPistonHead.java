package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

/**
 * @author CreeperFace
 */
public class BlockPistonHead extends BlockTransparentMeta implements Faceable {

    public BlockPistonHead() {
        this(0);
    }

    public BlockPistonHead(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PISTON_HEAD;
    }

    @Override
    public String getName() {
        return "Piston Head";
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(BlockID.AIR), true, true);
        Block piston = getSide(getBlockFace().getOpposite());
        if (piston instanceof BlockPistonBase && ((BlockPistonBase) piston).getFacing() == this.getBlockFace()) {
            piston.onBreak(item);
        }
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        BlockFace face = BlockFace.fromIndex(this.getDamage()).getOpposite();
        if (face == BlockFace.UP) return BlockFace.DOWN;
        if (face == BlockFace.DOWN) return BlockFace.UP;
        return face;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(BlockID.AIR));
    }
}
