/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;

public class BlockEntityBed
extends BlockEntitySpawnable {
    public int color;

    public BlockEntityBed(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("color")) {
            this.namedTag.putByte("color", 0);
        }
        this.color = this.namedTag.getByte("color");
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 26;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("color", this.color);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "Bed").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putByte("color", this.color);
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.color);
    }
}

