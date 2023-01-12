/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.passive.EntityTameable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityTameableAnimal
extends EntityTameable
implements cn.nukkit.entity.EntityTameable {
    private Player w = null;
    private String v = "";
    private boolean u = false;

    public EntityTameableAnimal(FullChunk fullChunk, CompoundTag compoundTag) {
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
        this.namedTag.putBoolean("Sitting", this.u);
        if (this.w != null) {
            this.namedTag.putString("Owner", this.w.getName());
            this.namedTag.putString("OwnerUUID", this.w.getUniqueId().toString());
        }
    }

    @Override
    public Player getOwner() {
        this.checkOwner();
        return this.w;
    }

    @Override
    public boolean hasOwner() {
        return this.hasOwner(true);
    }

    public boolean hasOwner(boolean bl) {
        if (bl) {
            this.checkOwner();
            return this.w != null;
        }
        if (this.namedTag != null) {
            String string = this.namedTag.getString("Owner");
            return string != null && !string.isEmpty();
        }
        return false;
    }

    @Override
    public void setOwner(Player player) {
        this.w = player;
        this.setDataProperty(new LongEntityData(5, player.getId()));
        this.setTamed(true);
    }

    @Override
    public boolean isSitting() {
        return this.u;
    }

    @Override
    public void setSitting(boolean bl) {
        this.u = bl;
        this.setDataFlag(0, 24, bl);
    }

    public void setTamed(boolean bl) {
        this.setDataFlag(0, 28, bl);
    }

    @Override
    public String getOwnerUUID() {
        return this.v;
    }

    @Override
    public void setOwnerUUID(String string) {
        this.v = string;
    }

    @Override
    public Vector3 updateMove(int n) {
        if (this.u) {
            return this.target;
        }
        return super.updateMove(n);
    }

    public void checkOwner() {
        Player player;
        String string;
        if (this.w == null && this.namedTag != null && (string = this.namedTag.getString("Owner")) != null && !string.isEmpty() && (player = this.getServer().getPlayerExact(string)) != null) {
            this.setOwner(player);
        }
    }
}

