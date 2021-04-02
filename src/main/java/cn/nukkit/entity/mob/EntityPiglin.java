package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.HashMap;

/**
 * @author Erik Miller | EinBexiii
 */
public class EntityPiglin extends EntityWalkingMob {

    public final static int NETWORK_ID = 123;

    private int angry = 0;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityPiglin(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(16);
        this.setDamage(new int[]{0, 3, 5, 7});
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 30 && player.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

            if (player instanceof Player) {
                HashMap<Integer, Float> armorValues = new ArmorPoints();

                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0f);
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    public void setAngry(int val) {
        this.angry = val;
    }

    private static boolean isWearingGold(Player p) {
        PlayerInventory i = p.getInventory();
        return i.getHelmetFast().getId() == ItemID.GOLD_HELMET || i.getChestplateFast().getId() == ItemID.GOLD_CHESTPLATE || i.getLeggingsFast().getId() == ItemID.GOLD_LEGGINGS || i.getBootsFast().getId() == ItemID.GOLD_BOOTS;
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.setAngry(600);
            }
        }

        return true;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (distance <= 100 && this.isAngry() && creature instanceof EntityPiglin && !((EntityPiglin) creature).isAngry()) {
            ((EntityPiglin) creature).setAngry(600);
        }
        return creature.isPlayer && (this.isAngry() || !isWearingGold((Player) creature)) && super.targetOption(creature, distance);
    }
}
