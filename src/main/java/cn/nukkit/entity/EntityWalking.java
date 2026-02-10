package cn.nukkit.entity;

import cn.nukkit.block.*;
import cn.nukkit.entity.mob.EntityDrowned;
import cn.nukkit.entity.passive.EntityIronGolem;
import cn.nukkit.entity.passive.EntityLlama;
import cn.nukkit.entity.passive.EntityPig;
import cn.nukkit.entity.passive.EntitySkeletonHorse;
import cn.nukkit.entity.route.RouteFinder;
import cn.nukkit.entity.route.RouteFinderSearchTask;
import cn.nukkit.entity.route.RouteFinderThreadPool;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public abstract class EntityWalking extends BaseEntity {

    /**
     * Hack: Better onGround detection for horse jumps
     */
    protected boolean horseOnGround;

    private static final double FLOW_MULTIPLIER = 0.1;

    protected RouteFinder route;

    public EntityWalking(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    protected boolean checkJump(double dx, double dz) {
        if (this.motionY == this.getGravity() * 2 && this.canSwimIn(level.getBlockIdAt(chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ()))) {
            return true;
        } else {
            if (this.canSwimIn(level.getBlockIdAt(chunk, NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z)))) {
                if (!(this instanceof EntityDrowned || this instanceof EntityIronGolem || this instanceof EntitySkeletonHorse) || this.target == null) {
                    this.motionY = this.getGravity() * 2;
                }
                return true;
            }
        }

        if (!this.onGround || this.stayTime > 0) {
            return false;
        }

        Block that = this.getLevel().getBlock(chunk, NukkitMath.floorDouble(this.x + dx), this.getFloorY(), NukkitMath.floorDouble(this.z + dz), false);
        Block block = that.getSide(this.getHorizontalFacing());
        Block down;
        if (this.followTarget == null && !(this.target instanceof Entity) && this.passengers.isEmpty() && !(down = block.down()).isSolid() && !block.isSolid() && !down.down().isSolid()) {
            this.stayTime = 10; // "hack": try to make mobs not to be so suicidal
        } else if (!block.canPassThrough() && !(block instanceof BlockFlowable || block.getId() == BlockID.SOUL_SAND) && block.up().canPassThrough() && that.up(2).canPassThrough()) {
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

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }

        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed() && targetOption((EntityCreature) this.followTarget, this.distanceSquared(this.followTarget)) && this.target != null) {
            return;
        }

        this.followTarget = null;

        if (!this.passengers.isEmpty() && !(this instanceof EntityLlama) && !(this instanceof EntityPig)) {
            return;
        }

        double near = Integer.MAX_VALUE;

        for (Entity entity : this.getLevel().getEntitiesList()) {
            if (entity == this || !(entity instanceof EntityCreature) || entity.closed || !this.canTarget(entity)) {
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
            this.followTarget = creature;
            if (this.route == null && this.passengers.isEmpty()) this.target = creature;
        }

        if (!this.canSetTemporalTarget()) {
            return;
        }

        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) != 1) {
                return;
            }
            this.target = this.add(Utils.rand(-30, 30), Utils.rand(-20.0, 20.0) / 10, Utils.rand(-30, 30));
        } else if (Utils.rand(1, 100) == 1) {
            this.stayTime = Utils.rand(80, 200);
            this.target = this.add(Utils.rand(-30, 30), Utils.rand(-20.0, 20.0) / 10, Utils.rand(-30, 30));
        } else if (this.moveTime <= 0 || this.target == null) {
            this.stayTime = 0;
            this.moveTime = Utils.rand(80, 200);
            double tx = this.x;
            double tz = this.z;
            int attempts = 0;
            boolean inWater = true;
            while (attempts++ < 10 && inWater) {
                tx = this.x + Utils.rand(-30, 30);
                tz = this.z + Utils.rand(-30, 30);
                int txFloor = NukkitMath.floorDouble(tx);
                int tzFloor = NukkitMath.floorDouble(tz);
                inWater = Block.isWater(level.getBlockIdAt(chunk, txFloor, level.getHighestBlockAt(txFloor, tzFloor), tzFloor));
            }
            this.target = new Vector3(tx, this.y + Utils.rand(-20.0, 20.0) / 10, tz);
        }
    }

    public RouteFinder getRoute() {
        return this.route;
    }

    public void setRoute(RouteFinder route) {
        this.route = route;
    }

    public Vector3 updateMove(int tickDiff) {
        if (!this.isInTickingRange(server.entityActivationRange)) {
            return null;
        }
        if (!this.isImmobile()) {
            if (this.age % 10 == 0 && this.route != null && !this.route.isSearching()) {
                RouteFinderThreadPool.executeRouteFinderThread(new RouteFinderSearchTask(this.route));
                if (this.route.hasNext()) {
                    this.target = this.route.next();
                }
            }

            if (this.isKnockback()) {
                if (this.riding == null) {
                    this.move(this.motionX, this.motionY, this.motionZ);
                    if ((this instanceof EntityDrowned) && this.isInsideOfWater()) {
                        this.motionY -= this.getGravity() * 0.3;
                    } else {
                        this.motionY -= this.getGravity();
                    }
                    this.updateMovement();
                }
                return this.followTarget != null ? this.followTarget : this.target;
            }

            Block levelBlock = getLevelBlock();
            boolean inWater = levelBlock.getId() == 8 || levelBlock.getId() == 9;
            int downId = level.getBlockIdAt(chunk, getFloorX(), getFloorY() - 1, getFloorZ());
            this.horseOnGround = Block.isBlockSolidById(downId) || !Block.isBlockTransparentById(downId);
            if (inWater && (downId == 0 || downId == 8 || downId == 9 || downId == BlockID.LAVA || downId == BlockID.STILL_LAVA || downId == BlockID.SIGN_POST || downId == BlockID.WALL_SIGN))
                onGround = false;
            if (downId == 0 || downId == BlockID.SIGN_POST || downId == BlockID.WALL_SIGN) onGround = false;
            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.target != null && this.followTarget.canBeFollowed()) {
                double x = this.target.x - this.x;
                double z = this.target.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.riding != null || diff <= 0.001 || !inWater && (this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() / 2 + 0.3) * nearbyDistanceMultiplier())) {
                    if (!this.isInsideOfWater()) {
                        this.motionX = 0;
                        this.motionZ = 0;
                    }
                } else {
                    if (levelBlock.getId() == BlockID.WATER) {
                        BlockWater blockWater = (BlockWater) levelBlock;
                        Vector3 flowVector = blockWater.getFlowVector();
                        motionX = flowVector.getX() * FLOW_MULTIPLIER;
                        motionZ = flowVector.getZ() * FLOW_MULTIPLIER;
                    } else if (Block.isWater(levelBlock.getId())) {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.05 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.05 * (z / diff);
                        if (!(this instanceof EntityDrowned || this instanceof EntityIronGolem || this instanceof EntitySkeletonHorse))
                            this.level.addParticle(new BubbleParticle(this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0), Utils.rand(-2.0, 2.0))));
                    } else {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.1 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.1 * (z / diff);
                    }
                }
                if (this.noRotateTicks <= 0 && (this.passengers.isEmpty() || this instanceof EntityLlama || this instanceof EntityPig) && (this.stayTime <= 0 || Utils.rand()) && diff > 0.001) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(x / diff, z / diff)));
                }
            }

            if (this.isLookupForTarget()) {
                this.checkTarget();
            }
            if (this.target != null || !this.isLookupForTarget()) {
                double x = this.target.x - this.x;
                double z = this.target.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                boolean distance = false;
                if (this.riding != null || diff <= 0.001 || !inWater && (this.stayTime > 0 || (distance = this.distance(this.target) <= (this.getWidth() / 2 + 0.3) * nearbyDistanceMultiplier()))) {
                    if (!this.isInsideOfWater()) {
                        this.motionX = 0;
                        this.motionZ = 0;
                    }
                } else {
                    if (levelBlock.getId() == BlockID.WATER) {
                        BlockWater blockWater = (BlockWater) levelBlock;
                        Vector3 flowVector = blockWater.getFlowVector();
                        motionX = flowVector.getX() * FLOW_MULTIPLIER;
                        motionZ = flowVector.getZ() * FLOW_MULTIPLIER;
                    } else if (Block.isWater(levelBlock.getId())) {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.05 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.05 * (z / diff);
                        if (!(this instanceof EntityDrowned || this instanceof EntityIronGolem || this instanceof EntitySkeletonHorse)) {
                            this.level.addParticle(new BubbleParticle(this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0), Utils.rand(-2.0, 2.0))));
                        } else if (this.followTarget != null) {
                            double y = this.followTarget.y - this.y;
                            this.motionY = this.getSpeed() * moveMultiplier * 0.05 * (y / (diff + Math.abs(y)));
                        }
                    } else {
                        this.motionX = this.getSpeed() * moveMultiplier * 0.15 * (x / diff);
                        this.motionZ = this.getSpeed() * moveMultiplier * 0.15 * (z / diff);
                    }
                }
                if (this.noRotateTicks <= 0 && !distance && (this.passengers.isEmpty() || this instanceof EntityLlama || this instanceof EntityPig) && (this.stayTime <= 0 || Utils.rand()) && diff > 0.001) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(x / diff, z / diff)));
                }
            }

            double dx = this.motionX;
            double dz = this.motionZ;
            boolean isJump = this.checkJump(dx, dz);
            if (this.stayTime > 0 && !inWater) {
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
                if (this.onGround && !inWater) {
                    this.motionY = 0;
                } else if (this.motionY > -this.getGravity() * 4) {
                    int b = this.level.getBlockIdAt(chunk, NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
                    if (!Block.isWater(b) && b != Block.LAVA && b != Block.STILL_LAVA) {
                        this.motionY -= this.getGravity();
                    }
                } else {
                    if ((this instanceof EntityDrowned || this instanceof EntityIronGolem || this instanceof EntitySkeletonHorse) && inWater && this.motionY < 0) {
                        this.motionY = this.getGravity() * -0.3; // Slow drowned's fall when going into water
                        this.stayTime = 40;
                    } else {
                        this.motionY -= this.getGravity();
                    }
                }
            }

            this.updateMovement();
            if (this.route != null) {
                if (this.route.hasCurrentNode() && this.route.hasArrivedNode(this)) {
                    if (this.route.hasNext()) {
                        this.target = this.route.next();
                    }
                }
            }
            return this.followTarget != null ? this.followTarget : this.target;
        }
        return null;
    }
}
