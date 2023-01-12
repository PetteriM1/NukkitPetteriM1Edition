/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityHanging
extends Entity {
    protected int direction;

    public EntityHanging(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(1);
        super.initEntity();
        this.setHealth(1.0f);
        if (this.namedTag.contains("Direction")) {
            this.direction = this.namedTag.getByte("Direction");
        } else if (this.namedTag.contains("Dir")) {
            int n = this.namedTag.getByte("Dir");
            if (n == 2) {
                this.direction = 0;
            } else if (n == 0) {
                this.direction = 2;
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Direction", this.getDirection().getHorizontalIndex());
        this.namedTag.putInt("TileX", (int)this.x);
        this.namedTag.putInt("TileY", (int)this.y);
        this.namedTag.putInt("TileZ", (int)this.z);
    }

    @Override
    public BlockFace getDirection() {
        return BlockFace.fromIndex(this.direction);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            this.close();
            return true;
        }
        if (this.lastYaw != this.yaw || this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z) {
            this.despawnFromAll();
            this.direction = (int)(this.yaw / 90.0);
            this.lastYaw = this.yaw;
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.spawnToAll();
            return true;
        }
        return false;
    }

    protected boolean isSurfaceValid() {
        return true;
    }
}

