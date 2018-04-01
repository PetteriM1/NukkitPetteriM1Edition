package cn.nukkit.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.Animal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class FlyingEntity extends BaseEntity {

    public FlyingEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }

        Vector3 target = this.target;
        if (!(target instanceof EntityCreature) || !this.targetOption((EntityCreature) target, this.distanceSquared(target))) {
            double near = Integer.MAX_VALUE;

            for (Entity entity : this.getLevel().getEntities()) {
                if (entity == this || !(entity instanceof EntityCreature) || entity instanceof Animal) {
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

                this.moveTime = 0;
                this.target = creature;
            }
        }

        if (this.target instanceof EntityCreature && ((EntityCreature) this.target).isAlive()) {
            return;
        }

        int x, y, z;
        int maxY = Math.max(this.getLevel().getHighestBlockAt((int) this.x, (int) this.z) + 15, 120);
        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) > 5) {
                return;
            }

            x = Utils.rand(10, 30);
            z = Utils.rand(10, 30);
            if (this.y > maxY) {
                y = Utils.rand(-12, -4);
            } else {
                y = Utils.rand(-10, 10);
            }
            this.target = this.add(Utils.rand() ? x : -x, y, Utils.rand() ? z : -z);
        } else if (Utils.rand(1, 410) == 1) {
            x = Utils.rand(10, 30);
            z = Utils.rand(10, 30);
            if (this.y > maxY) {
                y = Utils.rand(-12, -4);
            } else {
                y = Utils.rand(-10, 10);
            }
            this.stayTime = Utils.rand(90, 400);
            this.target = this.add(Utils.rand() ? x : -x, y, Utils.rand() ? z : -z);
        } else if (this.moveTime <= 0 || !(this.target instanceof Vector3)) {
            x = Utils.rand(20, 100);
            z = Utils.rand(20, 100);
            if (this.y > maxY) {
                y = Utils.rand(-12, -4);
            } else {
                y = Utils.rand(-10, 10);
            }
            this.stayTime = 0;
            this.moveTime = Utils.rand(300, 1200);
            this.target = this.add(Utils.rand() ? x : -x, y, Utils.rand() ? z : -z);
        }
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (MobPlugin.MOB_AI_ENABLED && !isImmobile()) {
            if (!this.isMovement()) {
                return null;
            }

            if (this.isKnockback()) {
                this.move(this.motionX * tickDiff, this.motionY * tickDiff, this.motionZ * tickDiff);
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
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                    this.motionY = this.getSpeed() * 0.27 * (y / diff);
                }
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
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
                    this.motionY = this.getSpeed() * 0.27 * (y / diff);
                }
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }

            double dx = this.motionX * tickDiff;
            double dy = this.motionY * tickDiff;
            double dz = this.motionZ * tickDiff;
            Vector3 target = this.target;
            if (this.stayTime > 0) {
                this.stayTime -= tickDiff;
                this.move(0, dy, 0);
            } else {
                Vector2 be = new Vector2(this.x + dx, this.z + dz);
                this.move(dx, dy, dz);
                Vector2 af = new Vector2(this.x, this.z);

                if (be.x != af.x || be.y != af.y) {
                    this.moveTime -= 90 * tickDiff;
                }
            }
            this.updateMovement();
            return target;
        }
        return null;
    }

}
