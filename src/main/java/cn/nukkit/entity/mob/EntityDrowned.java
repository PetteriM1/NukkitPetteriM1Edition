package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityDrowned extends EntityWalkingMob implements EntitySmite {

    public static final int NETWORK_ID = 110;

    public Item tool;

    public EntityDrowned(FullChunk chunk, CompoundTag nbt) {
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
        return 1.95f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setMaxHealth(20);
        this.setDamage(new int[] { 0, 2, 3, 4 });

        if (this.namedTag.contains("Item")) {
            this.tool = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
        } else {
            this.setRandomTool();
        }
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && player.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

            if (player instanceof Player) {
                HashMap<Integer, Float> armorValues = new ArmorPoints();

                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0f);
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.getId();
            pk.event = EntityEventPacket.ARM_SWING;
            Server.broadcastPacket(this.getViewers().values(), pk);
        }
    }
    
    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate;
        if (Timings.entityBaseTickTimer != null) Timings.entityBaseTickTimer.startTiming();

        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        hasUpdate = super.entityBaseTick(tickDiff);

        if (level.shouldMobBurn(this)) {
            this.setOnFire(100);
        }

        if (Timings.entityBaseTickTimer != null) Timings.entityBaseTickTimer.stopTiming();
        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.ROTTEN_FLESH, 0, 1));
            }

            if (Utils.rand(1, 100) <= 11) {
                drops.add(Item.get(Item.GOLD_INGOT, 0, 1));
            }

            if (tool != null && Utils.rand(1, 100) == 50) {
                drops.add(tool);
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    private void setRandomTool() {
        switch (Utils.rand(1, 3)) {
            case 1:
                if (Utils.rand(1, 100) <= 15) {
                    this.tool = Item.get(Item.TRIDENT, Utils.rand(200, 246), 1);
                }
                return;
            case 2:
                if (Utils.rand(1, 100) == 50) {
                    this.tool = Item.get(Item.FISHING_ROD, Utils.rand(51, 61), 1);
                }
                return;
            case 3:
                if (Utils.rand(1, 100) <= 8) {
                    this.tool = Item.get(Item.NAUTILUS_SHELL, 0, 1);
                }
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        if (this.tool != null) {
            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = this.getId();
            pk.hotbarSlot = 0;
            pk.item = this.tool;
            player.dataPacket(pk);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        if (tool != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(tool));
        }
    }
}
