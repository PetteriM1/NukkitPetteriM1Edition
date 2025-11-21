package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.passive.EntityVillagerV1;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityZombie extends EntityWalkingMob implements EntitySmite {

    public static final int NETWORK_ID = 32;

    private Item tool;

    private boolean hasPlayerItem;

    private boolean pickupItems;

    public EntityZombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled() && ev.getCause() == EntityDamageEvent.DamageCause.DROWNING && !(this instanceof EntityZombieVillagerV1)) {
            CreatureSpawnEvent cse = new CreatureSpawnEvent(EntityDrowned.NETWORK_ID, this, CreatureSpawnEvent.SpawnReason.DROWNED);
            level.getServer().getPluginManager().callEvent(cse);

            if (!cse.isCancelled()) {
                CompoundTag nbt = Entity.getDefaultNBT(this).putBoolean("HandItemSet", true);
                Entity ent = Entity.createEntity(EntityDrowned.NETWORK_ID, this.getChunk(), nbt);
                if (ent != null) {
                    // According to Minecraft Wiki the drowned always spawns with full health
                    this.close();
                    ent.spawnToAll();
                }
            }
        }

        return true;
    }

    @Override
    public void attackEntity(Entity target) {
        if (this.attackDelay > 23 && target.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            int attackDamage = this.getDamage();
            if (attackDamage != 0 && this.tool != null && this.tool.getAttackDamage() > 0) {
                attackDamage += this.tool.getAttackDamage() - 1;
            }
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) attackDamage);

            if (target instanceof Player) {
                float points = 0;
                for (Item i : ((Player) target).getInventory().getArmorContents()) {
                    points += this.getArmorPoints(i.getId());
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            target.attack(new EntityDamageByEntityEvent(this, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
            this.playAttack();

            if (target instanceof EntityVillagerV1 && this.getServer().getDifficulty() > 1 && Utils.rand()) {
                CreatureSpawnEvent cse = new CreatureSpawnEvent(EntityZombieVillagerV1.NETWORK_ID, this, CreatureSpawnEvent.SpawnReason.INFECTION);
                level.getServer().getPluginManager().callEvent(cse);

                if (!cse.isCancelled()) {
                    Entity ent = Entity.createEntity("ZombieVillager", this);
                    if (ent != null) {
                        ent.setHealth(target.getHealth());
                        target.close();
                        ent.spawnToAll();
                    }
                }
            }
        }
    }

    @Override
    public boolean canDespawn() {
        return !this.hasPlayerItem && super.canDespawn();
    }

    @Override
    public boolean canTarget(Entity entity) {
        return (entity.getId() == this.isAngryTo || entity instanceof Player) && entity.canBeFollowed();
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }


        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (!this.closed && this.isAlive()) {
            if (shouldMobBurn()) {
                if (this.armor[0] == null) {
                    this.setOnFire(100);
                } else if (this.armor[0].getId() == 0) {
                    this.setOnFire(100);
                }
            }

            if (this.pickupItems && this.tool == null && this.age % 60 == 0) {
                Entity[] entities = level.getCollidingEntities(this.boundingBox);
                for (Entity entity : entities) {
                    if (entity instanceof EntityItem) {
                        Item item = ((EntityItem) entity).getItem();
                        if (item != null && item.getCount() == 1) {
                            Player droppedBy = ((EntityItem) entity).droppedBy;
                            if (droppedBy != null) {
                                droppedBy.awardAchievement("diamondsToYou");
                            }
                            entity.close();
                            this.tool = item;
                            this.hasPlayerItem = true;
                            this.getViewers().forEach((id, pl) -> this.sendTool(pl));
                        }
                        break;
                    }
                }
            }

            if (this.age % 111 == 0) {
                if (this.getLevel().getEntity(this.isAngryTo) == null) {
                    this.isAngryTo = -1;

                    for (Entity e : this.getChunk().getEntities().values()) {
                        if (e instanceof EntityVillagerV1) {
                            this.isAngryTo = e.getId();
                            this.target = e;
                            this.followTarget = e;
                            break;
                        }
                    }
                }
            }
        }

        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.ROTTEN_FLESH, 0, 1));
            }

            if (this.tool != null) {
                if (this.hasPlayerItem || Utils.rand(1, 3) == 1) {
                    drops.add(this.tool);
                }
            }

            if (this.armor != null && armor.length == 4 && Utils.rand(1, 3) == 1) { // TODO: always drop picked up armor
                drops.add(armor[Utils.rand(0, 3)]);
            }

            if (Utils.rand(1, 3) == 1) {
                switch (Utils.rand(1, 3)) {
                    case 1:
                        drops.add(Item.get(Item.IRON_INGOT, 0, Utils.rand(0, 1)));
                        break;
                    case 2:
                        drops.add(Item.get(Item.CARROT, 0, Utils.rand(0, 1)));
                        break;
                    case 3:
                        drops.add(Item.get(Item.POTATO, 0, Utils.rand(0, 1)));
                        break;
                }
            }
        } else if (this.tool != null && this.hasPlayerItem) { // always drop picked up items
            drops.add(this.tool);
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 12 : 5;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return this.isBaby() ? 1.6 : 1.1;
    }

    /**
     * Get held tool
     *
     * @return the tool this zombie has in hand or null
     */
    public Item getTool() {
        return this.tool;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return !this.hasPlayerItem && super.ignoredAsSaveReason();
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();

        this.setDamage(new int[]{0, 2, 3, 4});

        if (this.namedTag.contains("Armor") && this.namedTag.get("Armor") instanceof ListTag) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Armor", CompoundTag.class);
            Item[] loadedArmor = new Item[4];
            int count = 0;
            for (CompoundTag item : listTag.getAll()) {
                int slot = item.getByte("Slot");
                if (slot < 0 || slot > 3) {
                    this.server.getLogger().error("Failed to load zombie armor: Invalid slot: " + slot);
                    break;
                }
                if (slot < count) {
                    this.server.getLogger().error("Failed to load zombie armor: Duplicated slot: " + slot);
                    break;
                }
                loadedArmor[slot] = NBTIO.getItemHelper(item);
                count++;
            }
            this.armor = loadedArmor;
        } else {
            this.armor = getRandomArmor();
        }

        this.addArmorExtraHealth(); //TODO: replace with actual damage modifier

        if (this.namedTag.contains("Item")) {
            this.tool = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
            if (this.tool != null) {
                this.hasPlayerItem = this.namedTag.getBoolean("hasPlayerItem", false);
            }
        } else {
            this.setRandomTool();
        }

        this.pickupItems = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    private void saveArmor() {
        if (this.armor != null && this.armor.length == 4) {
            ListTag<CompoundTag> listTag = new ListTag<>("Armor");
            for (int slot = 0; slot < 4; ++slot) {
                listTag.add(NBTIO.putItemHelper(this.armor[slot], slot));
            }
            this.namedTag.putList(listTag);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.saveTool();
        this.saveArmor();
    }

    private void saveTool() {
        if (tool != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(tool));
            this.namedTag.putBoolean("hasPlayerItem", hasPlayerItem);
        }
    }

    private void sendTool(Player p) {
        if (this.tool != null) {
            Item clean = null;

            if (server.reduceTraffic) {
                clean = Item.get(this.tool.getId(), this.tool.getDamage(), 1);

                CompoundTag oldTag = this.tool.getNamedTag();

                if (oldTag != null) {
                    clean.setNamedTag(CompoundTag.sanitize(oldTag));
                }
            }

            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = this.getId();
            pk.hotbarSlot = 0;
            pk.item = clean != null ? clean : this.tool;
            p.dataPacket(pk);
        }
    }

    private void setRandomTool() {
        if (Utils.rand(1, 10) == 5) {
            if (Utils.rand(1, 3) == 1) {
                this.tool = Item.get(Item.IRON_SWORD, Utils.rand(200, 246), 1);
            } else {
                this.tool = Item.get(Item.IRON_SHOVEL, Utils.rand(200, 246), 1);
            }
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        if (this.armor[0].getId() != 0 || this.armor[1].getId() != 0 || this.armor[2].getId() != 0 || this.armor[3].getId() != 0) {
            MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
            pk.eid = this.getId();
            pk.slots = this.armor;

            player.dataPacket(pk);
        }

        this.sendTool(player);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return (creature instanceof EntityVillagerV1 && creature.getId() == this.isAngryTo && creature.isAlive() && distance <= 1764) || targetOptionInternal(creature, distance);
    }

    private boolean targetOptionInternal(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure())) {
                PlayerInventory inv = player.getInventory();
                Item helmet;
                if (inv != null && (helmet = inv.getHelmetFast()).getId() == Item.SKULL && helmet.getDamage() == ItemSkull.ZOMBIE_HEAD) {
                    return distance <= 64;
                }
                return distance <= 256;
            }
            return false;
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }
}
