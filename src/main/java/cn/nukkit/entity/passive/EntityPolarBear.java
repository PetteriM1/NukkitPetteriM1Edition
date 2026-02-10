package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityPolarBear extends EntityWalkingMob {

    public static final int NETWORK_ID = 28;

    private int angry = 0;

    public EntityPolarBear(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.setAngry(2400);
            }
        }

        return true;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared(player) < 1.44) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

            if (player instanceof Player) {
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += this.getArmorPoints(i.getId());
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.angry > 0) {
            this.angry--;
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            drops.add(Item.get(Item.RAW_FISH, 0, Utils.rand(0, 2)));
            drops.add(Item.get(Item.RAW_SALMON, 0, Utils.rand(0, 2)));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    protected float getFreezingDamage() {
        return 0f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Polar Bear";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return 1.25;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(30);
        super.initEntity();

        this.setDamage(new int[]{0, 4, 6, 9});

        if (!this.server.suomiCraftPEMode() && this.namedTag.contains("Angry")) {
            this.angry = this.namedTag.getInt("Angry");
        }
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Angry", this.angry);
    }

    public void setAngry(int val) {
        this.angry = val;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.isAngry() && super.targetOption(creature, distance);
    }
}
