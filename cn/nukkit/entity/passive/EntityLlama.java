/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.passive.EntityHorseBase;
import cn.nukkit.entity.projectile.EntityLlamaSpit;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityLlama
extends EntityHorseBase {
    public static final int NETWORK_ID = 29;
    private int C;
    private static final int[] B = new int[]{0, 1, 2, 3};
    private int A;
    private Entity w;

    public EntityLlama(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 29;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.935f;
        }
        return 1.87f;
    }

    @Override
    public boolean canBeSaddled() {
        return false;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(15);
        super.initEntity();
        this.C = this.namedTag.contains("Variant") ? this.namedTag.getInt("Variant") : EntityLlama.b();
        this.setDataProperty(new IntEntityData(2, this.C));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.C);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        Entity entity;
        super.attack(entityDamageEvent);
        if (entityDamageEvent instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player && (((Player)entity).isSurvival() || ((Player)entity).isAdventure()) && this.A <= 0) {
            this.A = 60;
            this.w = entity;
        }
        return true;
    }

    @Override
    public boolean onUpdate(int n) {
        if (!this.closed && this.A > 0) {
            --this.A;
            this.moveTime = 0;
            this.stayTime = 60;
            if (this.w != null) {
                double d2 = this.w.x - this.x;
                double d3 = this.w.z - this.z;
                double d4 = Math.abs(d2) + Math.abs(d3);
                this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(d2 / d4, d3 / d4)));
                if (this.A == 0 && this.distanceSquared(this.w) < 100.0) {
                    double d5 = 2.0;
                    double d6 = this.yaw;
                    double d7 = this.pitch;
                    Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d6)) * Math.cos(FastMathLite.toRadians(d7)) * 0.5, this.y + (double)this.getEyeHeight(), this.z + Math.cos(FastMathLite.toRadians(d6)) * Math.cos(FastMathLite.toRadians(d7)) * 0.5, d6, d7, this.level);
                    Entity entity = Entity.createEntity("LlamaSpit", (Position)location, this);
                    if (entity instanceof EntityLlamaSpit) {
                        EntityLlamaSpit entityLlamaSpit = (EntityLlamaSpit)entity;
                        entityLlamaSpit.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d6)) * Math.cos(FastMathLite.toRadians(d7)) * d5 * d5, -Math.sin(FastMathLite.toRadians(d7)) * d5 * d5, Math.cos(FastMathLite.toRadians(d6)) * Math.cos(FastMathLite.toRadians(d7)) * d5 * d5));
                        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityLlamaSpit);
                        this.server.getPluginManager().callEvent(projectileLaunchEvent);
                        if (projectileLaunchEvent.isCancelled()) {
                            entityLlamaSpit.close();
                        } else {
                            entityLlamaSpit.spawnToAll();
                            this.getLevel().addSound((Vector3)this, Sound.MOB_LLAMA_SPIT);
                        }
                    }
                }
            }
        }
        return super.onUpdate(n);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(334, 0, Utils.rand(0, 2))};
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        boolean bl = super.targetOption(entityCreature, d2);
        if (bl && entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return player.isAlive() && !player.closed && this.isFeedItem(player.getInventory().getItemInHandFast()) && d2 <= 40.0;
        }
        return false;
    }

    @Override
    public boolean isFeedItem(Item item) {
        return item.getId() == 296;
    }

    @Override
    public void onPlayerInput(Player player, double d2, double d3) {
    }

    private static int b() {
        return B[Utils.rand(0, B.length - 1)];
    }

    @Override
    public void onJump(Player player, int n) {
    }
}

