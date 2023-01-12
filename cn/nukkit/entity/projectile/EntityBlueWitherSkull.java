/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.projectile.EntityWitherSkull;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.StrongExplosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityBlueWitherSkull
extends EntityWitherSkull
implements EntityExplosive {
    public static final int NETWORK_ID = 91;
    boolean k;

    public EntityBlueWitherSkull(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityBlueWitherSkull(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 91;
    }

    public void setExplode(boolean bl) {
        this.k = bl;
    }

    @Override
    public void explode() {
        if (this.closed) {
            return;
        }
        this.close();
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 1.2);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (!entityExplosionPrimeEvent.isCancelled()) {
            StrongExplosion strongExplosion = new StrongExplosion(this, (float)entityExplosionPrimeEvent.getForce(), this.shootingEntity);
            if (entityExplosionPrimeEvent.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                ((Explosion)strongExplosion).explodeA();
            }
            ((Explosion)strongExplosion).explodeB();
        }
    }
}

