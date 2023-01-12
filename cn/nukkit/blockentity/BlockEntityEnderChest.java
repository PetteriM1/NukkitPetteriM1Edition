/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityEnderChest
extends BlockEntitySpawnable {
    public BlockEntityEnderChest(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 130;
    }

    @Override
    public String getName() {
        return "EnderChest";
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "EnderChest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
    }
}

