package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityBlueWitherSkull extends EntityWitherSkull implements EntityExplosive {

    public static final int NETWORK_ID = 91;

    boolean canExplode;

    public EntityBlueWitherSkull(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityBlueWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public void explode() {
        if (this.closed) {
            return;
        }
        this.close();

        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, 1);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this.shootingEntity);
            explosion.setBreakObsidian(true);
            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }

            explosion.explodeB();
        }
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public void setExplode(boolean bool) {
        this.canExplode = bool;
    }
}
