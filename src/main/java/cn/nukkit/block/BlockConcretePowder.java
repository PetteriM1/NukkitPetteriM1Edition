package cn.nukkit.block;

import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

/**
 * Created by CreeperFace on 2.6.2017.
 */
public class BlockConcretePowder extends BlockFallable {
    private int meta;

    public BlockConcretePowder() {
        this(0);
    }

    public BlockConcretePowder(int meta) {
        this.meta = meta;
    }

    @Override
    public final int getDamage() {
        return this.meta;
    }

    @Override
    public final void setDamage(int meta) {
        this.meta = meta;
    }

    @Override
    public int getFullId() {
        return (getId() << 4) + getDamage();
    }

    @Override
    public int getId() {
        return CONCRETE_POWDER;
    }

    @Override
    public String getName() {
        return "Concrete Powder";
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
        return ItemTool.TYPE_SHOVEL;
    }
    
    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            for (int side = 1; side < 6; ++side) {
                Block blockSide = this.getSide(BlockFace.fromIndex(side));
                if (blockSide instanceof BlockWater || blockSide instanceof BlockLava || blockSide instanceof BlockWaterStill || blockSide instanceof BlockLavaStill) {
                    this.getLevel().setBlock(this, Block.get(Block.CONCRETE, this.meta), true, true);
                }
            }
            
            if (this.down().getId() == AIR) {
                this.getLevel().setBlock(this, Block.get(Block.AIR), true, true);
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", this.x + 0.5))
                                .add(new DoubleTag("", this.y))
                                .add(new DoubleTag("", this.z + 0.5)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0)))

                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", 0))
                                .add(new FloatTag("", 0)))
                        .putInt("TileID", this.getId())
                        .putByte("Data", this.getDamage());

                EntityFallingBlock fall = new EntityFallingBlock(this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);

                fall.spawnToAll();
            }

            return Level.BLOCK_UPDATE_NORMAL;
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            for (int side = 1; side < 6; ++side) {
                Block blockSide = this.getSide(BlockFace.fromIndex(side));
                if (blockSide instanceof BlockWater || blockSide instanceof BlockLava || blockSide instanceof BlockWaterStill || blockSide instanceof BlockLavaStill) {
                    this.getLevel().setBlock(this, Block.get(Block.CONCRETE, this.meta), true, true);
                }
            }
            
            return Level.BLOCK_UPDATE_SCHEDULED;
        }

        return 0;
    }
}
