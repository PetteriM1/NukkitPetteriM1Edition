/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ExplodeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.HashMap;

public class EntitySilverfish
extends EntityWalkingMob
implements EntityArthropod {
    public static final int NETWORK_ID = 39;
    private boolean w;

    public EntitySilverfish(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 39;
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
    }

    @Override
    public double getSpeed() {
        return 1.4;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(8);
        super.initEntity();
        this.setDamage(new int[]{0, 1, 1, 1});
        this.w = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.0) {
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
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int n) {
        boolean bl = super.entityBaseTick(n);
        if (this.closed) {
            return false;
        }
        if (this.w && this.isCollided && this.followTarget == null && this.age != 0 && this.age % 40 == 0 && Utils.rand(0, 5) == 3) {
            Block[] blockArray;
            block5: for (Block block : blockArray = this.level.getCollisionBlocks(this.getBoundingBox().grow(0.1, 0.1, 0.1))) {
                int n2 = block.getId();
                switch (n2) {
                    case 1: {
                        this.level.setBlockAt((int)block.x, (int)block.y, (int)block.z, 97, 0);
                        this.getLevel().addParticle(new ExplodeParticle(block.add(0.5, 1.0, 0.5)));
                        this.close();
                        continue block5;
                    }
                    case 4: {
                        this.level.setBlockAt((int)block.x, (int)block.y, (int)block.z, 97, 1);
                        this.getLevel().addParticle(new ExplodeParticle(block.add(0.5, 1.0, 0.5)));
                        this.close();
                        continue block5;
                    }
                    case 98: {
                        this.level.setBlockAt((int)block.x, (int)block.y, (int)block.z, 97, block.getDamage() == 0 ? 2 : (block.getDamage() == 1 ? 3 : (block.getDamage() == 2 ? 4 : (block.getDamage() == 3 ? 5 : 0))));
                        this.getLevel().addParticle(new ExplodeParticle(block.add(0.5, 1.0, 0.5)));
                        this.close();
                    }
                }
            }
        }
        return bl;
    }
}

