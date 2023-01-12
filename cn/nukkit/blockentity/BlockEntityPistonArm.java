/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

public class BlockEntityPistonArm
extends BlockEntitySpawnable {
    public float progress;
    public float lastProgress;
    public BlockFace facing;
    public boolean extending = false;
    public boolean sticky = false;
    public byte state;
    public byte newState = 1;
    public Vector3 attachedBlock = null;
    public boolean isMovable = true;

    public BlockEntityPistonArm(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.isMovable = true;
        if (this.namedTag.contains("Progress")) {
            this.progress = this.namedTag.getFloat("Progress");
        }
        if (this.namedTag.contains("LastProgress")) {
            this.lastProgress = this.namedTag.getInt("LastProgress");
        }
        if (this.namedTag.contains("Sticky")) {
            this.sticky = this.namedTag.getBoolean("Sticky");
        }
        if (this.namedTag.contains("Extending")) {
            this.extending = this.namedTag.getBoolean("Extending");
        }
        if (this.namedTag.contains("State")) {
            this.state = (byte)this.namedTag.getByte("State");
        }
        if (this.namedTag.contains("AttachedBlocks")) {
            ListTag<IntTag> listTag = this.namedTag.getList("AttachedBlocks", IntTag.class);
            if (listTag != null && listTag.size() > 0) {
                this.attachedBlock = new Vector3(listTag.get(0).getData().intValue(), listTag.get(1).getData().intValue(), listTag.get(2).getData().intValue());
            }
        } else {
            this.namedTag.putList(new ListTag("AttachedBlocks"));
        }
        super.initBlockEntity();
    }

    public void setExtended(boolean bl) {
        this.extending = bl;
        this.newState = this.state;
        this.lastProgress = this.progress;
        this.state = (byte)(bl ? 1 : 0);
        this.progress = bl ? 1.0f : 0.0f;
    }

    public boolean isExtended() {
        return this.extending;
    }

    public void broadcastMove() {
        this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), this.createSpawnPacket());
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.getBlock().getId();
        return n == 33 || n == 29;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("isMovable", this.isMovable);
        this.namedTag.putByte("State", this.state);
        this.namedTag.putByte("NewState", this.newState);
        this.namedTag.putFloat("Progress", this.progress);
        this.namedTag.putFloat("LastProgress", this.lastProgress);
        this.namedTag.putBoolean("Sticky", this.sticky);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "PistonArm").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putFloat("Progress", this.progress).putFloat("LastProgress", this.lastProgress).putBoolean("Sticky", this.sticky).putByte("State", this.state).putByte("NewState", this.newState);
    }
}

