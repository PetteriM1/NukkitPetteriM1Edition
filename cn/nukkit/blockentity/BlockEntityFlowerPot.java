/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityFlowerPot
extends BlockEntitySpawnable {
    public BlockEntityFlowerPot(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("item")) {
            this.namedTag.putShort("item", 0);
        }
        if (!this.namedTag.contains("data")) {
            if (this.namedTag.contains("mData")) {
                this.namedTag.putInt("data", this.namedTag.getInt("mData"));
                this.namedTag.remove("mData");
            } else {
                this.namedTag.putInt("data", 0);
            }
        }
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 140;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "FlowerPot").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        int n = this.namedTag.getShort("item");
        if (n != 0) {
            compoundTag.putShort("item", n).putInt("mData", this.namedTag.getInt("data"));
        }
        return compoundTag;
    }
}

