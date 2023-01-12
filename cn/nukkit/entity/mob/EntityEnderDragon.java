/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.item.EntityAreaEffectCloud;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.mob.b;
import cn.nukkit.entity.projectile.EntityEnderCharge;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EntityEnderDragon
extends EntityFlyingMob
implements EntityBoss {
    public static final int NETWORK_ID = 53;
    private static List<b> w;
    private static int D;
    private int E;
    private int B = -1;
    private boolean C = true;
    private boolean F;
    private final Long2IntOpenHashMap A = new Long2IntOpenHashMap();

    @Override
    public int getNetworkId() {
        return 53;
    }

    public EntityEnderDragon(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public float getWidth() {
        return 16.0f;
    }

    @Override
    public float getHeight() {
        return 8.0f;
    }

    @Override
    public double getSpeed() {
        return 3.0;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(200);
        super.initEntity();
        this.fireProof = true;
        this.setDataFlag(0, 50, true);
        this.E = this.namedTag.getInt("flightPathPos");
        if (w == null && this.level.getDimension() == 2 && this.server.suomiCraftPEMode()) {
            w = EntityEnderDragon.b();
        }
        this.F = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    @Override
    public int getKillExperience() {
        if (this.server.suomiCraftPEMode()) {
            this.A.forEach((l, n) -> {
                Entity entity = this.level.getEntity((long)l);
                if (entity instanceof Player) {
                    ((Player)entity).addExperience(Math.min(n, 500));
                }
            });
            this.A.clear();
        } else {
            for (int k = 0; k < 167; ++k) {
                this.level.dropExpOrb(this, 3);
            }
        }
        return 0;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (super.attack(entityDamageEvent)) {
            Entity entity;
            if (entityDamageEvent instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player) {
                long l = entity.getId();
                int n = this.A.containsKey(l) ? this.A.get(l) + (int)entityDamageEvent.getFinalDamage() : (int)entityDamageEvent.getFinalDamage();
                this.A.put(l, n);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return player.spawned && player.isAlive() && !player.closed && (player.isSurvival() || player.isAdventure()) && d2 <= 22500.0;
        }
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay == 200 && Utils.rand(1, 5) < 3 && this.distanceSquared(entity) <= 22500.0) {
            this.attackDelay = 0;
            double d2 = 1.2;
            double d3 = this.target.x - this.x;
            double d4 = this.target.y - this.y;
            double d5 = this.target.z - this.z;
            double d6 = Math.abs(d3) + Math.abs(d5);
            double d7 = FastMathLite.toDegrees(-FastMathLite.atan2(d3 / d6, d5 / d6));
            double d8 = d4 * 2.0;
            EntityEnderCharge entityEnderCharge = (EntityEnderCharge)Entity.createEntity("EnderCharge", (Position)this.getLocation(), this);
            entityEnderCharge.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d7)) * Math.cos(FastMathLite.toRadians(d8)) * d2 * d2, -Math.sin(FastMathLite.toRadians(d8)) * d2 * d2, Math.cos(FastMathLite.toRadians(d7)) * Math.cos(FastMathLite.toRadians(d8)) * d2 * d2));
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityEnderCharge);
            this.server.getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityEnderCharge.close();
            } else {
                entityEnderCharge.spawnToAll();
            }
        }
    }

    @Override
    public boolean entityBaseTick(int n) {
        block2: {
            Block[] blockArray;
            block3: {
                if (this.age % 2 != 0) break block3;
                for (Entity entity : this.level.entities.values()) {
                    float f2;
                    if (!(entity instanceof EntityEndCrystal) || !(entity.distanceSquared(this) <= 32.0) || (f2 = this.getHealth()) > (float)this.getRealMaxHealth() || f2 == 0.0f) continue;
                    this.setHealth(f2 + 0.2f);
                    break block2;
                }
                break block2;
            }
            if (!this.F || !this.isCollided || this.B != -1) break block2;
            for (Block block : blockArray = this.level.getCollisionBlocks(this.getBoundingBox().grow(0.1, 0.1, 0.1))) {
                int n2 = block.getId();
                if (n2 == 7 || n2 == 49 || n2 == 121 || n2 == 101 || n2 == 119 || n2 == 120 || n2 == 209 || n2 == 252 || n2 == 137 || n2 == 189 || n2 == 188 || n2 == 122) continue;
                this.level.setBlockAt((int)block.x, (int)block.y, (int)block.z, 0, 0);
            }
        }
        return super.entityBaseTick(n);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Ender Dragon";
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.type = 53;
        addEntityPacket.entityUniqueId = this.getId();
        addEntityPacket.entityRuntimeId = this.getId();
        addEntityPacket.yaw = (float)this.yaw;
        addEntityPacket.headYaw = (float)this.yaw;
        addEntityPacket.pitch = (float)this.pitch;
        addEntityPacket.x = (float)this.x;
        addEntityPacket.y = (float)this.y;
        addEntityPacket.z = (float)this.z;
        addEntityPacket.speedX = (float)this.motionX;
        addEntityPacket.speedY = (float)this.motionY;
        addEntityPacket.speedZ = (float)this.motionZ;
        addEntityPacket.metadata = this.dataProperties.clone();
        addEntityPacket.attributes = new Attribute[]{Attribute.getAttribute(4).setMaxValue(200.0f).setValue(200.0f)};
        return addEntityPacket;
    }

    @Override
    public Vector3 updateMove(int n) {
        if (this.isMovement() && !this.isImmobile() && w != null) {
            int n2;
            if (this.E > D) {
                if (this.B == -1) {
                    this.B = Utils.rand(100, 300);
                    if (this.target instanceof Player || this.followTarget instanceof Player) {
                        this.attackDelay = 0;
                        this.c();
                    }
                } else if (this.B > 0) {
                    --this.B;
                } else {
                    this.B = -1;
                    this.E = 0;
                }
                return null;
            }
            if (this.C) {
                int n3 = this.E;
                n2 = n3;
                this.E = n3 + 1;
            } else {
                n2 = this.E;
            }
            b b2 = w.get(n2);
            boolean bl = this.C = !this.C;
            if (b2.c) {
                this.motionX = b2.e / 2.0;
                this.motionY = b2.f / 2.0;
                this.motionZ = b2.a / 2.0;
            } else {
                this.yaw = b2.b;
                this.headYaw = b2.d;
                this.move((b2.e - this.x) / 2.0, (b2.f - this.y) / 2.0, (b2.a - this.z) / 2.0);
            }
            this.updateMovement();
            this.checkTarget();
            return this.target;
        }
        return null;
    }

    private void c() {
        EntityAreaEffectCloud entityAreaEffectCloud = (EntityAreaEffectCloud)Entity.createEntity("AreaEffectCloud", this.getChunk(), new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", -9.0)).add(new DoubleTag("", 63.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putShort("PotionId", 24).putFloat("InitialRadius", 5.0f).putInt("ParticleColor", 8339378), new Object[0]);
        Effect effect = Potion.getEffect(24, true);
        if (effect != null && entityAreaEffectCloud != null) {
            entityAreaEffectCloud.cloudEffects.add(effect.setDuration(1).setVisible(false).setAmbient(false));
            entityAreaEffectCloud.spawnToAll();
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("flightPathPos", this.E);
    }

    private static List<b> b() {
        try {
            String string;
            ArrayList<b> arrayList = new ArrayList<b>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Server.class.getClassLoader().getResourceAsStream("dragon_path.txt")));
            while ((string = bufferedReader.readLine()) != null) {
                String[] stringArray;
                if (string.startsWith("MoveEntityDeltaPacket(")) {
                    stringArray = string.split("\\(");
                    String[] stringArray2 = stringArray[3].replace("), rotation=", "").split(", ");
                    String[] stringArray3 = stringArray[4].replace("))", "").split(", ");
                    arrayList.add(new b(false, Double.parseDouble(stringArray2[0]), Double.parseDouble(stringArray2[1]), Double.parseDouble(stringArray2[2]), Double.parseDouble(stringArray3[1]), Double.parseDouble(stringArray3[2]), null));
                    continue;
                }
                if (!string.startsWith("SetEntityMotionPacket(")) continue;
                stringArray = string.split("\\(")[2].replace("))", "").split(", ");
                arrayList.add(new b(true, Double.parseDouble(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]), 0.0, 0.0, null));
            }
            bufferedReader.close();
            D = arrayList.size() - 1;
            return arrayList;
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().error("Failed to load dragon's flight path", exception);
            return null;
        }
    }

    @Override
    public void knockBack(Entity entity, double d2, double d3, double d4, double d5) {
    }

    public AxisAlignedBB[] getCollisionCubes(AxisAlignedBB axisAlignedBB) {
        int n = NukkitMath.floorDouble(axisAlignedBB.minX);
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
        int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
        int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
        int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
        int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
        ArrayList<AxisAlignedBB> arrayList = new ArrayList<AxisAlignedBB>();
        for (int k = n3; k <= n6; ++k) {
            for (int i2 = n; i2 <= n4; ++i2) {
                for (int i3 = n2; i3 <= n5; ++i3) {
                    Block block = this.level.getBlock(this.chunk, i2, i3, k, false);
                    if (block.getId() == 49 || block.getId() == 7 || block.canPassThrough() || !block.collidesWithBB(axisAlignedBB)) continue;
                    arrayList.add(block.getBoundingBox());
                }
            }
        }
        return arrayList.toArray(new AxisAlignedBB[0]);
    }

    @Override
    public boolean move(double d2, double d3, double d4) {
        AxisAlignedBB[] axisAlignedBBArray;
        if (d3 < -10.0 || d3 > 10.0) {
            return false;
        }
        if (d2 == 0.0 && d4 == 0.0 && d3 == 0.0) {
            return false;
        }
        if (Timings.entityMoveTimer != null) {
            Timings.entityMoveTimer.startTiming();
        }
        this.blocksAround = null;
        double d5 = d2 * (double)this.moveMultiplier;
        double d6 = d3;
        double d7 = d4 * (double)this.moveMultiplier;
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray = this.getCollisionCubes(this.boundingBox.addCoord(d2, d3, d4))) {
            d2 = axisAlignedBB.calculateXOffset(this.boundingBox, d2);
        }
        this.boundingBox.offset(d2, 0.0, 0.0);
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray) {
            d4 = axisAlignedBB.calculateZOffset(this.boundingBox, d4);
        }
        this.boundingBox.offset(0.0, 0.0, d4);
        for (AxisAlignedBB axisAlignedBB : axisAlignedBBArray) {
            d3 = axisAlignedBB.calculateYOffset(this.boundingBox, d3);
        }
        this.boundingBox.offset(0.0, d3, 0.0);
        this.setComponents(this.x + d2, this.y + d3, this.z + d4);
        this.checkChunks();
        this.checkGroundState(d5, d6, d7, d2, d3, d4);
        this.updateFallState(this.onGround);
        if (Timings.entityMoveTimer != null) {
            Timings.entityMoveTimer.stopTiming();
        }
        return true;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

