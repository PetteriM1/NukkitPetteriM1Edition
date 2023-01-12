/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityMovingBlock
extends BlockEntitySpawnable {
    public Block block;
    public BlockVector3 piston;
    public int progress;

    public BlockEntityMovingBlock(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains("movingBlockData") && this.namedTag.contains("movingBlockId")) {
            this.block = Block.get(this.namedTag.getInt("movingBlockId"), this.namedTag.getInt("movingBlockData"));
        } else {
            this.close();
        }
        if (this.namedTag.contains("pistonPosX") && this.namedTag.contains("pistonPosY") && this.namedTag.contains("pistonPosZ")) {
            this.piston = new BlockVector3(this.namedTag.getInt("pistonPosX"), this.namedTag.getInt("pistonPosY"), this.namedTag.getInt("pistonPosZ"));
        } else {
            this.close();
        }
        super.initBlockEntity();
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isBlockEntityValid() {
        return true;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return BlockEntityMovingBlock.getDefaultCompound(this, "MovingBlock").putFloat("movingBlockId", this.block.getId()).putFloat("movingBlockData", this.block.getDamage()).putInt("pistonPosX", this.piston.x).putInt("pistonPosY", this.piston.y).putInt("pistonPosZ", this.piston.z);
    }
}

