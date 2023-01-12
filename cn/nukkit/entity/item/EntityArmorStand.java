/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.EntityArmorInventory;
import cn.nukkit.inventory.EntityEquipmentInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import java.util.Collection;

public class EntityArmorStand
extends Entity
implements InventoryHolder,
EntityInteractable {
    public static final int NETWORK_ID = 61;
    public static final String TAG_MAINHAND;
    public static final String TAG_OFFHAND;
    public static final String TAG_POSE_INDEX;
    public static final String TAG_ARMOR;
    private EntityEquipmentInventory k;
    private EntityArmorInventory l;
    private int n;
    private String m;

    public EntityArmorStand(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        if (compoundTag.contains("PoseIndex")) {
            this.setPose(compoundTag.getInt("PoseIndex"));
        }
    }

    private static int a(ItemArmor itemArmor) {
        if (itemArmor.canBePutInHelmetSlot()) {
            return 0;
        }
        if (itemArmor.isChestplate()) {
            return 1;
        }
        if (itemArmor.isLeggings()) {
            return 2;
        }
        return 3;
    }

    @Override
    public int getNetworkId() {
        return 61;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    public float getHeight() {
        return 1.975f;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(6);
        super.initEntity();
        this.setHealth(6.0f);
        this.setImmobile(true);
        this.k = new EntityEquipmentInventory(this);
        this.l = new EntityArmorInventory(this);
        if (this.namedTag.contains("Mainhand")) {
            this.k.setItemInHand(NBTIO.getItemHelper(this.namedTag.getCompound("Mainhand")), true);
        }
        if (this.namedTag.contains("Offhand")) {
            this.k.setOffhandItem(NBTIO.getItemHelper(this.namedTag.getCompound("Offhand")), true);
        }
        if (this.namedTag.contains("Armor")) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Armor", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                this.l.setItem(compoundTag.getByte("Slot"), NBTIO.getItemHelper(compoundTag));
            }
        }
        if (this.namedTag.contains("PoseIndex")) {
            this.setPose(this.namedTag.getInt("PoseIndex"));
        }
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 421 && !player.isAdventure() && item.hasCustomName()) {
            String string = item.getCustomName();
            this.namedTag.putString("CustomName", string);
            this.namedTag.putBoolean("CustomNameVisible", true);
            this.setNameTag(string);
            this.setNameTagVisible(true);
            return true;
        }
        if (player.isSneaking()) {
            if (this.getPose() >= 12) {
                this.setPose(0);
            } else {
                this.setPose(this.getPose() + 1);
            }
            this.sendData(this.getViewers().values().toArray(new Player[0]));
            return false;
        }
        if (this.isValid() && !player.isSpectator()) {
            int n = 0;
            boolean bl = !item.isNull();
            boolean bl2 = false;
            if (bl && item instanceof ItemArmor) {
                ItemArmor itemArmor = (ItemArmor)item;
                n = EntityArmorStand.a(itemArmor);
                bl2 = true;
            }
            if (bl && item.getId() == 397 || item.getId() == -155) {
                n = 0;
            }
            int n2 = 0;
            double d2 = vector3.y - this.y;
            boolean bl3 = false;
            if (d2 >= 0.1 && d2 < 0.55 && !this.l.getItemFast(3).isNull()) {
                n2 = 3;
                bl2 = true;
                bl3 = true;
            } else if (d2 >= 0.9 && d2 < 1.6 && !this.l.getItemFast(1).isNull()) {
                n2 = 1;
                bl2 = true;
                bl3 = true;
            } else if (d2 >= 0.4 && d2 < 1.2 && !this.l.getItemFast(2).isNull()) {
                n2 = 2;
                bl2 = true;
                bl3 = true;
            } else if (d2 >= 1.6 && !this.l.getItemFast(0).isNull()) {
                bl2 = true;
                bl3 = true;
            } else if (!this.k.getItemFast(n2).isNull()) {
                bl3 = true;
            }
            if (bl) {
                this.a(player, item, n, bl2);
            } else if (bl3) {
                this.a(player, item, n2, bl2);
            }
            return false;
        }
        return false;
    }

    private void a(Player player, Item item, int n, boolean bl) {
        Object object;
        Item item2;
        Item item3 = item2 = bl ? this.l.getItem(n) : this.k.getItem(n);
        if (item2.equals(item)) {
            return;
        }
        if (item.isNull()) {
            if (bl) {
                this.l.setItem(n, Item.get(0));
            } else {
                this.k.setItem(n, Item.get(0));
            }
        } else {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            object = item.clone();
            ((Item)object).setCount(1);
            if (bl) {
                this.l.setItem(n, (Item)object);
            } else {
                this.k.setItem(n, (Item)object);
            }
        }
        if (!item2.isNull()) {
            player.getInventory().addItem(item2);
        }
        object = this.getViewers().values();
        this.k.sendContents((Collection<Player>)object);
        this.l.sendContents((Collection<Player>)object);
    }

    public int getPose() {
        return this.n;
    }

    public void setPose(int n) {
        this.n = n;
        this.dataProperties.putInt(79, n);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.put("Mainhand", NBTIO.putItemHelper(this.k.getItemInHand()));
        this.namedTag.put("Offhand", NBTIO.putItemHelper(this.k.getOffHandItem()));
        if (this.l != null) {
            ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("Armor");
            for (int k = 0; k < 4; ++k) {
                listTag.add(NBTIO.putItemHelper(this.l.getItem(k), k));
            }
            this.namedTag.putList(listTag);
        }
        this.namedTag.putInt("PoseIndex", this.getPose());
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        this.k.sendContents(player);
        this.l.sendContents(player);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            entityDamageEvent.setCancelled(true);
        }
        if (!super.attack(entityDamageEvent)) {
            return false;
        }
        if (!entityDamageEvent.isCancelled() && !this.closed && this.isAlive()) {
            EntityDamageByEntityEvent entityDamageByEntityEvent;
            this.setGenericFlag(40, true);
            this.level.addParticle(new DestroyBlockParticle(this, Block.get(5)));
            this.kill();
            if (entityDamageEvent instanceof EntityDamageByEntityEvent && (entityDamageByEntityEvent = (EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
                Player player = (Player)entityDamageByEntityEvent.getDamager();
                if (player.isCreative()) {
                    this.close();
                    return true;
                }
                boolean bl = this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS);
                if (bl) {
                    this.level.dropItem(this, Item.get(425));
                }
                if (this.k != null) {
                    if (bl) {
                        this.k.getContents().values().forEach(item -> this.level.dropItem(this, (Item)item));
                    }
                    this.k.clearAll();
                }
                if (this.l != null) {
                    if (bl) {
                        this.l.getContents().values().forEach(item -> this.level.dropItem(this, (Item)item));
                    }
                    this.l.clearAll();
                }
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Armor Stand";
    }

    public EntityEquipmentInventory getEquipmentInventory() {
        return this.k;
    }

    @Override
    public EntityArmorInventory getInventory() {
        return this.l;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl = super.onUpdate(n);
        if (this.onGround && this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ()) == 0) {
            this.onGround = false;
        }
        if (!this.onGround) {
            this.motionY -= (double)this.getGravity();
        }
        if (this.move(this.motionX, this.motionY, this.motionZ)) {
            this.updateMovement();
        }
        this.motionX *= 0.9;
        this.motionY *= 0.9;
        this.motionZ *= 0.9;
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public String getInteractButtonText() {
        return "action.interact.armorstand.equip";
    }

    @Override
    public boolean canDoInteraction() {
        return true;
    }

    @Override
    public boolean goToNewChunk(FullChunk fullChunk) {
        if (fullChunk.getEntities().size() > 200) {
            if (!this.isClosed() && this.isAlive()) {
                boolean bl = this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS);
                if (bl) {
                    this.level.dropItem(this, Item.get(425));
                }
                if (this.k != null) {
                    if (bl) {
                        this.k.getContents().values().forEach(item -> this.level.dropItem(this, (Item)item));
                    }
                    this.k.clearAll();
                }
                if (this.l != null) {
                    if (bl) {
                        this.l.getContents().values().forEach(item -> this.level.dropItem(this, (Item)item));
                    }
                    this.l.clearAll();
                }
            }
            this.close();
            return false;
        }
        return true;
    }

    @Override
    public void setNameTag(String string) {
        this.m = string;
        if (this.namedTag.contains("CustomNameVisible") || this.namedTag.contains("CustomNameAlwaysVisible")) {
            this.setDataProperty(new StringEntityData(4, string));
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.m != null;
    }

    @Override
    public String getNameTag() {
        return this.m == null ? "" : this.m;
    }

    static {
        TAG_OFFHAND = "Offhand";
        TAG_MAINHAND = "Mainhand";
        TAG_POSE_INDEX = "PoseIndex";
        TAG_ARMOR = "Armor";
    }
}

