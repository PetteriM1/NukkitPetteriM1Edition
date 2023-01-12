/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntitySwimming;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityWaterAnimal
extends EntitySwimming
implements EntityAnimal {
    public EntityWaterAnimal(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public double getSpeed() {
        return 0.8;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }
        int n2 = n - this.lastUpdate;
        this.lastUpdate = n;
        this.entityBaseTick(n2);
        Vector3 vector3 = this.updateMove(n2);
        return true;
    }
}

