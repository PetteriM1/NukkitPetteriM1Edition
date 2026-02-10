package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.HashMap;

public class EntityRavager extends EntityWalkingMob {

    public static final int NETWORK_ID = 59;

    public EntityRavager(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getHeight() {
        return 2.2f;
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    protected float getKnockbackModifier() {
        return 0.25f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    public float getWidth() {
        return 1.9f;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 80 && player.distanceSquared(this) <= 1.5) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

            if (player instanceof Player) {
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += this.getArmorPoints(i.getId());
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }

            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(100);
        super.initEntity();
        this.setDamage(new int[]{0, 7, 12, 18});
    }
}