/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityMusic
extends BlockEntity {
    public BlockEntityMusic(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("note")) {
            this.namedTag.putByte("note", 0);
        }
        if (!this.namedTag.contains("powered")) {
            this.namedTag.putBoolean("note", false);
        }
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 25;
    }

    public void changePitch() {
        this.namedTag.putByte("note", (this.namedTag.getByte("note") + 1) % 25);
    }

    public int getPitch() {
        return this.namedTag.getByte("note");
    }

    public void setPowered(boolean bl) {
        this.namedTag.putBoolean("powered", bl);
    }

    public boolean isPowered() {
        return this.namedTag.getBoolean("powered");
    }
}

