package cn.nukkit.entity;

import cn.nukkit.block.*;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.passive.EntityLlama;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

public abstract class EntityWalking extends BaseEntity {

    public EntityWalking(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }

        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
            return;
        }

        Vector3 target = this.target;
        if (!(target instanceof EntityCreature) || !this.targetOption((EntityCreature) target, this.distanceSquared(target)) || !((Entity) target).canBeFollowed()) {
            double near = Integer.MAX_VALUE;

            for (Entity entity : this.getViewers().values()) {
                if (entity == this || !(entity instanceof EntityCreature) || (entity instanceof EntityAnimal || !entity.canBeFollowed())) {
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
            if (Utils.rand(1, 100) > 5) {
                return;
            }
            x = Utils.rand(10, 30);
            z = Utils.rand(10, 30);
            this.target = this.add(Utils.rand() ? x : -x, Utils.rand(-20.0, 20.0) / 10, Utils.rand() ? z : -z);
        } else if (Utils.rand(1, 100) == 1) {
            x = Utils.rand(10, 30);
            z = Utils.rand(10, 30);
            this.stayTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? x : -x, Utils.rand(-20.0, 20.0) / 10, Utils.rand() ? z : -z);
        } else if (this.moveTime <= 0 || this.target == null) {
            x = Utils.rand(20, 100);
            z = Utils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? x : -x, 0, Utils.rand() ? z : -z);
        }
    }

    protected boolean checkJump(double dx, double dz) {
        if (this.motionY == this.getGravity() * 2) {
            int b = level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z));
            return b == BlockID.WATER || b == BlockID.STILL_WATER;
        } else {
            int b = level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
            if (b == BlockID.WATER || b == BlockID.STILL_WATER) {
                if (!this.isDrowned || this.target == null) {
                    this.motionY = this.getGravity() * 2;
                }
                return true;
            }
        }

        if (!this.onGround || this.stayTime > 0) {
            return false;
        }

        Block that = this.getLevel().getBlock(new Vector3(NukkitMath.floorDouble(this.x + dx), (int) this.y, NukkitMath.floorDouble(this.z + dz)));
        /*if (this.getDirection() == null) {
            return false;
        }*/

        Block block = that.getSide(this.getHorizontalFacing());
        Block down = block.down();
        if (!down.isSolid() && !block.isSolid() && !down.down().isSolid()) {
            this.stayTime = 10; // "hack": try to make mobs not to be so suicidal
        } else if (!block.canPassThrough() && block.up().canPassThrough() && that.up(2).canPassThrough()) {
            if (block instanceof BlockFence || block instanceof BlockFenceGate) {
                this.motionY = this.getGravity();
            } else if (this.motionY <= this.getGravity() * 4) {
                this.motionY = this.getGravity() * 4;
            } else if (block instanceof BlockStairs) {
                this.motionY = this.getGravity() * 4;
            } else if (this.motionY <= (this.getGravity() * 8)) {
                this.motionY = this.getGravity() * 8;
            } else {
                this.motionY += this.getGravity() * 0.25;
            }
            return true;
        }
        return false;
    }

    public Vector3 updateMove(int tickDiff) {
        if (this.getServer().getMobAiEnabled() && !isImmobile()) {
            if (!this.isMovement()) {
                return null;
            }

            if (this.isKnockback()) {
                this.move(this.motionX, this.motionY, this.motionZ);
                this.motionY -= this.getGravity();
                this.updateMovement();
                return null;
            }

            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
                double x = this.followTarget.x - this.x;
                double z = this.followTarget.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() / 2 + 0.05)) {
                    this.motionX = 0;
                    this.motionZ = 0;
                } else {
                    if (this.isInsideOfWater()) {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.05 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.05 * (z / diff);
                        if (!this.isDrowned) this.level.addParticle(new BubbleParticle(this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0), Utils.rand(-2.0, 2.0))));
                    } else {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.1 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.1 * (z / diff);
                    }
                }
                if ((this.passengers.isEmpty() || this instanceof EntityLlama) && (this.stayTime <= 0 || Utils.rand())) this.yaw = FastMath.toDegrees(-FastMath.atan2(x / diff, z / diff));
                return this.followTarget;
            }

            Vector3 before = this.target;
            this.checkTarget();
            if (this.target instanceof EntityCreature || before != this.target) {
                double x = this.target.x - this.x;
                double z = this.target.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.stayTime > 0 || (this.distance(this.target) <= (this.getWidth() / 2 + 0.05) * nearbyDistanceMultiplier() && !this.isInsideOfWater())) {
                    this.motionX = 0;
                    this.motionZ = 0;
                } else {
                    if (this.isInsideOfWater()) {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.05 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.05 * (z / diff);
                        if (!this.isDrowned) this.level.addParticle(new BubbleParticle(this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0), Utils.rand(-2.0, 2.0))));
                    } else {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.15 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.15 * (z / diff);
                    }
                }
                if ((this.passengers.isEmpty() || this instanceof EntityLlama) && (this.stayTime <= 0 || Utils.rand())) this.yaw = FastMath.toDegrees(-FastMath.atan2(x / diff, z / diff));
            }

            double dx = this.motionX;
            double dz = this.motionZ;
            boolean isJump = this.checkJump(dx, dz);
            if (this.stayTime > 0) {
                this.stayTime -= tickDiff;
                this.move(0, this.motionY, 0);
            } else {
                Vector2 be = new Vector2(this.x + dx, this.z + dz);
                this.move(dx, this.motionY, dz);
                Vector2 af = new Vector2(this.x, this.z);

                if ((be.x != af.x || be.y != af.y) && !isJump) {
                    this.moveTime -= 90;
                }
            }

            if (!isJump) {
                if (this.onGround) {
                    this.motionY = 0;
                } else if (this.motionY > -this.getGravity() * 4) {
                    int b = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
                    if (b != Block.WATER && b != Block.STILL_WATER && b != Block.LAVA && b != Block.STILL_LAVA) {
                        this.motionY -= this.getGravity();
                    }
                } else {
                    this.motionY -= this.getGravity();
                }
            }
            this.updateMovement();
            return this.target;
        }
        return null;
    }
}
