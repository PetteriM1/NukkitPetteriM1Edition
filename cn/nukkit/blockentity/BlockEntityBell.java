/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityBell
extends BlockEntitySpawnable {
    public BlockEntityBell(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 461;
    }

    @Override
    public String getName() {
        return "Bell";
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return BlockEntity.getDefaultCompound(this, "Bell");
    }
}

