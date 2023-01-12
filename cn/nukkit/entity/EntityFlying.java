/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public abstract class EntityFlying
extends BaseEntity {
    public EntityFlying(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        this.noFallDamage = true;
    }

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }
        Vector3 vector3 = this.target;
        if (!(vector3 instanceof EntityCreature) || !((EntityCreature)vector3).closed && !this.targetOption((EntityCreature)vector3, this.distanceSquared(vector3)) || !((Entity)vector3).canBeFollowed()) {
            this.followTarget = null;
            double d2 = 2.147483647E9;
            for (Entity entity : this.getLevel().getEntities()) {
                double d3;
                EntityCreature entityCreature;
                if (entity == this || !(entity instanceof EntityCreature) || entity.closed || !this.canTarget(entity) || (entityCreature = (EntityCreature)entity) instanceof BaseEntity && ((BaseEntity)entityCreature).isFriendly() == this.isFriendly() || (d3 = this.distanceSquared(entityCreature)) > d2 || !this.targetOption(entityCreature, d3)) continue;
                d2 = d3;
                this.stayTime = 0;
                this.moveTime = 0;
                this.target = entityCreature;
                this.followTarget = entityCreature;
            }
        }
        if (this.target instanceof EntityCreature && ((EntityCreature)this.target).isAlive()) {
            return;
        }
        int n = Math.max(this.getLevel().getHighestBlockAt(this.chunk, this.getFloorX(), this.getFloorZ(), true) + 15, 120);
        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) > 5) {
                return;
            }
            int n2 = Utils.rand(10, 30);
            int n3 = Utils.rand(10, 30);
            int n4 = this.y > (double)n ? Utils.rand(-12, -4) : Utils.rand(-10, 10);
            this.target = this.add(Utils.rand() ? (double)n2 : (double)(-n2), n4, Utils.rand() ? (double)n3 : (double)(-n3));
        } else if (Utils.rand(1, 100) == 1) {
            int n5 = Utils.rand(10, 30);
            int n6 = Utils.rand(10, 30);
            int n7 = this.y > (double)n ? Utils.rand(-12, -4) : Utils.rand(-10, 10);
            this.stayTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? (double)n5 : (double)(-n5), n7, Utils.rand() ? (double)n6 : (double)(-n6));
        } else if (this.moveTime <= 0 || this.target == null) {
            int n8 = Utils.rand(20, 100);
            int n9 = Utils.rand(20, 100);
            int n10 = this.y > (double)n ? Utils.rand(-12, -4) : Utils.rand(-10, 10);
            this.stayTime = 0;
            this.moveTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? (double)n8 : (double)(-n8), n10, Utils.rand() ? (double)n9 : (double)(-n9));
        }
    }

    @Override
    public Vector3 updateMove(int n) {
        if (this.isMovement() && !this.isImmobile()) {
            double d2;
            double d3;
            double d4;
            if (this.isKnockback()) {
                this.move(this.motionX, this.motionY, this.motionZ);
                this.updateMovement();
                return null;
            }
            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
                double d5 = this.followTarget.x - this.x;
                double d6 = this.followTarget.y - this.y;
                double d7 = this.followTarget.z - this.z;
                double d8 = Math.abs(d5) + Math.abs(d7);
                if (this.stayTime > 0 || this.distance(this.followTarget) <= (double)(this.getWidth() / 2.0f) + 0.05) {
                    this.motionX = 0.0;
                    this.motionZ = 0.0;
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (d5 / d8);
                    this.motionZ = this.getSpeed() * 0.15 * (d7 / d8);
                    this.motionY = this.getSpeed() * 0.27 * (d6 / d8);
                }
                if (this.stayTime <= 0 || Utils.rand()) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d5 / d8, d7 / d8)));
                }
            }
            Vector3 vector3 = this.target;
            this.checkTarget();
            if (this.target instanceof EntityCreature || vector3 != this.target) {
                d4 = this.target.x - this.x;
                d3 = this.target.y - this.y;
                d2 = this.target.z - this.z;
                double d9 = Math.abs(d4) + Math.abs(d2);
                if (this.stayTime > 0 || this.distance(this.target) <= ((double)(this.getWidth() / 2.0f) + 0.05) * (double)this.nearbyDistanceMultiplier()) {
                    this.motionX = 0.0;
                    this.motionZ = 0.0;
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (d4 / d9);
                    this.motionZ = this.getSpeed() * 0.15 * (d2 / d9);
                    this.motionY = this.getSpeed() * 0.27 * (d3 / d9);
                }
                if (this.stayTime <= 0 || Utils.rand()) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d4 / d9, d2 / d9)));
                }
            }
            d4 = this.motionX;
            d3 = this.motionY;
            d2 = this.motionZ;
            if (this.stayTime > 0) {
                this.stayTime -= n;
                this.move(0.0, d3, 0.0);
            } else {
                Vector2 vector2 = new Vector2(this.x + d4, this.z + d2);
                this.move(d4, d3, d2);
                Vector2 vector22 = new Vector2(this.x, this.z);
                if (vector2.x != vector22.x || vector2.y != vector22.y) {
                    this.moveTime -= 90;
                }
            }
            if (this.stayTime <= 0) {
                this.motionY = this.isOnGround() ? Utils.rand(0.05, 0.1) : (this.followTarget != null ? Math.min(0.2, Math.max(-0.2, (this.followTarget.x - this.x) / 100.0)) : Utils.rand(-0.05, 0.05));
            }
            this.updateMovement();
            return this.target;
        }
        return null;
    }
}

