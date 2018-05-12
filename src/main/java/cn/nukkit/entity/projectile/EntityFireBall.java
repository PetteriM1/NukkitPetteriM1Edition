package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.Utils;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityFireBall extends EntityProjectile {

    public static final int NETWORK_ID = 85;

    protected boolean critical = false;

    protected boolean canExplode = false;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getDamage() {
        return 4;
    }

    public EntityFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);

        this.critical = critical;
    }

    public boolean isExplode() {
        return this.canExplode;
    }

    public void setExplode(boolean bool) {
        this.canExplode = bool;
    }

    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        boolean hasUpdate = super.onUpdate(currentTick);

        if (!this.hadCollision && this.critical) {
            this.level.addParticle(new CriticalParticle(
                    this.add(this.getWidth() / 2 + Utils.rand(-100, 100) / 500, this.getHeight() / 2 + Utils.rand(-100, 100) / 500, this.getWidth() / 2 + Utils.rand(-100, 100) / 500)));
        } else if (this.onGround) {
            this.critical = false;
        }

        if (this.age > 1200 || this.isCollided) {
            if (this.isCollided && this.canExplode) {
                ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 2.8);
                this.server.getPluginManager().callEvent(ev);
                if (!ev.isCancelled()) {
                    Explosion explosion = new Explosion(this, (float) ev.getForce(), this.shootingEntity);
                    if (ev.isBlockBreaking()) {
                        explosion.explodeA();
                    }
                    explosion.explodeB();
                }
            }
            this.kill();
            hasUpdate = true;
        }

        return hasUpdate;
    }

    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = NETWORK_ID;
        pk.entityRuntimeId = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }
}
