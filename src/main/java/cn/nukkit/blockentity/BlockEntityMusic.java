package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityMusic extends BlockEntity {

    public BlockEntityMusic(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public void changePitch() {
        this.namedTag.putByte("note", (this.namedTag.getByte("note") + 1) % 25);
        setDirty();
    }

    public int getPitch() {
        return this.namedTag.getByte("note");
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("note")) {
            this.namedTag.putByte("note", 0);
        }

        if (!this.namedTag.contains("powered")) {
            this.namedTag.putBoolean("powered", false);
        }

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return level.getBlockIdAt(chunk, (int) x, (int) y, (int) z) == Block.NOTEBLOCK;
    }

    public boolean isPowered() {
        return this.namedTag.getBoolean("powered");
    }

    public void setPowered(boolean powered) {
        this.namedTag.putBoolean("powered", powered);
        setDirty();
    }
}
