/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityTameable;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityTameableMob
extends EntityWalkingMob
implements EntityTameable {
    private Player B = null;
    private String w = "";
    private boolean A = false;

    public EntityTameableMob(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        String string;
        super.initEntity();
        if (this.namedTag != null && (string = this.namedTag.getString("Owner")) != null && !string.isEmpty()) {
            Player player = this.getServer().getPlayerExact(string);
            if (player != null) {
                this.setOwner(player);
            }
            this.setSitting(this.namedTag.getBoolean("Sitting"));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Sitting", this.A);
        if (this.B != null) {
            this.namedTag.putString("Owner", this.B.getName());
            this.namedTag.putString("OwnerUUID", this.B.getUniqueId().toString());
        }
    }

    @Override
    public Player getOwner() {
        this.checkOwner();
        return this.B;
    }

    @Override
    public boolean hasOwner() {
        return this.hasOwner(true);
    }

    public boolean hasOwner(boolean bl) {
        if (bl) {
            this.checkOwner();
            return this.B != null;
        }
        if (this.namedTag != null) {
            String string = this.namedTag.getString("Owner");
            return string != null && !string.isEmpty();
        }
        return false;
    }

    @Override
    public void setOwner(Player player) {
        this.B = player;
        this.setDataProperty(new LongEntityData(5, player.getId()));
        this.setTamed(true);
    }

    @Override
    public boolean isSitting() {
        return this.A;
    }

    @Override
    public void setSitting(boolean bl) {
        this.A = bl;
        this.setDataFlag(0, 24, bl);
    }

    public void setTamed(boolean bl) {
        this.setDataFlag(0, 28, bl);
    }

    @Override
    public String getOwnerUUID() {
        return this.w;
    }

    @Override
    public void setOwnerUUID(String string) {
        this.w = string;
    }

    @Override
    public Vector3 updateMove(int n) {
        if (this.A) {
            return this.target;
        }
        return super.updateMove(n);
    }

    public void checkOwner() {
        Player player;
        String string;
        if (this.B == null && this.namedTag != null && (string = this.namedTag.getString("Owner")) != null && !string.isEmpty() && (player = this.getServer().getPlayerExact(string)) != null) {
            this.setOwner(player);
        }
    }
}

