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
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityZombiePigman extends EntityWalkingMob implements EntitySmite {

    public static final int NETWORK_ID = 36;

    private int angry;

    public EntityZombiePigman(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public double getSpeed() {
        return this.isBaby() ? 1.6 : this.isAngry() ? 1.15 : 1.1;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();

        if (!this.server.suomiCraftPEMode() && this.namedTag.contains("Angry")) {
            this.angry = this.namedTag.getInt("Angry");
        }

        this.fireProof = true;
        this.setDamage(new int[]{0, 5, 9, 13});
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Angry", this.angry);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.isAngry() && super.targetOption(creature, distance);
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared(player) < 2 + this.getWidth() * this.getWidth()) {
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
            this.playAttack();
        }
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    public void setAngry(int val) {
        this.setAngry(val, false);
    }

    public void setAngry(int val, boolean others) {
        this.angry = val;

        if (others && val > 0) {
            for (Entity creature : this.level.getEntitiesList()) {
                if (creature instanceof EntityZombiePigman && !((EntityZombiePigman) creature).isAngry() && this.distanceSquared(creature) <= 400) { // 20 blocks
                    ((EntityZombiePigman) creature).setAngry(val);
                }
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.setAngry(2400, true);
            }
        }

        return true;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = Item.get(Item.GOLDEN_SWORD);
        pk.inventorySlot = 0;
        player.dataPacket(pk);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            drops.add(Item.get(Item.ROTTEN_FLESH, 0, Utils.rand(0, 1)));
            drops.add(Item.get(Item.GOLD_NUGGET, 0, Utils.rand(0, 1)));

            for (int i = 0; i < (Utils.rand(0, 101) <= 9 ? 1 : 0); i++) {
                drops.add(Item.get(Item.GOLD_SWORD, Utils.rand(20, 30), 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Zombified Piglin";
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        if (this.angry > 0) {
            this.angry--;
        }

        return super.entityBaseTick(tickDiff);
    }
}
