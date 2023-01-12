/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityOwnable;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityTameable
extends EntityWalkingAnimal
implements EntityOwnable {
    public EntityTameable(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public String getOwnerName() {
        return this.namedTag.getString("Owner");
    }

    @Override
    public void setOwnerName(String string) {
        this.namedTag.putString("Owner", string);
    }

    @Override
    public Player getOwner() {
        return this.server.getPlayerExact(this.getOwnerName());
    }
}

