/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.mob.EntityZombieVillagerV1;
import cn.nukkit.entity.passive.EntityVillagerV1;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityZombie
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 32;
    private Item w;
    private boolean B;
    private boolean A;

    public EntityZombie(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 32;
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
    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 3, 4});
        if (this.namedTag.contains("Armor") && this.namedTag.get("Armor") instanceof ListTag) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Armor", CompoundTag.class);
            Item[] itemArray = new Item[4];
            int n = 0;
            for (CompoundTag compoundTag : listTag.getAll()) {
                int n2 = compoundTag.getByte("Slot");
                if (n2 < 0 || n2 > 3) {
                    this.server.getLogger().error("Failed to load zombie armor: Invalid slot: " + n2);
                    break;
                }
                if (n2 < n) {
                    this.server.getLogger().error("Failed to load zombie armor: Duplicated slot: " + n2);
                    break;
                }
                itemArray[n2] = NBTIO.getItemHelper(compoundTag);
                ++n;
            }
            this.armor = itemArray;
        } else {
            this.armor = this.getRandomArmor();
        }
        this.addArmorExtraHealth();
        if (this.namedTag.contains("Item")) {
            this.w = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
            if (this.w != null) {
                if (this.w.isSword() && this.w.getTier() > 1) {
                    this.setDamage(new int[]{0, 4, 6, 8});
                } else if (this.w.isTool()) {
                    this.setDamage(new int[]{0, 3, 4, 5});
                }
                this.B = this.namedTag.getBoolean("hasPlayerItem", false);
            }
        } else {
            this.c();
        }
        this.A = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && entity.distanceSquared(this) <= 1.0) {
            Object object;
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                object = ((Player)entity).getInventory().getArmorContents();
                int n = ((Item[])object).length;
                for (int k = 0; k < n; ++k) {
                    Item item = object[k];
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
            this.playAttack();
            if (entity instanceof EntityVillagerV1 && this.getServer().getDifficulty() > 1 && Utils.rand()) {
                CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(44, this, CreatureSpawnEvent.SpawnReason.INFECTION);
                this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
                if (!creatureSpawnEvent.isCancelled() && (object = Entity.createEntity("ZombieVillager", (Position)this, new Object[0])) != null) {
                    ((Entity)object).setHealth(entity.getHealth());
                    entity.close();
                    ((Entity)object).spawnToAll();
                }
            }
        }
    }

    @Override
    public boolean entityBaseTick(int n2) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.startTiming();
        }
        boolean bl = super.entityBaseTick(n2);
        if (!this.closed) {
            if (this.level.shouldMobBurn(this)) {
                if (this.armor[0] == null) {
                    this.setOnFire(100);
                } else if (this.armor[0].getId() == 0) {
                    this.setOnFire(100);
                }
            }
            if (this.A && this.w == null && this.age % 60 == 0) {
                Entity[] object = this.level.getCollidingEntities(this.boundingBox);
                for (Entity entity : object) {
                    if (!(entity instanceof EntityItem)) continue;
                    Item item = ((EntityItem)entity).getItem();
                    if (item == null || item.getCount() != 1) break;
                    Player player2 = ((EntityItem)entity).droppedBy;
                    if (player2 != null) {
                        player2.awardAchievement("diamondsToYou");
                    }
                    entity.close();
                    this.w = item;
                    if (item.isSword() && item.getTier() > 1) {
                        this.setDamage(new int[]{0, 4, 6, 8});
                    } else if (item.isTool()) {
                        this.setDamage(new int[]{0, 3, 4, 5});
                    }
                    this.B = true;
                    this.getViewers().forEach((n, player) -> this.b((Player)player));
                    break;
                }
            }
            if (this.age % 111 == 0 && this.getLevel().getEntity(this.isAngryTo) == null) {
                this.isAngryTo = -1L;
                for (Entity entity : this.getChunk().getEntities().values()) {
                    if (!(entity instanceof EntityVillagerV1)) continue;
                    this.isAngryTo = entity.getId();
                    this.target = entity;
                    this.followTarget = entity;
                    break;
                }
            }
        }
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.stopTiming();
        }
        return bl;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(367, 0, 1));
            }
            if (this.w != null && (this.B || Utils.rand(1, 3) == 1)) {
                arrayList.add(this.w);
            }
            if (this.armor != null && this.armor.length == 4 && Utils.rand(1, 3) == 1) {
                arrayList.add(this.armor[Utils.rand(0, 3)]);
            }
            if (Utils.rand(1, 3) == 1) {
                switch (Utils.rand(1, 3)) {
                    case 1: {
                        arrayList.add(Item.get(265, 0, Utils.rand(0, 1)));
                        break;
                    }
                    case 2: {
                        arrayList.add(Item.get(391, 0, Utils.rand(0, 1)));
                        break;
                    }
                    case 3: {
                        arrayList.add(Item.get(392, 0, Utils.rand(0, 1)));
                    }
                }
            }
        } else if (this.w != null && this.B) {
            arrayList.add(this.w);
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 12 : 5;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        if (this.armor[0].getId() != 0 || this.armor[1].getId() != 0 || this.armor[2].getId() != 0 || this.armor[3].getId() != 0) {
            MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
            mobArmorEquipmentPacket.eid = this.getId();
            mobArmorEquipmentPacket.slots = this.armor;
            player.dataPacket(mobArmorEquipmentPacket);
        }
        this.b(player);
    }

    private void c() {
        if (Utils.rand(1, 10) == 5) {
            if (Utils.rand(1, 3) == 1) {
                this.w = Item.get(267, Utils.rand(200, 246), 1);
                this.setDamage(new int[]{0, 4, 6, 8});
            } else {
                this.w = Item.get(256, Utils.rand(200, 246), 1);
                this.setDamage(new int[]{0, 3, 4, 5});
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.DROWNING && !(this instanceof EntityZombieVillagerV1)) {
            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(110, this, CreatureSpawnEvent.SpawnReason.DROWNED);
            this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
            if (!creatureSpawnEvent.isCancelled()) {
                CompoundTag compoundTag = Entity.getDefaultNBT(this).putBoolean("HandItemSet", true);
                Entity entity = Entity.createEntity(110, this.getChunk(), compoundTag, new Object[0]);
                if (entity != null) {
                    this.close();
                    entity.spawnToAll();
                }
            }
        }
        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.b();
        this.d();
    }

    private void b() {
        if (this.w != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(this.w));
            this.namedTag.putBoolean("hasPlayerItem", this.B);
        }
    }

    private void d() {
        if (this.armor != null && this.armor.length == 4) {
            ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("Armor");
            for (int k = 0; k < 4; ++k) {
                listTag.add(NBTIO.putItemHelper(this.armor[k], k));
            }
            this.namedTag.putList(listTag);
        }
    }

    private void b(Player player) {
        if (this.w != null) {
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.eid = this.getId();
            mobEquipmentPacket.hotbarSlot = 0;
            mobEquipmentPacket.item = this.w;
            player.dataPacket(mobEquipmentPacket);
        }
    }

    @Override
    public boolean canDespawn() {
        return super.canDespawn() && !this.B;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return entityCreature instanceof EntityVillagerV1 && entityCreature.getId() == this.isAngryTo && entityCreature.isAlive() && d2 <= 1764.0 || super.targetOption(entityCreature, d2);
    }

    @Override
    public boolean canTarget(Entity entity) {
        return (entity.getId() == this.isAngryTo || entity instanceof Player) && entity.canBeFollowed();
    }

    public Item getTool() {
        return this.w;
    }
}

