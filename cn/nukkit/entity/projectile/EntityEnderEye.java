/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.concurrent.ThreadLocalRandom;

public class EntityEnderEye
extends EntityProjectile {
    public static final int NETWORK_ID = 70;

    public EntityEnderEye(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityEnderEye(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 70;
    }

    @Override
    public boolean onUpdate(int n) {
        boolean bl = super.onUpdate(n);
        if (!this.isAlive()) {
            return bl;
        }
        if (this.age >= 60) {
            if ((double)ThreadLocalRandom.current().nextFloat() > 0.2) {
                this.level.dropItem(this, Item.get(381));
            }
            this.close();
            bl = true;
        } else if (this.age == 40) {
            this.setMotion(new Vector3(0.0, 0.2, 0.0));
            bl = true;
        }
        return bl;
    }
}

