/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntitySpider
extends EntityWalkingMob
implements EntityArthropod {
    public static final int NETWORK_ID = 35;
    private int w = 0;

    public EntitySpider(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 35;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 0.9f;
    }

    @Override
    public double getSpeed() {
        return 1.13;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(16);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 2, 3});
    }

    @Override
    protected boolean checkJump(double d2, double d3) {
        if (this.motionY == (double)(this.getGravity() * 2.0f)) {
            return this.canSwimIn(this.level.getBlockIdAt(this.chunk, NukkitMath.floorDouble(this.x), (int)this.y, NukkitMath.floorDouble(this.z)));
        }
        if (this.canSwimIn(this.level.getBlockIdAt(this.chunk, NukkitMath.floorDouble(this.x), (int)(this.y + 0.8), NukkitMath.floorDouble(this.z)))) {
            this.motionY = this.getGravity() * 2.0f;
            return true;
        }
        try {
            Block block = this.getLevel().getBlock(this.chunk, NukkitMath.floorDouble(this.x + d2), (int)this.y, NukkitMath.floorDouble(this.z + d3), false);
            Block block2 = block.getSide(this.getDirection());
            if (!block2.canPassThrough()) {
                this.motionY = this.getGravity() * 3.0f;
                return true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
        if ((!this.isFriendly() || !(entity instanceof Player)) && this.isAngry() && this.attackDelay > 23 && this.distanceSquared(entity) < 1.3) {
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
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.setAngry(2400);
        }
        return true;
    }

    @Override
    public Item[] getDrops() {
        int n;
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (n = 0; n < Utils.rand(0, 2); ++n) {
            arrayList.add(Item.get(287, 0, 1));
        }
        for (n = 0; n < (Utils.rand(0, 2) == 0 ? 1 : 0); ++n) {
            arrayList.add(Item.get(375, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (this.w > 0) {
            --this.w;
        }
        return super.entityBaseTick(n);
    }

    public boolean isAngry() {
        int n = this.level.getTime() % 24000;
        return this.w > 0 || n > 13184 && n < 22800;
    }

    public void setAngry(int n) {
        this.w = n;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return this.isAngry() && super.targetOption(entityCreature, d2);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

