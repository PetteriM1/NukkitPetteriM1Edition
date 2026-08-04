package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityPotionEffectEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;

public class EntityShulkerBullet extends EntityProjectile {

    public static final int NETWORK_ID = 76;

    public Entity target;
    private int stuckTicks;

    public EntityShulkerBullet(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityShulkerBullet(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getGravity() {
        return -0.001f;
    }

    @Override
    public float getDrag() {
        return 0.001f;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.40f;
    }

    @Override
    public float getHeight() {
        return 0.40f;
    }

    @Override
    protected double getBaseDamage() {
        return 4;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 1200) {
            this.close();
            return false;
        }

        if (target != null && !target.closed && target.isAlive()) {
            if (this.hadCollision || this.isCollided) {
                stuckTicks++;
                if (stuckTicks > 40) {
                    this.close();
                    return false;
                }
                // Reset so it treats this as a flying projectile again
                this.hadCollision = false;
                this.isCollided = false;
            } else {
                stuckTicks = 0;
            }

            double dx = target.x - this.x;
            double dy = (target.y + target.getEyeHeight()) - this.y;
            double dz = target.z - this.z;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (dist > 0.1) {
                final double speed = 0.15;
                double nx = dx / dist;
                double ny = dy / dist;
                double nz = dz / dist;

                if (stuckTicks > 0) {
                    Vector3 alt = chooseAlternateDirection(nx, ny, nz, speed);
                    this.motionX = alt.x;
                    this.motionY = alt.y;
                    this.motionZ = alt.z;
                } else {
                    // Gradually steer toward target, then normalize to constant speed
                    double steer = 0.125;
                    this.motionX += (nx * speed - this.motionX) * steer;
                    this.motionY += (ny * speed - this.motionY) * steer;
                    this.motionZ += (nz * speed - this.motionZ) * steer;

                    double s = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    if (s > 0.001) {
                        this.motionX = (this.motionX / s) * speed;
                        this.motionY = (this.motionY / s) * speed;
                        this.motionZ = (this.motionZ / s) * speed;
                    }
                }
            }
        } else if (this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }

        super.onUpdate(currentTick);
        return !this.closed;
    }

    // Returns the first unblocked direction from four perpendiculars to the direct path.
    // Mirrors vanilla's attempt to navigate around an obstacle before giving up.
    private Vector3 chooseAlternateDirection(double nx, double ny, double nz, double speed) {
        double[][] candidates = {
                {nx, 1.0, nz},  // up-biased
                {nx, -1.0, nz},  // down-biased
                {-nz, 0.0, nx},  // strafe left
                {nz, 0.0, -nx}  // strafe right
        };

        for (double[] c : candidates) {
            double len = Math.sqrt(c[0] * c[0] + c[1] * c[1] + c[2] * c[2]);
            if (len < 0.001) {
                continue;
            }

            double vx = (c[0] / len) * speed;
            double vy = (c[1] / len) * speed;
            double vz = (c[2] / len) * speed;

            if (!this.level.hasCollisionBlocks(this, this.boundingBox.addCoord(vx, vy, vz))) {
                return new Vector3(vx, vy, vz);
            }
        }

        // All four blocked, head directly toward target and let stuckTicks expire
        return new Vector3(nx * speed, ny * speed, nz * speed);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.level.addSound(this, Sound.MOB_SHULKER_BULLET_HIT);
        this.close();
        return true;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        this.level.addSound(this, Sound.MOB_SHULKER_BULLET_HIT);
        entity.addEffect(Effect.getEffect(Effect.LEVITATION).setDuration(200), EntityPotionEffectEvent.Cause.ATTACK);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return entity != shootingEntity && super.canCollideWith(entity);
    }
}
