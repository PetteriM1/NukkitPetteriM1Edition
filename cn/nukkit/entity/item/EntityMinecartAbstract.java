/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.block.BlockRailActivator;
import cn.nukkit.block.BlockRailPowered;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityControllable;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.entity.item.b;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.vehicle.VehicleMoveEvent;
import cn.nukkit.event.vehicle.VehicleUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;
import cn.nukkit.utils.Rail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public abstract class EntityMinecartAbstract
extends EntityVehicle
implements EntityControllable {
    private String w;
    private static final int[][][] C = new int[][][]{new int[][]{{0, 0, -1}, {0, 0, 1}}, new int[][]{{-1, 0, 0}, {1, 0, 0}}, new int[][]{{-1, -1, 0}, {1, 0, 0}}, new int[][]{{-1, 0, 0}, {1, -1, 0}}, new int[][]{{0, 0, -1}, {0, -1, 1}}, new int[][]{{0, -1, -1}, {0, 0, 1}}, new int[][]{{0, 0, 1}, {1, 0, 0}}, new int[][]{{0, 0, 1}, {-1, 0, 0}}, new int[][]{{0, 0, -1}, {-1, 0, 0}}, new int[][]{{0, 0, -1}, {1, 0, 0}}};
    private double p = 0.0;
    private Block s;
    private boolean v = true;
    private double t = 0.5;
    private double r = 0.5;
    private double n = 0.5;
    private double B = 0.95;
    private double u = 0.95;
    private double q = 0.95;
    private double o = 0.4;
    private boolean A = false;

    public abstract MinecartType getType();

    public abstract boolean isRideable();

    public EntityMinecartAbstract(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public float getHeight() {
        return 0.7f;
    }

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    public void setName(String string) {
        this.w = string;
    }

    @Override
    public String getName() {
        return this.w;
    }

    @Override
    public float getBaseOffset() {
        return 0.35f;
    }

    @Override
    public boolean hasCustomName() {
        return this.w != null;
    }

    @Override
    public boolean canDoInteraction() {
        return this.passengers.isEmpty() && this.s == null;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();
        this.setHealth(40.0f);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            this.despawnFromAll();
            this.close();
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0) {
            return false;
        }
        this.lastUpdate = n;
        if (this.isAlive()) {
            Block block;
            int n3;
            int n4;
            super.onUpdate(n);
            if (this.getHealth() < 20.0f) {
                this.setHealth(this.getHealth() + 1.0f);
            }
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.motionY -= 0.04;
            int n5 = MathHelper.floor(this.x);
            if (Rail.isRailBlock(this.level.getBlockIdAt(n5, (n4 = MathHelper.floor(this.y)) - 1, n3 = MathHelper.floor(this.z)))) {
                --n4;
            }
            if (Rail.isRailBlock(block = this.level.getBlock(this.chunk, n5, n4, n3, true))) {
                this.a(n5, n4, n3, (BlockRail)block);
                if (block instanceof BlockRailActivator && ((BlockRailActivator)block).isActive()) {
                    this.activate(n5, n4, n3, (block.getDamage() & 8) != 0);
                }
            } else {
                this.c();
            }
            this.checkBlockCollision();
            this.pitch = 0.0;
            double d2 = this.lastX - this.x;
            double d3 = this.lastZ - this.z;
            double d4 = this.yaw;
            if (d2 * d2 + d3 * d3 > 0.001) {
                d4 = FastMathLite.atan2(d3, d2) * 180.0 / Math.PI;
            }
            if (d4 < 0.0) {
                d4 -= d4 - d4;
            }
            this.setRotation(d4, this.pitch);
            Location location = new Location(this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch, this.level);
            Location location2 = new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.level);
            if (!this.getServer().suomiCraftPEMode()) {
                this.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(this));
            }
            if (!location.equals(location2)) {
                this.getServer().getPluginManager().callEvent(new VehicleMoveEvent(this, location, location2));
            }
            for (Entity entity : this.level.getNearbyEntities(this.boundingBox.grow(0.2, 0.0, 0.2), this)) {
                if (this.passengers.contains(entity) || !(entity instanceof EntityMinecartAbstract)) continue;
                entity.applyEntityCollision(this);
            }
            Iterator iterator = this.passengers.iterator();
            while (iterator.hasNext()) {
                Entity entity = (Entity)iterator.next();
                if (entity.isAlive()) continue;
                if (entity.riding == this) {
                    entity.riding = null;
                }
                iterator.remove();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.invulnerable) {
            return false;
        }
        entityDamageEvent.setDamage(entityDamageEvent.getDamage() * 15.0f);
        boolean bl = super.attack(entityDamageEvent);
        if (this.isAlive()) {
            this.performHurtAnimation();
        }
        return bl;
    }

    public void dropItem() {
        Entity entity;
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player && ((Player)entity).isCreative()) {
            return;
        }
        this.level.dropItem(this, Item.get(328));
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }
        super.kill();
        if (this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
            this.dropItem();
        }
    }

    @Override
    public void close() {
        super.close();
        for (Entity entity : new ArrayList(this.passengers)) {
            this.dismountEntity(entity);
        }
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (!this.isRideable()) {
            return false;
        }
        if (!this.passengers.isEmpty()) {
            return false;
        }
        if (this.s == null) {
            this.mountEntity(player);
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        double d2;
        double d3;
        double d4;
        if (entity != this.riding && (!(entity instanceof Player) || ((Player)entity).getGamemode() != 3) && (d4 = (d3 = entity.x - this.x) * d3 + (d2 = entity.z - this.z) * d2) >= (double)1.0E-4f) {
            d4 = Math.sqrt(d4);
            d3 /= d4;
            d2 /= d4;
            double d5 = 1.0 / d4;
            if (d5 > 1.0) {
                d5 = 1.0;
            }
            d3 *= d5;
            d2 *= d5;
            d3 *= (double)0.1f;
            d2 *= (double)0.1f;
            d3 *= 1.0 + this.entityCollisionReduction;
            d2 *= 1.0 + this.entityCollisionReduction;
            d3 *= 0.5;
            d2 *= 0.5;
            if (entity instanceof EntityMinecartAbstract) {
                Vector3 vector3;
                EntityMinecartAbstract entityMinecartAbstract = (EntityMinecartAbstract)entity;
                double d6 = entityMinecartAbstract.x - this.x;
                double d7 = entityMinecartAbstract.z - this.z;
                Vector3 vector32 = new Vector3(d6, 0.0, d7).normalize();
                double d8 = Math.abs(vector32.dot(vector3 = new Vector3(MathHelper.cos((float)this.yaw * ((float)Math.PI / 180)), 0.0, MathHelper.sin((float)this.yaw * ((float)Math.PI / 180))).normalize()));
                if (d8 < (double)0.8f) {
                    return;
                }
                double d9 = entityMinecartAbstract.motionX + this.motionX;
                double d10 = entityMinecartAbstract.motionZ + this.motionZ;
                if (entityMinecartAbstract.getType().getId() == 2 && this.getType().getId() != 2) {
                    this.motionX *= (double)0.2f;
                    this.motionZ *= (double)0.2f;
                    this.motionX += entityMinecartAbstract.motionX - d3;
                    this.motionZ += entityMinecartAbstract.motionZ - d2;
                    entityMinecartAbstract.motionX *= (double)0.95f;
                    entityMinecartAbstract.motionZ *= (double)0.95f;
                } else if (entityMinecartAbstract.getType().getId() != 2 && this.getType().getId() == 2) {
                    entityMinecartAbstract.motionX *= (double)0.2f;
                    entityMinecartAbstract.motionZ *= (double)0.2f;
                    this.motionX += entityMinecartAbstract.motionX + d3;
                    this.motionZ += entityMinecartAbstract.motionZ + d2;
                    this.motionX *= (double)0.95f;
                    this.motionZ *= (double)0.95f;
                } else {
                    this.motionX *= (double)0.2f;
                    this.motionZ *= (double)0.2f;
                    this.motionX += (d9 /= 2.0) - d3;
                    this.motionZ += (d10 /= 2.0) - d2;
                    entityMinecartAbstract.motionX *= (double)0.2f;
                    entityMinecartAbstract.motionZ *= (double)0.2f;
                    entityMinecartAbstract.motionX += d9 + d3;
                    entityMinecartAbstract.motionZ += d10 + d2;
                }
            } else {
                this.motionX -= d3;
                this.motionZ -= d2;
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.b();
    }

    public double getMaxSpeed() {
        return this.o;
    }

    protected void activate(int n, int n2, int n3, boolean bl) {
    }

    private void c() {
        this.motionX = NukkitMath.clamp(this.motionX, -this.o, this.o);
        this.motionZ = NukkitMath.clamp(this.motionZ, -this.o, this.o);
        if (!this.A) {
            for (Entity entity : this.passengers) {
                entity.setSeatPosition(this.getMountedOffset(entity).add(0.0f, 0.35f));
                this.updatePassengerPosition(entity);
            }
            this.A = true;
        }
        if (this.onGround) {
            this.motionX *= this.t;
            this.motionY *= this.r;
            this.motionZ *= this.n;
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= this.B;
            this.motionY *= this.u;
            this.motionZ *= this.q;
        }
    }

    private void a(int n, int n2, int n3, BlockRail blockRail) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        this.fallDistance = 0.0f;
        Vector3 vector3 = this.a(this.x, this.y, this.z);
        this.y = n2;
        boolean bl = false;
        boolean bl2 = false;
        if (blockRail instanceof BlockRailPowered) {
            bl = blockRail.isActive();
            bl2 = !blockRail.isActive();
        }
        switch (b.a[Rail.Orientation.byMetadata(blockRail.getRealMeta()).ordinal()]) {
            case 1: {
                this.motionX -= 0.0078125;
                this.y += 1.0;
                break;
            }
            case 2: {
                this.motionX += 0.0078125;
                this.y += 1.0;
                break;
            }
            case 3: {
                this.motionZ += 0.0078125;
                this.y += 1.0;
                break;
            }
            case 4: {
                this.motionZ -= 0.0078125;
                this.y += 1.0;
            }
        }
        int[][] nArray = C[blockRail.getRealMeta()];
        double d9 = nArray[1][0] - nArray[0][0];
        double d10 = nArray[1][2] - nArray[0][2];
        double d11 = Math.sqrt(d9 * d9 + d10 * d10);
        double d12 = this.motionX * d9 + this.motionZ * d10;
        if (d12 < 0.0) {
            d9 = -d9;
            d10 = -d10;
        }
        if ((d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 2.0) {
            d8 = 2.0;
        }
        this.motionX = d8 * d9 / d11;
        this.motionZ = d8 * d10 / d11;
        Entity entity = this.getPassenger();
        if (entity instanceof EntityLiving && (d7 = this.p) > 0.0) {
            d6 = -Math.sin(entity.yaw * Math.PI / 180.0);
            d5 = Math.cos(entity.yaw * Math.PI / 180.0);
            d4 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (d4 < 0.01) {
                this.motionX += d6 * 0.1;
                this.motionZ += d5 * 0.1;
                bl2 = false;
            }
        }
        if (bl2) {
            d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d7 < 0.03) {
                this.motionX *= 0.0;
                this.motionY *= 0.0;
                this.motionZ *= 0.0;
            } else {
                this.motionX *= 0.5;
                this.motionY *= 0.0;
                this.motionZ *= 0.5;
            }
        }
        d6 = (double)n + 0.5 + (double)nArray[0][0] * 0.5;
        d5 = (double)n3 + 0.5 + (double)nArray[0][2] * 0.5;
        d4 = (double)n + 0.5 + (double)nArray[1][0] * 0.5;
        double d13 = (double)n3 + 0.5 + (double)nArray[1][2] * 0.5;
        d9 = d4 - d6;
        d10 = d13 - d5;
        if (d9 == 0.0) {
            this.x = (double)n + 0.5;
            d7 = this.z - (double)n3;
        } else if (d10 == 0.0) {
            this.z = (double)n3 + 0.5;
            d7 = this.x - (double)n;
        } else {
            d3 = this.x - d6;
            d2 = this.z - d5;
            d7 = (d3 * d9 + d2 * d10) * 2.0;
        }
        this.x = d6 + d9 * d7;
        this.z = d5 + d10 * d7;
        this.setPosition(new Vector3(this.x, this.y, this.z));
        d3 = this.motionX;
        d2 = this.motionZ;
        if (!this.passengers.isEmpty()) {
            d3 *= 0.75;
            d2 *= 0.75;
        }
        d3 = NukkitMath.clamp(d3, -this.o, this.o);
        d2 = NukkitMath.clamp(d2, -this.o, this.o);
        this.move(d3, 0.0, d2);
        if (nArray[0][1] != 0 && MathHelper.floor(this.x) - n == nArray[0][0] && MathHelper.floor(this.z) - n3 == nArray[0][2]) {
            this.setPosition(new Vector3(this.x, this.y + (double)nArray[0][1], this.z));
        } else if (nArray[1][1] != 0 && MathHelper.floor(this.x) - n == nArray[1][0] && MathHelper.floor(this.z) - n3 == nArray[1][2]) {
            this.setPosition(new Vector3(this.x, this.y + (double)nArray[1][1], this.z));
        }
        this.a();
        Vector3 vector32 = this.a(this.x, this.y, this.z);
        if (vector32 != null && vector3 != null) {
            double d14 = (vector3.y - vector32.y) * 0.05;
            d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d8 > 0.0) {
                this.motionX = this.motionX / d8 * (d8 + d14);
                this.motionZ = this.motionZ / d8 * (d8 + d14);
            }
            this.setPosition(new Vector3(this.x, vector32.y, this.z));
        }
        int n4 = MathHelper.floor(this.x);
        int n5 = MathHelper.floor(this.z);
        if (n4 != n || n5 != n3) {
            d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d8 * (double)(n4 - n);
            this.motionZ = d8 * (double)(n5 - n3);
        }
        if (bl) {
            double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d15 > 0.01) {
                double d16 = 0.06;
                this.motionX += this.motionX / d15 * d16;
                this.motionZ += this.motionZ / d15 * d16;
            } else if (blockRail.getOrientation() == Rail.Orientation.STRAIGHT_NORTH_SOUTH) {
                if (this.level.getBlock(this.chunk, n - 1, n2, n3, true).isNormalBlock()) {
                    this.motionX = 0.02;
                } else if (this.level.getBlock(this.chunk, n + 1, n2, n3, true).isNormalBlock()) {
                    this.motionX = -0.02;
                }
            } else if (blockRail.getOrientation() == Rail.Orientation.STRAIGHT_EAST_WEST) {
                if (this.level.getBlock(this.chunk, n, n2, n3 - 1, true).isNormalBlock()) {
                    this.motionZ = 0.02;
                } else if (this.level.getBlock(this.chunk, n, n2, n3 + 1, true).isNormalBlock()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }

    private void a() {
        if (!this.passengers.isEmpty() || !this.v) {
            this.motionX *= (double)0.997f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.997f;
        } else {
            this.motionX *= (double)0.96f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.96f;
        }
    }

    private Vector3 a(double d2, double d3, double d4) {
        Block block;
        int n;
        int n2;
        int n3 = MathHelper.floor(d2);
        if (Rail.isRailBlock(this.level.getBlockIdAt(n3, (n2 = MathHelper.floor(d3)) - 1, n = MathHelper.floor(d4)))) {
            --n2;
        }
        if (Rail.isRailBlock(block = this.level.getBlock(this.chunk, n3, n2, n, true))) {
            double d5;
            int[][] nArray = C[((BlockRail)block).getRealMeta()];
            double d6 = (double)n3 + 0.5 + (double)nArray[0][0] * 0.5;
            double d7 = (double)n2 + 0.5 + (double)nArray[0][1] * 0.5;
            double d8 = (double)n + 0.5 + (double)nArray[0][2] * 0.5;
            double d9 = (double)n3 + 0.5 + (double)nArray[1][0] * 0.5;
            double d10 = (double)n2 + 0.5 + (double)nArray[1][1] * 0.5;
            double d11 = (double)n + 0.5 + (double)nArray[1][2] * 0.5;
            double d12 = d9 - d6;
            double d13 = (d10 - d7) * 2.0;
            double d14 = d11 - d8;
            if (d12 == 0.0) {
                d5 = d4 - (double)n;
            } else if (d14 == 0.0) {
                d5 = d2 - (double)n3;
            } else {
                double d15 = d2 - d6;
                double d16 = d4 - d8;
                d5 = (d15 * d12 + d16 * d14) * 2.0;
            }
            d2 = d6 + d12 * d5;
            d3 = d7 + d13 * d5;
            d4 = d8 + d14 * d5;
            if (d13 < 0.0) {
                d3 += 1.0;
            }
            if (d13 > 0.0) {
                d3 += 0.5;
            }
            return new Vector3(d2, d3, d4);
        }
        return null;
    }

    @Override
    public void onPlayerInput(Player player, double d2, double d3) {
        this.setCurrentSpeed(d3);
    }

    public void setCurrentSpeed(double d2) {
        this.p = d2;
    }

    private void b() {
        boolean bl = super.getDataPropertyByte(18) == 1 || this.s != null;
        this.namedTag.putBoolean("CustomDisplayTile", bl);
        if (bl) {
            int n = this.s.getId() | this.s.getDamage() << 16;
            int n2 = this.getDataPropertyInt(17);
            this.namedTag.putInt("DisplayTile", n);
            this.namedTag.putInt("DisplayOffset", n2);
        }
    }

    public boolean setDisplayBlock(Block block) {
        return this.setDisplayBlock(block, true);
    }

    public boolean setDisplayBlock(Block block, boolean bl) {
        if (!bl) {
            this.s = block.isNormalBlock() ? block : null;
            return true;
        }
        if (block != null) {
            if (block.isNormalBlock()) {
                this.s = block;
                int n = this.s.getId() | this.s.getDamage() << 16;
                this.setDataProperty(new ByteEntityData(18, 1));
                this.setDataProperty(new IntEntityData(16, n));
                this.setDisplayBlockOffset(6);
            }
        } else {
            this.s = null;
            this.setDataProperty(new ByteEntityData(18, 0));
            this.setDataProperty(new IntEntityData(16, 0));
            this.setDisplayBlockOffset(0);
        }
        return true;
    }

    public Block getDisplayBlock() {
        return this.s;
    }

    public void setDisplayBlockOffset(int n) {
        this.setDataProperty(new IntEntityData(17, n));
    }

    public int getDisplayBlockOffset() {
        return super.getDataPropertyInt(17);
    }

    public boolean isSlowWhenEmpty() {
        return this.v;
    }

    public void setSlowWhenEmpty(boolean bl) {
        this.v = bl;
    }

    public Vector3 getFlyingVelocityMod() {
        return new Vector3(this.B, this.u, this.q);
    }

    public void setFlyingVelocityMod(Vector3 vector3) {
        Objects.requireNonNull(vector3, "Flying velocity modifiers cannot be null");
        this.B = vector3.getX();
        this.u = vector3.getY();
        this.q = vector3.getZ();
    }

    public Vector3 getDerailedVelocityMod() {
        return new Vector3(this.t, this.r, this.n);
    }

    public void setDerailedVelocityMod(Vector3 vector3) {
        Objects.requireNonNull(vector3, "Derailed velocity modifiers cannot be null");
        this.t = vector3.getX();
        this.r = vector3.getY();
        this.n = vector3.getZ();
    }

    public void setMaximumSpeed(double d2) {
        this.o = d2;
    }

    @Override
    public boolean goToNewChunk(FullChunk fullChunk) {
        if (fullChunk.getEntities().size() > 200) {
            if (!this.isClosed() && this.isAlive() && this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                this.dropItem();
            }
            this.close();
            return false;
        }
        return true;
    }

    @Override
    public String getInteractButtonText() {
        return "";
    }
}

