package cn.nukkit.entity;

import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

public abstract class EntitySwimming extends BaseEntity {

    public EntitySwimming(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }

        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive()) {
            return;
        }

        Vector3 target = this.target;
        if (!(target instanceof EntityCreature) || !this.targetOption((EntityCreature) target, this.distanceSquared(target))) {
            double near = Integer.MAX_VALUE;

            for (Entity entity : this.getLevel().getEntities()) {
                if (entity == this || !(entity instanceof EntityCreature) || entity instanceof EntityAnimal) {
                    continue;
                }

                EntityCreature creature = (EntityCreature) entity;
                if (creature instanceof BaseEntity && ((BaseEntity) creature).isFriendly() == this.isFriendly()) {
                    continue;
                }

                double distance = this.distanceSquared(creature);
                if (distance > near || !this.targetOption(creature, distance)) {
                    continue;
                }
                near = distance;

                this.stayTime = 0;
                this.moveTime = 0;
                this.target = creature;
            }
        }

        if (this.target instanceof EntityCreature && !((EntityCreature) this.target).closed && ((EntityCreature) this.target).isAlive() && this.targetOption((EntityCreature) this.target, this.distanceSquared(this.target))) {
            return;
        }

        int x, z;
        if (this.stayTime > 0) {
            if (EntityUtils.rand(1, 100) > 5) {
                return;
            }
            x = EntityUtils.rand(10, 30);
            z = EntityUtils.rand(10, 30);
            this.target = this.add(EntityUtils.rand() ? x : -x, EntityUtils.rand(-20, 20) / 10, EntityUtils.rand() ? z : -z);
        } else if (EntityUtils.rand(1, 410) == 1) {
            x = EntityUtils.rand(10, 30);
            z = EntityUtils.rand(10, 30);
            this.stayTime = EntityUtils.rand(100, 300);
            this.target = this.add(EntityUtils.rand() ? x : -x, EntityUtils.rand(-20, 20) / 10, EntityUtils.rand() ? z : -z);
        } else if (this.moveTime <= 0 || this.target == null) {
            x = EntityUtils.rand(20, 100);
            z = EntityUtils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = EntityUtils.rand(200, 1200);
            this.target = this.add(EntityUtils.rand() ? x : -x, 0, EntityUtils.rand() ? z : -z);
        }
    }

    protected boolean checkJump(double dx, double dz) {
        if (this.isInsideOfWater() && (this.motionX > 0 || this.motionZ > 0)) {
            this.motionY = EntityUtils.rand(-0.12, 0.12);
        } else if (!this.isOnGround() && !isInsideOfWater()) {
            this.motionY -= this.getGravity();
        } else {
            this.motionY = 0;
        }
        return true;
    }

    public Vector3 updateMove(int tickDiff) {
        if (this.getServer().getMobAiEnabled() && !isImmobile()) {
            if (!this.isMovement()) {
                return null;
            }

            if (this.isKnockback()) {
                this.move(this.motionX * tickDiff, this.motionY, this.motionZ * tickDiff);
                this.motionY -= this.getGravity() * tickDiff;
                this.updateMovement();
                return null;
            }

            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive()) {
                double x = this.followTarget.x - this.x;
                double y = this.followTarget.y - this.y;
                double z = this.followTarget.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() + 0.0d) / 2 + 0.05) {
                    this.motionX = 0;
                    this.motionZ = 0;
                } else {
                    this.motionX = this.getSpeed() * 0.1 * (x / diff);
                    this.motionZ = this.getSpeed() * 0.1 * (z / diff);
                }
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
                return this.followTarget;
            }

            Vector3 before = this.target;
            this.checkTarget();
            if (this.target instanceof EntityCreature || before != this.target) {
                double x = this.target.x - this.x;
                double y = this.target.y - this.y;
                double z = this.target.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.stayTime > 0 || this.distance(this.target) <= (this.getWidth() + 0.0d) / 2 + 0.05) {
                    this.motionX = 0;
                    this.motionZ = 0;
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                }
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }

            double dx = this.motionX * tickDiff;
            double dz = this.motionZ * tickDiff;
            boolean isJump = this.checkJump(dx, dz);
            if (this.stayTime > 0) {
                this.stayTime -= tickDiff;
                this.move(0, this.motionY * tickDiff, 0);
            } else {
                Vector2 be = new Vector2(this.x + dx, this.z + dz);
                this.move(dx, this.motionY * tickDiff, dz);
                Vector2 af = new Vector2(this.x, this.z);

                if ((be.x != af.x || be.y != af.y) && !isJump) {
                    this.moveTime -= 90 * tickDiff;
                }
            }

            if (!isJump) {
                if (this.onGround) {
                    this.motionY = 0;
                } else if (this.motionY > -this.getGravity() * 4) {
                    if (!(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid)) {
                        this.motionY -= this.getGravity() * 1;
                    }
                } else {
                    this.motionY -= this.getGravity() * tickDiff;
                }
            }

            this.updateMovement();
            return this.target;
        }

        return null;
    }
}
