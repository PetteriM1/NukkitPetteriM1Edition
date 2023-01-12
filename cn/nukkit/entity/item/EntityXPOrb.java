/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.List;

public class EntityXPOrb
extends Entity {
    public static final int NETWORK_ID = 69;
    public static final int[] ORB_SPLIT_SIZES = new int[]{2477, 1237, 617, 307, 149, 73, 37, 17, 7, 3, 1};
    public Player closestPlayer = null;
    private int k;
    private int l;

    public EntityXPOrb(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    public static int getMaxOrbSize(int n) {
        for (int n2 : ORB_SPLIT_SIZES) {
            if (n < n2) continue;
            return n2;
        }
        return 1;
    }

    public static List<Integer> splitIntoOrbSizes(int n) {
        IntArrayList intArrayList = new IntArrayList();
        while (n > 0) {
            int n2 = EntityXPOrb.getMaxOrbSize(n);
            intArrayList.add(Integer.valueOf(n2));
            n -= n2;
        }
        return intArrayList;
    }

    @Override
    public int getNetworkId() {
        return 69;
    }

    @Override
    public float getWidth() {
        return 0.1f;
    }

    @Override
    public float getLength() {
        return 0.1f;
    }

    @Override
    public float getHeight() {
        return 0.1f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    public boolean canCollide() {
        return false;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(5);
        super.initEntity();
        if (this.namedTag.contains("Health")) {
            this.setHealth(this.namedTag.getShort("Health"));
        } else {
            this.setHealth(5.0f);
        }
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }
        if (this.namedTag.contains("PickupDelay")) {
            this.k = this.namedTag.getShort("PickupDelay");
        }
        if (this.namedTag.contains("Value")) {
            this.l = this.namedTag.getShort("Value");
        }
        if (this.l <= 0) {
            this.l = 1;
        }
        this.dataProperties.putInt(15, this.l);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) && !this.isInsideOfWater()) && super.attack(entityDamageEvent);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = n;
        if (this.age > 6000) {
            this.close();
            return false;
        }
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            if (this.k > 0) {
                this.k -= n2;
                if (this.k < 0) {
                    this.k = 0;
                }
            }
            if (!this.isOnGround()) {
                this.motionY -= (double)this.getGravity();
            }
            if (this.closestPlayer == null || this.closestPlayer.distanceSquared(this) > 64.0) {
                for (Player player : this.getViewers().values()) {
                    if (player == this.closestPlayer || player.isSpectator() || !(player.distanceSquared(this) <= 64.0)) continue;
                    this.closestPlayer = player;
                    break;
                }
            }
            if (this.closestPlayer != null && (this.closestPlayer.isSpectator() || !this.closestPlayer.canPickupXP())) {
                this.closestPlayer = null;
            }
            if (this.closestPlayer != null && (d6 = 1.0 - (d5 = Math.sqrt((d4 = (this.closestPlayer.x - this.x) / 8.0) * d4 + (d3 = (this.closestPlayer.y + (double)this.closestPlayer.getEyeHeight() / 2.0 - this.y) / 8.0) * d3 + (d2 = (this.closestPlayer.z - this.z) / 8.0) * d2))) > 0.0) {
                d6 *= d6;
                this.motionX += d4 / d5 * d6 * 0.1;
                this.motionY += d3 / d5 * d6 * 0.1;
                this.motionZ += d2 / d5 * d6 * 0.1;
            }
            this.move(this.motionX, this.motionY, this.motionZ);
            double d7 = 1.0 - (double)this.getDrag();
            if (this.onGround && (Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5)) {
                d7 = this.getLevel().getBlock(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ(), false).getFrictionFactor() * d7;
            }
            this.motionX *= d7;
            this.motionY *= (double)(1.0f - this.getDrag());
            this.motionZ *= d7;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            this.updateMovement();
        }
        return bl || !this.onGround || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putShort("Health", (int)this.getHealth());
        this.namedTag.putShort("Age", this.age);
        this.namedTag.putShort("PickupDelay", this.k);
        this.namedTag.putShort("Value", this.l);
    }

    public int getExp() {
        return this.l;
    }

    public void setExp(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("XP amount must be greater than 0, got " + n);
        }
        this.l = n;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    public int getPickupDelay() {
        return this.k;
    }

    public void setPickupDelay(int n) {
        this.k = n;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

