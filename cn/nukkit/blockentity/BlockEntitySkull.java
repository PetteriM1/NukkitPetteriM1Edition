/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntitySkull
extends BlockEntitySpawnable {
    public BlockEntitySkull(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("SkullType")) {
            this.namedTag.putByte("SkullType", 0);
        }
        if (!this.namedTag.contains("Rot")) {
            this.namedTag.putByte("Rot", 0);
        }
        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.remove("Creator");
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 144;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "Skull").put("SkullType", this.namedTag.get("SkullType")).putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).put("Rot", this.namedTag.get("Rot"));
    }
}

