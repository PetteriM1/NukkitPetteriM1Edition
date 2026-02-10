package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

import java.util.HashMap;

public class EntityPhantom extends EntityFlyingMob implements EntitySmite {

    public static final int NETWORK_ID = 58;

    private int nextDive;
    private int moveUpTicks;

    public EntityPhantom(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.PHANTOM_MEMBRANE, 0, Utils.rand(0, 1))};
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return 2;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && player.distanceSquared(this) <= 1.5) {
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

            this.moveUpTicks = Utils.rand(60, 70);
            this.nextDive = server.getTick() + Utils.rand(200, 400);
        }
    }

    @Override
    public boolean dropsOnNaturalDeath() {
        return false;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate;

        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        if (this.moveUpTicks > 0) {
            this.moveUpTicks--;
        }

        hasUpdate = super.entityBaseTick(tickDiff);

        if (shouldMobBurn()) {
            this.setOnFire(100);
        }

        return hasUpdate;
    }

    @Override
    protected double getVerticalSpeed(double m1, double m2) {
        return !(this.target instanceof Entity || this.followTarget != null) ? super.getVerticalSpeed(m1, m2) : server.getTick() >= this.nextDive ? -0.4 : this.moveUpTicks > 0 ? 0.3 : 0;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new int[]{0, 4, 6, 9});
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && (player.isSurvival() || player.isAdventure()) && distance <= 1024;
        }
        return creature.isAlive() && !creature.closed && distance <= 1024;
    }
}
