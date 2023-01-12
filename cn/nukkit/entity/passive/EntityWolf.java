/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.mob.EntityTameableMob;
import cn.nukkit.entity.passive.EntityFox;
import cn.nukkit.entity.passive.EntityRabbit;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.entity.passive.EntityTurtle;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Utils;
import java.rmi.server.Skeleton;
import java.util.HashMap;

public class EntityWolf
extends EntityTameableMob {
    public static final int NETWORK_ID = 14;
    private static final String I;
    private static final String F;
    private boolean D;
    private int H;
    private DyeColor G = DyeColor.RED;
    private int C = -1;
    private final Vector3 E = new Vector3();

    public EntityWolf(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 14;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    protected void initEntity() {
        this.setFriendly(true);
        this.setMaxHealth(8);
        super.initEntity();
        if (this.namedTag.contains("Angry") && this.namedTag.getByte("Angry") == 1) {
            this.setAngry(true);
        }
        if (this.namedTag.contains("CollarColor")) {
            this.G = DyeColor.getByDyeData(this.namedTag.getByte("CollarColor"));
            if (this.G == null) {
                this.G = DyeColor.RED;
            }
        }
        this.setDamage(new int[]{0, 3, 4, 6});
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Angry", this.D);
        this.namedTag.putByte("CollarColor", this.G.getDyeData());
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (!entityCreature.isAlive() || entityCreature.closed || d2 > 256.0) {
            return false;
        }
        if (this.isAngry() && this.isAngryTo == entityCreature.getId()) {
            return true;
        }
        if (entityCreature instanceof Player) {
            if (d2 <= 64.0 && this.isBeggingItem(((Player)entityCreature).getInventory().getItemInHandFast())) {
                if (d2 <= 9.0) {
                    this.stayTime = 40;
                }
                return true;
            }
            if (this.hasOwner() && entityCreature.equals(this.getOwner())) {
                if (d2 <= 4.0) {
                    return false;
                }
                if (d2 <= 100.0) {
                    return true;
                }
            }
        }
        if (!this.hasOwner() && d2 <= 256.0 && (entityCreature instanceof Skeleton && !entityCreature.isInsideOfWater() || entityCreature instanceof EntitySheep || entityCreature instanceof EntityRabbit || entityCreature instanceof EntityFox || entityCreature instanceof EntityTurtle && ((EntityTurtle)entityCreature).isBaby() && !entityCreature.isInsideOfWater())) {
            this.isAngryTo = entityCreature.getId();
            this.setAngry(true);
            return true;
        }
        if (this.hasOwner() && d2 <= 256.0 && entityCreature instanceof Skeleton) {
            this.isAngryTo = entityCreature.getId();
            this.setAngry(true);
            return true;
        }
        return false;
    }

    public boolean isAngry() {
        return this.D;
    }

    public void setAngry(boolean bl) {
        this.D = bl;
        this.setDataFlag(0, 25, bl);
        this.H = bl ? 500 : 0;
        this.setFriendly(!bl);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        int n = this.getHealableItem(item);
        if (item.getId() == 352) {
            if (!this.hasOwner() && !this.isAngry()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                if (Utils.rand(1, 3) == 3) {
                    EntityEventPacket entityEventPacket = new EntityEventPacket();
                    entityEventPacket.eid = this.getId();
                    entityEventPacket.event = 7;
                    player.dataPacket(entityEventPacket);
                    this.setMaxHealth(20);
                    this.setHealth(20.0f);
                    this.setOwner(player);
                    this.setCollarColor(DyeColor.RED);
                    this.getLevel().dropExpOrb(this, Utils.rand(1, 7));
                    return true;
                }
                EntityEventPacket entityEventPacket = new EntityEventPacket();
                entityEventPacket.eid = this.getId();
                entityEventPacket.event = 6;
                player.dataPacket(entityEventPacket);
            }
        } else if (item.getId() == 351) {
            if (this.hasOwner() && player.equals(this.getOwner())) {
                this.setCollarColor(((ItemDye)item).getDyeColor());
                return true;
            }
        } else {
            if (this.isBreedingItem(item) || n != 0) {
                this.getLevel().addSound((Vector3)this, Sound.RANDOM_EAT);
                this.getLevel().addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(item.getId(), 0, 1)));
                this.setInLove();
                if (n != 0) {
                    this.setHealth(Math.max((float)this.getRealMaxHealth(), this.getHealth() + (float)n));
                }
                return true;
            }
            if (this.hasOwner() && player.equals(this.getOwner()) && !this.isAngry()) {
                this.setSitting(!this.isSitting());
            }
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (super.attack(entityDamageEvent)) {
            this.setSitting(false);
            if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                Player player;
                if (((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player && (!(player = (Player)((EntityDamageByEntityEvent)entityDamageEvent).getDamager()).isSurvival() && !player.isAdventure() || this.hasOwner() && player.equals(this.getOwner()))) {
                    return true;
                }
                this.isAngryTo = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getId();
                this.setAngry(true);
            }
            return true;
        }
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (entity instanceof Player && (!this.isAngry() && this.isBeggingItem(((Player)entity).getInventory().getItemInHandFast()) || this.hasOwner() && entity.equals(this.getOwner()))) {
            return;
        }
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.5) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            this.setMotion(this.E.setComponents(0.0, this.getGravity() * 6.0f, 0.0));
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
        }
    }

    @Override
    public boolean entityBaseTick(int n) {
        boolean bl = super.entityBaseTick(n);
        if (this.H == 1) {
            this.setAngry(false);
        } else if (this.H > 0) {
            --this.H;
        }
        if (this.isInsideOfWater()) {
            this.C = 0;
        } else if (this.C != -1) {
            ++this.C;
        }
        if (this.C > 60) {
            this.C = -1;
            this.stayTime = 40;
            EntityEventPacket entityEventPacket = new EntityEventPacket();
            entityEventPacket.eid = this.getId();
            entityEventPacket.event = 8;
            Server.broadcastPacket(this.getViewers().values(), (DataPacket)entityEventPacket);
        }
        return bl;
    }

    @Override
    protected void checkTarget() {
        if (!this.isSitting() && this.hasOwner() && this.distanceSquared(this.getOwner()) > 144.0) {
            this.setAngry(false);
            this.teleport(this.getOwner());
            this.move(0.0, 1.0E-4, 0.0);
            return;
        }
        super.checkTarget();
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 3;
    }

    public void setCollarColor(DyeColor dyeColor) {
        this.namedTag.putByte("CollarColor", dyeColor.getDyeData());
        this.setDataProperty(new ByteEntityData(3, dyeColor.getWoolData()));
        this.G = dyeColor;
    }

    @Override
    public boolean canDespawn() {
        if (this.hasOwner(false)) {
            return false;
        }
        return super.canDespawn();
    }

    public boolean isBeggingItem(Item item) {
        return item.getId() == 352 || item.getId() == 365 || item.getId() == 366 || item.getId() == 363 || item.getId() == 364 || item.getId() == 423 || item.getId() == 424 || item.getId() == 319 || item.getId() == 320 || item.getId() == 411 || item.getId() == 412 || item.getId() == 367;
    }

    public boolean isBreedingItem(Item item) {
        return item.getId() == 365 || item.getId() == 366 || item.getId() == 363 || item.getId() == 364 || item.getId() == 423 || item.getId() == 424 || item.getId() == 319 || item.getId() == 320 || item.getId() == 411 || item.getId() == 412 || item.getId() == 367;
    }

    public int getHealableItem(Item item) {
        switch (item.getId()) {
            case 319: 
            case 363: 
            case 411: {
                return 3;
            }
            case 320: 
            case 364: {
                return 8;
            }
            case 349: 
            case 365: 
            case 423: 
            case 460: {
                return 2;
            }
            case 461: 
            case 462: {
                return 1;
            }
            case 350: 
            case 412: {
                return 5;
            }
            case 366: 
            case 424: 
            case 463: {
                return 6;
            }
            case 367: {
                return 4;
            }
            case 413: {
                return 10;
            }
        }
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Wolf";
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed();
    }

    static {
        F = "CollarColor";
        I = "Angry";
    }
}

