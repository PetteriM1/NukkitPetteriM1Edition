/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.PlayerEnderChestInventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.PlayerOffhandInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public abstract class EntityHumanType
extends EntityCreature
implements InventoryHolder {
    protected PlayerInventory inventory;
    protected PlayerEnderChestInventory enderChestInventory;
    protected PlayerOffhandInventory offhandInventory;

    public EntityHumanType(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public PlayerInventory getInventory() {
        return this.inventory;
    }

    public PlayerEnderChestInventory getEnderChestInventory() {
        return this.enderChestInventory;
    }

    public PlayerOffhandInventory getOffhandInventory() {
        return this.offhandInventory;
    }

    @Override
    protected void initEntity() {
        ListTag<CompoundTag> listTag;
        this.inventory = new PlayerInventory(this);
        this.offhandInventory = new PlayerOffhandInventory(this);
        if (this.namedTag.contains("Inventory") && this.namedTag.get("Inventory") instanceof ListTag) {
            listTag = this.namedTag.getList("Inventory", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                int n = compoundTag.getByte("Slot");
                if (n >= 0 && n < 9) {
                    listTag.remove(compoundTag);
                    continue;
                }
                if (n >= 100 && n < 104) {
                    this.inventory.setItem(this.inventory.getSize() + n - 100, NBTIO.getItemHelper(compoundTag));
                    continue;
                }
                if (n == -106) {
                    this.offhandInventory.setItem(0, NBTIO.getItemHelper(compoundTag));
                    continue;
                }
                this.inventory.setItem(n - 9, NBTIO.getItemHelper(compoundTag));
            }
        }
        this.enderChestInventory = new PlayerEnderChestInventory(this);
        if (this.namedTag.contains("EnderItems") && this.namedTag.get("EnderItems") instanceof ListTag) {
            listTag = this.namedTag.getList("EnderItems", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                this.enderChestInventory.setItem(compoundTag.getByte("Slot"), NBTIO.getItemHelper(compoundTag));
            }
        }
        super.initEntity();
    }

    @Override
    public void saveNBT() {
        Item item;
        super.saveNBT();
        ListTag<CompoundTag> listTag = null;
        if (this.inventory != null) {
            Item item2;
            int n;
            int n2;
            listTag = new ListTag<CompoundTag>("Inventory");
            this.namedTag.putList(listTag);
            for (n2 = 0; n2 < 9; ++n2) {
                listTag.add(new CompoundTag().putByte("Count", 0).putShort("Damage", 0).putByte("Slot", n2).putByte("TrueSlot", -1).putShort("id", 0));
            }
            n2 = 45;
            for (n = 9; n < n2; ++n) {
                item2 = this.inventory.getItem(n - 9);
                listTag.add(NBTIO.putItemHelper(item2, n));
            }
            for (n = 100; n < 104; ++n) {
                item2 = this.inventory.getItem(this.inventory.getSize() + n - 100);
                if (item2 == null || item2.getId() == 0) continue;
                listTag.add(NBTIO.putItemHelper(item2, n));
            }
        }
        if (this.offhandInventory != null && (item = this.offhandInventory.getItem(0)).getId() != 0) {
            if (listTag == null) {
                listTag = new ListTag("Inventory");
                this.namedTag.putList(listTag);
            }
            listTag.add(NBTIO.putItemHelper(item, -106));
        }
        this.namedTag.putList(new ListTag("EnderItems"));
        if (this.enderChestInventory != null) {
            for (int k = 0; k < 27; ++k) {
                Item item3 = this.enderChestInventory.getItem(k);
                if (item3 == null || item3.getId() == 0) continue;
                this.namedTag.getList("EnderItems", CompoundTag.class).add(NBTIO.putItemHelper(item3, k));
            }
        }
    }

    @Override
    public Item[] getDrops() {
        if (this.inventory != null) {
            ArrayList<Item> arrayList = new ArrayList<Item>(this.inventory.getContents().values());
            arrayList.addAll(this.offhandInventory.getContents().values());
            return arrayList.toArray(new Item[0]);
        }
        return new Item[0];
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (!this.isAlive() || this.closed) {
            return false;
        }
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.VOID && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.CUSTOM && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.HUNGER) {
            int n = 0;
            int n2 = 0;
            Item[] object = this.inventory.getArmorContents();
            int n3 = object.length;
            for (int k = 0; k < n3; ++k) {
                Item item = object[k];
                n += item.getArmorPoints();
                n2 = (int)((double)n2 + this.calculateEnchantmentProtectionFactor(item, entityDamageEvent));
            }
            if (entityDamageEvent.canBeReducedByArmor()) {
                entityDamageEvent.setDamage(-entityDamageEvent.getFinalDamage() * (float)n * 0.04f, EntityDamageEvent.DamageModifier.ARMOR);
            }
            entityDamageEvent.setDamage(-entityDamageEvent.getFinalDamage() * (float)Math.min(NukkitMath.ceilFloat((float)Math.min(n2, 25) * ((float)Utils.random.nextInt(50, 100) / 100.0f)), 20) * 0.04f, EntityDamageEvent.DamageModifier.ARMOR_ENCHANTMENTS);
            if (super.attack(entityDamageEvent)) {
                void var4_7;
                Object var4_5 = null;
                if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                    Entity entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
                }
                if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.VOID && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.MAGIC && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.HUNGER && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.DROWNING && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.SUICIDE && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FALL) {
                    for (n3 = 0; n3 < 4; ++n3) {
                        Item item;
                        this.inventory.setArmorItem(n3, item, (item = this.damageArmor(this.inventory.getArmorItem(n3), (Entity)var4_7, entityDamageEvent.getDamage(), false, entityDamageEvent.getCause())).getId() != 0);
                    }
                } else if (var4_7 != null && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.THORNS) {
                    for (n3 = 0; n3 < 4; ++n3) {
                        Item item = this.inventory.getArmorItem(n3);
                        if (!item.hasEnchantments()) continue;
                        for (Enchantment enchantment : item.getEnchantments()) {
                            enchantment.doPostAttack((Entity)var4_7, this);
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return super.attack(entityDamageEvent);
    }

    protected Item damageArmor(Item item, Entity entity, float f2, boolean bl, EntityDamageEvent.DamageCause damageCause) {
        if (item.isUnbreakable() || item instanceof ItemSkull || item.getId() == -155) {
            return item;
        }
        if (item.hasEnchantments()) {
            Enchantment[] enchantmentArray;
            if (entity != null && damageCause != EntityDamageEvent.DamageCause.THORNS) {
                enchantmentArray = item.getEnchantments();
                int n = enchantmentArray.length;
                for (int k = 0; k < n; ++k) {
                    Enchantment enchantment = enchantmentArray[k];
                    enchantment.doPostAttack(entity, this);
                }
            }
            if ((enchantmentArray = item.getEnchantment(17)) != null && enchantmentArray.getLevel() > 0 && 100 / (enchantmentArray.getLevel() + 1) <= Utils.random.nextInt(100)) {
                return item;
            }
        }
        if (bl) {
            item.setDamage(item.getDamage() + (f2 >= 4.0f ? (int)f2 : 1));
        } else {
            item.setDamage(item.getDamage() + Math.max((int)(f2 / 4.0f), 1));
        }
        if (item.getDamage() >= item.getMaxDurability()) {
            return Item.get(0, 0, 0);
        }
        return item;
    }

    protected double calculateEnchantmentProtectionFactor(Item item, EntityDamageEvent entityDamageEvent) {
        if (!item.hasEnchantments()) {
            return 0.0;
        }
        double d2 = 0.0;
        for (Enchantment enchantment : item.getEnchantments()) {
            d2 += (double)enchantment.getProtectionFactor(entityDamageEvent);
        }
        return d2;
    }

    @Override
    public void setOnFire(int n) {
        int n2 = 0;
        for (Item item : this.inventory.getArmorContents()) {
            Enchantment enchantment = item.getEnchantment(1);
            if (enchantment == null || enchantment.getLevel() <= 0) continue;
            n2 = Math.max(n2, enchantment.getLevel());
        }
        n = (int)((double)n * (1.0 - (double)n2 * 0.15));
        super.setOnFire(n);
    }
}

