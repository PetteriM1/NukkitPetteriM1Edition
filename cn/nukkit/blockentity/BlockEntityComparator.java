/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityComparator
extends BlockEntity {
    private int b;

    public BlockEntityComparator(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        if (!compoundTag.contains("OutputSignal")) {
            compoundTag.putInt("OutputSignal", 0);
        }
        this.b = compoundTag.getInt("OutputSignal");
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 150 || n == 149;
    }

    public int getOutputSignal() {
        return this.b;
    }

    public void setOutputSignal(int n) {
        this.b = n;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("OutputSignal", this.b);
    }
}

