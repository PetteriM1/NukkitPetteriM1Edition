package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.randomitem.ConstantItemSelector;
import cn.nukkit.item.randomitem.RandomItem;
import cn.nukkit.item.randomitem.Selector;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Erik Miller | EinBexiii
 */
public class EntityPiglin extends EntityWalkingMob implements InventoryHolder {

    public final static int NETWORK_ID = 123;

    @Getter
    private Item offhandItem;
    @Getter
    private Item handItem;
    private int angry;
    private boolean angryFlagSet;
    private boolean isRunning;
    private boolean trading;
    private int lookingTicks;
    private Player lookingPlayer;
    private PiglinInventory inventory;

    public EntityPiglin(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public static class PiglinInventory extends ContainerInventory {

        public PiglinInventory(InventoryHolder holder) {
            super(holder, InventoryType.CHEST);
        }

        @Override
        public int getSize() {
            return 8;
        }
    }

    private static class PiglinDrops {

        @SuppressWarnings("unused")
        private static Item getResult() {
            Selector ROOT_TRADING = RandomItem.putSelector(new Selector(RandomItem.ROOT));
            Selector GRAVEL = RandomItem.putSelector(new ConstantItemSelector(Block.GRAVEL, 0, Utils.rand(8, 16), ROOT_TRADING), 0.077f);
            Selector BLACKSTONE = RandomItem.putSelector(new ConstantItemSelector(255 - Block.BLACKSTONE, 0, Utils.rand(8, 16), ROOT_TRADING), 0.077f);
            Selector ARROW = RandomItem.putSelector(new ConstantItemSelector(Item.ARROW, 0, Utils.rand(6, 12), ROOT_TRADING), 0.087f);
            Selector SOULSAND = RandomItem.putSelector(new ConstantItemSelector(Block.SOUL_SAND, 0, Utils.rand(2, 8), ROOT_TRADING), 0.087f);
            Selector NETHER_BRICK = RandomItem.putSelector(new ConstantItemSelector(Item.BRICK, 0, Utils.rand(2, 8), ROOT_TRADING), 0.087f);
            Selector LEATHER = RandomItem.putSelector(new ConstantItemSelector(Item.LEATHER, 0, Utils.rand(2, 4), ROOT_TRADING), 0.087f);
            Selector CRYING_OBSIDIAN = RandomItem.putSelector(new ConstantItemSelector(255 - Block.CRYING_OBSIDIAN, 0, Utils.rand(1, 3), ROOT_TRADING), 0.087f);
            Selector OBSIDIAN = RandomItem.putSelector(new ConstantItemSelector(Block.OBSIDIAN, 0, 1, ROOT_TRADING), 0.087f);
            Selector FIRE_CHARGE = RandomItem.putSelector(new ConstantItemSelector(Item.FIRE_CHARGE, 0, 1, ROOT_TRADING), 0.087f);
            Selector NETHER_QUARZ = RandomItem.putSelector(new ConstantItemSelector(Item.QUARTZ, 0, Utils.rand(5, 12), ROOT_TRADING), 0.043f);
            Selector STRING = RandomItem.putSelector(new ConstantItemSelector(Item.STRING, 0, Utils.rand(3, 9), ROOT_TRADING), 0.043f);
            Selector ENDER_PEARL = RandomItem.putSelector(new ConstantItemSelector(Item.ENDER_PEARL, 0, Utils.rand(2, 4), ROOT_TRADING), 0.043f);
            Selector IRON_NUGGET = RandomItem.putSelector(new ConstantItemSelector(Item.IRON_NUGGET, 0, Utils.rand(10, 36), ROOT_TRADING), 0.021f);
            Selector FIRE_RESITANCE_POTION_TROWABLE = RandomItem.putSelector(new ConstantItemSelector(Item.POTION, 13, 1, ROOT_TRADING), 0.021f);
            Selector FIRE_RESITANCE_POTION = RandomItem.putSelector(new ConstantItemSelector(Item.SPLASH_POTION, 13, 1, ROOT_TRADING), 0.021f);
            Selector WATER_BOTTLE = RandomItem.putSelector(new ConstantItemSelector(Item.IRON_NUGGET, 0, 1, ROOT_TRADING), 0.021f);
            Selector ENCHANTED_IRON_BOOTS = RandomItem.putSelector(new ConstantItemSelector(Item.IRON_BOOTS, 0, 1, ROOT_TRADING), 0.017f);
            Selector ENCHANTED_BOOK = RandomItem.putSelector(new ConstantItemSelector(Item.ENCHANTED_BOOK, 0, 1, ROOT_TRADING), 0.01f);

            Object result = RandomItem.selectFrom(ROOT_TRADING);
            if (result instanceof Item) {
                if (result instanceof ItemBookEnchanted || result instanceof ItemBootsIron) {
                    ((Item) result).addEnchantment(Enchantment.getEnchantment(Enchantment.ID_SOUL_SPEED));
                }
                return (Item) result;
            }
            return null;
        }
    }

    private void animateTrade(Item item, Entity entity) {
        this.setAngry(0);
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_ADMIRING, true);
        this.stayTime = 140;
        this.lookingTicks = 70; // 140 but updated every second tick
        this.trading = true;
        this.isRunning = false;
        Item cloneitem = item.clone();
        cloneitem.count = 1;
        setOffhandItem(cloneitem);
        if (item.getCount() > 1) {
            item.setCount(item.getCount() - 1);
            entity.spawnToAll();
        } else {
            entity.close();
        }

        this.playAnimation("animation.piglin.admire");
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.trading = false;
                this.setAngry(600);
            }
        }

        return true;
    }

    @Override
    public void attackEntity(Entity player) {
        if (!(player instanceof EntityLiving)) {
            return;
        }

        if (handItem instanceof ItemCrossbow) {
            if (this.attackDelay > 40 && this.distanceSquared(player) <= 256) { // 16 blocks
                if (!this.seesTarget(player)) {
                    return;
                }

                this.attackDelay = 0;

                EntityArrow shot = (EntityArrow) Entity.createEntity("Arrow", this.add(0, this.getEyeHeight(), 0), this);

                if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                    shot.close();
                    return;
                }

                EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), shot, 2);
                this.server.getPluginManager().callEvent(ev);

                shot.setMotion(player.add(Utils.rand(-0.1, 0.1), Utils.rand(-0.1, 0.1) + 0.3, Utils.rand(-0.1, 0.1)).subtract(this).normalize().multiply(ev.getForce()));

                EntityProjectile projectile = ev.getProjectile();
                if (ev.isCancelled()) {
                    projectile.close();
                } else {
                    ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                    this.server.getPluginManager().callEvent(launch);
                    if (launch.isCancelled()) {
                        projectile.close();
                    } else {
                        projectile.namedTag.putDouble("damage", 4);
                        projectile.updateRotation();
                        projectile.spawnToAll();
                        ((EntityArrow) projectile).setPickupMode(EntityArrow.PICKUP_NONE);
                        this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_CROSSBOW_SHOOT);
                    }
                }
            }
        } else {
            if (this.attackDelay > 30 && player.distanceSquared(this) <= 1) {
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
    }

    @Override
    protected boolean canSetTemporalTarget() {
        return !this.isRunning;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }

        if (this.isAlive() && this.level != null) {
            if (this.angry > 0) {
                if (this.angry == 1) {
                    this.setAngry(0); // Reset flag
                } else {
                    this.angry--;
                }
            }

            if (lookingTicks > 0 && age % 2 == 1) {
                if (lookingPlayer != null && !lookingPlayer.isClosed() && lookingPlayer.isAlive() && this.getLevel().equals(lookingPlayer.getLevel())) {
                    double xdiff = lookingPlayer.x - this.x;
                    double zdiff = lookingPlayer.z - this.z;
                    double angle = FastMathLite.atan2(zdiff, xdiff);
                    double yaw = angle * 180.0D / Math.PI - 90.0D;
                    double ydiff = lookingPlayer.y - this.y;
                    double dist = new Vector2(this.x, this.z).distance(lookingPlayer.x, lookingPlayer.z);
                    angle = FastMathLite.atan2(dist, ydiff);
                    double pitch = angle * 180.0D / Math.PI - 90.0D;
                    this.noRotateTicks = lookingTicks;
                    this.setRotation(yaw, pitch, yaw);
                    lookingTicks--;
                } else {
                    lookingTicks = 0;
                    lookingPlayer = null;
                }
            }

            if (!this.trading && age % 5 == 0) {
                if (this.stayTime <= 0) {
                    if (!(target instanceof Entity) || ((Entity) target).closed) {
                        for (Entity entity : this.getLevel().getNearbyEntities(this.getBoundingBox().grow(16, 16, 16), this)) {
                            if (entity instanceof EntityItem) {
                                Item item = ((EntityItem) entity).getItem();

                                if (item != null && (item.getId() == Item.GOLD_INGOT || (isItemValid(item) && this.inventory.canAddItem(item)) || item.isArmor())) {
                                    this.isRunning = true;
                                    this.target = entity; // setTarget(entity) would set followTarget but it must be EntityCreature
                                    break;
                                }
                            }
                        }
                    }
                }

                for (Entity entity : this.getLevel().getNearbyEntities(this.getBoundingBox().grow(1, 0, 1), this)) {
                    if (this.trading) {
                        break;
                    }

                    if (entity instanceof EntityItem) {
                        Item item = ((EntityItem) entity).getItem();

                        if (item == null) {
                            continue;
                        }

                        if (item.getId() == Item.GOLD_INGOT) {
                            if (!this.isBaby()) {
                                animateTrade(item, entity);

                                Server.getInstance().getScheduler().scheduleDelayedTask(null, () -> {
                                    if (EntityPiglin.this.level == null) {
                                        return;
                                    }
                                    if (!EntityPiglin.this.isClosed()) {
                                        this.trading = false;
                                        setOffhandItem(Item.get(Item.AIR));
                                        EntityPiglin.this.setDataFlag(DATA_FLAGS, DATA_FLAG_ADMIRING, false);
                                        EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, PiglinDrops.getResult());
                                    }
                                }, 120);
                                break;
                            } else {
                                setHandItem(item);
                            }
                        } else if (isItemValid(item)) {
                            if (this.inventory.canAddItem(item)) {
                                animateTrade(item, entity);

                                Server.getInstance().getScheduler().scheduleDelayedTask(null, () -> {
                                    if (EntityPiglin.this.level == null) {
                                        return;
                                    }
                                    if (!EntityPiglin.this.isClosed()) {
                                        this.trading = false;
                                        EntityPiglin.this.setDataFlag(DATA_FLAGS, DATA_FLAG_ADMIRING, false);
                                        setOffhandItem(Item.get(Item.AIR));
                                        Item cloneItem = item.clone();
                                        cloneItem.setCount(1);
                                        if (item.isHelmet() && (armor[0] == null || armor[0].getId() != Item.GOLD_HELMET)) {
                                            Item[] armorHelmet = this.armor;
                                            if (armorHelmet[0] != null) {
                                                EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, armorHelmet[0]);
                                            }
                                            armorHelmet[0] = item;
                                            setArmor(armorHelmet);
                                        } else if (item.isChestplate() && (armor[1] == null || armor[1].getId() != Item.GOLD_CHESTPLATE)) {
                                            Item[] armorChestplate = this.armor;
                                            if (armorChestplate[1] != null) {
                                                EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, armorChestplate[1]);
                                            }
                                            armorChestplate[1] = item;
                                            setArmor(armorChestplate);

                                        } else if (item.isLeggings() && (armor[2] == null || armor[2].getId() != Item.GOLD_LEGGINGS)) {
                                            Item[] armorLeggings = this.armor;
                                            if (armorLeggings[2] != null) {
                                                EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, armorLeggings[2]);
                                            }
                                            armorLeggings[2] = item;
                                            setArmor(armorLeggings);
                                        } else if (item.isBoots() && (armor[3] == null || armor[3].getId() != Item.GOLD_BOOTS)) {
                                            Item[] armorBoots = this.armor;
                                            if (armorBoots[3] != null) {
                                                EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, armorBoots[3]);
                                            }
                                            armorBoots[3] = item;
                                            setArmor(armorBoots);
                                        } else if (item.isSword() && handItem == null) {
                                            setHandItem(item);
                                        } else {
                                            if (EntityPiglin.this.inventory.canAddItem(cloneItem)) {
                                                EntityPiglin.this.inventory.addItem(cloneItem);
                                            }
                                        }
                                    }
                                }, 120);
                                break;
                            }
                        } else {
                            if ((item.isHelmet() && (armor[0] == null || armor[0].getId() != Item.GOLD_HELMET && item.getTier() > armor[0].getTier())) || (item.isChestplate() && (armor[1] == null || armor[1].getId() != Item.GOLD_CHESTPLATE && item.getTier() > armor[1].getTier())) || (item.isLeggings() && (armor[2] == null || armor[2].getId() != Item.GOLD_LEGGINGS && item.getTier() > armor[2].getTier())) || (item.isBoots() && (armor[3] == null || armor[3].getId() != Item.GOLD_BOOTS && item.getTier() > armor[3].getTier()))) {
                                if (item.isHelmet() && (armor[0] == null || armor[0].getId() != Item.GOLD_HELMET)) {
                                    Item[] armorHelmet = this.armor;
                                    if (armorHelmet[0] != null) {
                                        this.getLevel().dropItem(this, armorHelmet[0]);
                                    }
                                    armorHelmet[0] = item;
                                    setArmor(armorHelmet);
                                } else if (item.isChestplate() && (armor[1] == null || armor[1].getId() != Item.GOLD_CHESTPLATE)) {
                                    Item[] armorChestplate = this.armor;
                                    if (armorChestplate[1] != null) {
                                        this.getLevel().dropItem(this, armorChestplate[1]);
                                    }
                                    armorChestplate[1] = item;
                                    setArmor(armorChestplate);

                                } else if (item.isLeggings() && (armor[2] == null || armor[2].getId() != Item.GOLD_LEGGINGS)) {
                                    Item[] armorLeggings = this.armor;
                                    if (armorLeggings[2] != null) {
                                        this.getLevel().dropItem(this, armorLeggings[2]);
                                    }
                                    armorLeggings[2] = item;
                                    setArmor(armorLeggings);
                                } else if (item.isBoots() && (armor[3] == null || armor[3].getId() != Item.GOLD_BOOTS)) {
                                    Item[] armorBoots = this.armor;
                                    if (armorBoots[3] != null) {
                                        this.getLevel().dropItem(this, armorBoots[3]);
                                    }
                                    armorBoots[3] = item;
                                    setArmor(armorBoots);
                                }

                                if (item.getCount() > 1) {
                                    item.setCount(item.getCount() - 1);
                                    entity.spawnToAll();
                                } else {
                                    entity.close();
                                }
                            }
                        }
                    }
                }
            }

            if (this.isRunning) {
                this.stayTime = 0;
            }
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>(this.inventory.getContents().values());

        for (Item item : this.armor) {
            if (item == null) {
                continue;
            }

            /*if (this.getLastDamageCause() != null && this.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) this.getLastDamageCause()).getLootingLevel() >= 1) {
                if (Utils.rand(1, 200) <= 17 + ((EntityDamageByEntityEvent) this.getLastDamageCause()).getLootingLevel() * 2) {
                    drops.add(item);
                }
            } else {*/
            if (Utils.rand(1, 200) <= 17) {
                drops.add(item);
            }
            //}
        }
        if (Utils.rand(1, 200) <= 17) {
            drops.add(handItem == null ? Item.get(Item.AIR) : handItem);
        }

        drops.add(offhandItem == null ? Item.get(Item.AIR) : offhandItem);
        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public PiglinInventory getInventory() {
        return inventory;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 1 : 5;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    private static Item[] getRandomGoldArmor() {
        Item[] randomArmor = new Item[4];
        if (Utils.rand(1, 4) == 1) {
            randomArmor[0] = Item.get(Item.GOLD_HELMET, Utils.rand(10, 77));
        }
        if (Utils.rand(1, 4) == 1) {
            randomArmor[1] = Item.get(Item.GOLD_CHESTPLATE, Utils.rand(10, 112));
        }
        if (Utils.rand(1, 4) == 1) {
            randomArmor[2] = Item.get(Item.GOLD_LEGGINGS, Utils.rand(10, 105));
        }
        if (Utils.rand(1, 4) == 1) {
            randomArmor[3] = Item.get(Item.GOLD_BOOTS, Utils.rand(10, 91));
        }
        return randomArmor;
    }

    private int getSlotIndex(int index) {
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getByte("Slot") == index) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return false;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(16);
        super.initEntity();
        this.setDamage(new int[]{0, 3, 5, 7});

        if (this.namedTag.contains("ItemHand")) {
            this.setHandItem(NBTIO.getItemHelper(this.namedTag.getCompound("ItemHand")));
        } else if (!this.isBaby()) {
            if (Utils.rand(1, 2) == 1) {
                this.setHandItem(Item.get(Item.GOLDEN_SWORD));
                this.setDamage(new int[]{0, 5, 9, 13});
            } else {
                this.setHandItem(Item.get(Item.CROSSBOW));
            }
        }

        if (this.namedTag.contains("ItemOffHand")) {
            this.setOffhandItem(NBTIO.getItemHelper(this.namedTag.getCompound("ItemOffHand")));
        }

        this.inventory = new PiglinInventory(this);
        if (!namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        }
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (CompoundTag tag : list.getAll()) {
            Item item = NBTIO.getItemHelper(tag);
            this.inventory.slots.put(tag.getByte("Slot"), item);
        }

        if (!this.namedTag.contains("Armor") || !(this.namedTag.get("Armor") instanceof ListTag)) {
            this.setArmor(getRandomGoldArmor());
            this.namedTag.putList(new ListTag<CompoundTag>("Armor"));
            return;
        }
        ListTag<CompoundTag> armor = this.namedTag.getList("Armor", CompoundTag.class);
        Item[] armorItems = new Item[4];
        for (CompoundTag tag : armor.getAll()) {
            Item item = NBTIO.getItemHelper(tag);
            armorItems[tag.getByte("Slot")] = item;
        }
        this.setArmor(armorItems);
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    private static boolean isItemValid(Item item) {
        switch (item.getId()) {
            case 255 - BlockID.BELL:
            case BlockID.GOLD_BLOCK:
            case 255 - BlockID.RAW_GOLD_BLOCK:
            case ItemID.CLOCK:
            case 255 - BlockID.DEEPSLATE_GOLD_ORE:
            case ItemID.GOLDEN_APPLE_ENCHANTED:
            case 255 - BlockID.GILDED_BLACKSTONE:
            case ItemID.GLISTERING_MELON:
            case ItemID.GOLD_INGOT:
            case ItemID.GOLD_NUGGET:
            case BlockID.GOLD_ORE:
            case ItemID.GOLDEN_APPLE:
            case ItemID.GOLDEN_AXE:
            case ItemID.GOLD_BOOTS:
            case ItemID.GOLDEN_CARROT:
            case ItemID.GOLD_CHESTPLATE:
            case ItemID.GOLD_HELMET:
            case ItemID.GOLDEN_HOE:
            case ItemID.GOLD_HORSE_ARMOR:
            case ItemID.GOLD_LEGGINGS:
            case ItemID.GOLD_PICKAXE:
            case ItemID.GOLD_SHOVEL:
            case ItemID.GOLD_SWORD:
            case BlockID.LIGHT_WEIGHTED_PRESSURE_PLATE:
            case 255 - BlockID.NETHER_GOLD_ORE:
            case BlockID.POWERED_RAIL:
            case ItemID.RAW_PORKCHOP:
                return true;

            default:
                return false; //item instanceof ItemRawGold;
        }
    }

    private static boolean isWearingGold(Player p) {
        PlayerInventory i = p.getInventory();
        if (i == null) {
            return false;
        }
        return i.getHelmetFast().getId() == Item.GOLD_HELMET ||
                i.getChestplateFast().getId() == Item.GOLD_CHESTPLATE ||
                i.getLeggingsFast().getId() == Item.GOLD_LEGGINGS ||
                i.getBootsFast().getId() == Item.GOLD_BOOTS;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return handItem instanceof ItemCrossbow && (target instanceof EntityLiving || followTarget instanceof EntityLiving) ? 20 : 1;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.GOLD_INGOT && !this.trading) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_ADMIRING, true);
            this.trading = true;
            Item clone = item.clone();
            clone.setCount(1);
            this.setOffhandItem(clone);
            this.stayTime = 140;
            this.noRotateTicks = 140;
            this.playAnimation("minecraft:animation.piglin.admire");
            this.lookingPlayer = player;
            this.lookingTicks = 70; // 140 but updated every second tick

            Server.getInstance().getScheduler().scheduleDelayedTask(null, () -> {
                if (EntityPiglin.this.level == null) {
                    return;
                }
                if (!EntityPiglin.this.isClosed()) {
                    EntityPiglin.this.spawnToAll();
                    EntityPiglin.this.trading = false;
                    EntityPiglin.this.setOffhandItem(Item.get(Item.AIR));
                    if (player.isAlive() && !player.isClosed() && player.getLevel().equals(EntityPiglin.this.getLevel()) && EntityPiglin.this.distanceSquared(player) <= 49) {
                        Vector3 motion = player.subtract(EntityPiglin.this.add(0, 1, 0)).multiply(0.1D);
                        motion.y += Math.sqrt(player.distance(EntityPiglin.this.add(0, 1, 0))) * 0.12D;
                        EntityPiglin.this.getLevel().dropItem(EntityPiglin.this.add(0, 1, 0), PiglinDrops.getResult(), motion);
                    } else {
                        EntityPiglin.this.getLevel().dropItem(EntityPiglin.this, PiglinDrops.getResult());
                    }
                }
            }, 120);
            return true;
        }
        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public void saveNBT() {
        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.inventory.getSize(); index++) {
            Item item = this.inventory.getItem(index);
            int i = getSlotIndex(index);
            if (item.getId() == Item.AIR || item.getCount() <= 0) {
                if (i >= 0) {
                    this.namedTag.getList("Items").remove(i);
                }
            } else if (i < 0) {
                this.namedTag.getList("Items", CompoundTag.class).add(NBTIO.putItemHelper(item, index));
            } else {
                this.namedTag.getList("Items", CompoundTag.class).add(i, NBTIO.putItemHelper(item, index));
            }
        }

        this.namedTag.putList(new ListTag<CompoundTag>("Armor"));
        for (int i = 0; i < armor.length; i++) {
            if (armor[i] != null) {
                this.namedTag.getList("Armor", CompoundTag.class).add(NBTIO.putItemHelper(armor[i], i));
            }
        }
        if (handItem != null) {
            this.namedTag.putCompound("ItemHand", NBTIO.putItemHelper(handItem));
        }

        if (offhandItem != null) {
            this.namedTag.putCompound("ItemOffHand", NBTIO.putItemHelper(offhandItem));
        }


        super.saveNBT();
    }

    public void setAngry(int val) {
        this.angry = val;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, val > 0);
        this.angryFlagSet = val > 0;
    }

    public void setArmor(Item[] armor) {
        this.armor = armor;
        this.spawnToAll();
    }

    public void setHandItem(Item item) {
        this.handItem = item;
        this.spawnToAll();
    }

    public void setOffhandItem(Item item) {
        this.offhandItem = item;
        this.spawnToAll();
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        if (this.offhandItem != null) {
            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = this.getId();
            pk.inventorySlot = 1;
            pk.item = this.offhandItem;
            player.dataPacket(pk);
        }

        if (this.handItem != null) {
            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = this.getId();
            pk.hotbarSlot = 1;
            pk.item = this.handItem;
            player.dataPacket(pk);
        }

        if (armor != null) {
            MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
            pk.eid = this.getId();
            pk.slots = this.armor;
            player.dataPacket(pk);
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (this.isAngry() && distance <= 256 && creature instanceof EntityPiglin && !((EntityPiglin) creature).isAngry()) {
            ((EntityPiglin) creature).setAngry(600);
        }
        boolean hasTarget = !this.trading && creature instanceof Player && (this.isAngry() || !isWearingGold((Player) creature)) && targetOptionInternal(creature, distance);
        if (hasTarget) {
            if (!this.angryFlagSet) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, true);
                this.angryFlagSet = true;
            }
        } else {
            if (this.angryFlagSet) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, false);
                this.angryFlagSet = false;
                this.stayTime = 100;
            }
        }
        return hasTarget;
    }

    private boolean targetOptionInternal(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure())) {
                PlayerInventory inv = player.getInventory();
                Item helmet;
                if (inv != null && (helmet = inv.getHelmetFast()).getId() == Item.SKULL && helmet.getDamage() == ItemSkull.PIGLIN_HEAD) {
                    return distance <= 64;
                }
                return distance <= 256;
            }
            return false;
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }
}
