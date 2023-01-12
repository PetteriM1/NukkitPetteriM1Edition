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

public abstract class EntitySwimming
extends BaseEntity {
    public EntitySwimming(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
            return;
        }
        Vector3 vector3 = this.target;
        if (!(vector3 instanceof EntityCreature) || !((EntityCreature)vector3).closed && !this.targetOption((EntityCreature)vector3, this.distanceSquared(vector3)) || !((Entity)vector3).canBeFollowed()) {
            double d2 = 2.147483647E9;
            for (Entity entity : this.getLevel().getEntities()) {
                double d3;
                EntityCreature entityCreature;
                if (entity == this || !(entity instanceof EntityCreature) || entity.closed || !this.canTarget(entity) || (entityCreature = (EntityCreature)entity) instanceof BaseEntity && ((BaseEntity)entityCreature).isFriendly() == this.isFriendly() || (d3 = this.distanceSquared(entityCreature)) > d2 || !this.targetOption(entityCreature, d3)) continue;
                d2 = d3;
                this.stayTime = 0;
                this.moveTime = 0;
                this.target = entityCreature;
            }
        }
        if (this.target instanceof EntityCreature && !((EntityCreature)this.target).closed && ((EntityCreature)this.target).isAlive() && this.targetOption((EntityCreature)this.target, this.distanceSquared(this.target))) {
            return;
        }
        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) > 5) {
                return;
            }
            int n = Utils.rand(10, 30);
            int n2 = Utils.rand(10, 30);
            this.target = this.add(Utils.rand() ? (double)n : (double)(-n), Utils.rand(-20.0, 20.0) / 10.0, Utils.rand() ? (double)n2 : (double)(-n2));
        } else if (Utils.rand(1, 100) == 1) {
            int n = Utils.rand(10, 30);
            int n3 = Utils.rand(10, 30);
            this.stayTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? (double)n : (double)(-n), Utils.rand(-20.0, 20.0) / 10.0, Utils.rand() ? (double)n3 : (double)(-n3));
        } else if (this.moveTime <= 0 || this.target == null) {
            int n = Utils.rand(20, 100);
            int n4 = Utils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = Utils.rand(100, 200);
            this.target = this.add(Utils.rand() ? (double)n : (double)(-n), 0.0, Utils.rand() ? (double)n4 : (double)(-n4));
        }
    }

    @Override
    public Vector3 updateMove(int n) {
        if (this.isMovement() && !this.isImmobile()) {
            double d2;
            double d3;
            if (this.isKnockback()) {
                this.move(this.motionX, this.motionY, this.motionZ);
                this.motionY -= (double)this.getGravity();
                this.updateMovement();
                return null;
            }
            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
                double d4 = this.followTarget.x - this.x;
                double d5 = this.followTarget.z - this.z;
                double d6 = Math.abs(d4) + Math.abs(d5);
                if (this.stayTime > 0 || this.distance(this.followTarget) <= (double)(this.getWidth() / 2.0f) + 0.05) {
                    this.motionX = 0.0;
                    this.motionZ = 0.0;
                } else {
                    this.motionX = this.getSpeed() * 0.1 * (d4 / d6);
                    this.motionZ = this.getSpeed() * 0.1 * (d5 / d6);
                }
                if (this.stayTime <= 0 || Utils.rand()) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d4 / d6, d5 / d6)));
                }
                return this.followTarget;
            }
            Vector3 vector3 = this.target;
            this.checkTarget();
            if (this.target instanceof EntityCreature || vector3 != this.target) {
                d3 = this.target.x - this.x;
                d2 = this.target.z - this.z;
                double d7 = Math.abs(d3) + Math.abs(d2);
                if (this.stayTime > 0 || this.distance(this.target) <= ((double)(this.getWidth() / 2.0f) + 0.05) * (double)this.nearbyDistanceMultiplier()) {
                    this.motionX = 0.0;
                    this.motionZ = 0.0;
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (d3 / d7);
                    this.motionZ = this.getSpeed() * 0.15 * (d2 / d7);
                }
                if ((this.stayTime <= 0 || Utils.rand()) && (this.stayTime <= 0 || Utils.rand())) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d3 / d7, d2 / d7)));
                }
            }
            d3 = this.motionX;
            d2 = this.motionZ;
            this.motionY = this.isInsideOfWater() && (this.motionX > 0.0 || this.motionZ > 0.0) ? Utils.rand(-0.12, 0.12) : (!this.isOnGround() && !this.isInsideOfWater() ? (this.motionY -= (double)this.getGravity()) : 0.0);
            if (this.stayTime > 0) {
                this.stayTime -= n;
                this.move(0.0, this.motionY, 0.0);
            } else {
                Vector2 vector2 = new Vector2(this.x + d3, this.z + d2);
                this.move(d3, this.motionY, d2);
                Vector2 vector22 = new Vector2(this.x, this.z);
                if (vector2.x != vector22.x || vector2.y != vector22.y) {
                    this.moveTime -= 90;
                }
            }
            this.updateMovement();
            return this.target;
        }
        return null;
    }
}

