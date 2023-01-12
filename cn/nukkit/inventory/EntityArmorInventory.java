/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityArmorStand;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import java.util.HashSet;
import java.util.Set;

public class EntityArmorInventory
extends BaseInventory {
    private final EntityArmorStand b;
    private final Set<Player> c = new HashSet<Player>();
    public static final int SLOT_HEAD = 0;
    public static final int SLOT_CHEST = 1;
    public static final int SLOT_LEGS = 2;
    public static final int SLOT_FEET = 3;

    public EntityArmorInventory(EntityArmorStand entityArmorStand) {
        super(entityArmorStand, InventoryType.ENTITY_ARMOR);
        this.b = entityArmorStand;
    }

    @Override
    public InventoryHolder getHolder() {
        return this.holder;
    }

    @Override
    public String getName() {
        return "Entity Armor";
    }

    @Override
    public int getSize() {
        return 4;
    }

    public Item getHelmet() {
        return this.getItem(0);
    }

    public Item getChestplate() {
        return this.getItem(1);
    }

    public Item getLeggings() {
        return this.getItem(2);
    }

    public Item getBoots() {
        return this.getItem(3);
    }

    public void setHelmet(Item item) {
        this.setItem(1, item);
    }

    public void setChestplate(Item item) {
        this.setItem(1, item);
    }

    public void setLeggings(Item item) {
        this.setItem(2, item);
    }

    public void setBoots(Item item) {
        this.setItem(3, item);
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        for (Player player : playerArray) {
            this.sendSlot(n, player);
        }
    }

    @Override
    public void sendSlot(int n, Player player) {
        MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
        mobArmorEquipmentPacket.eid = this.b.getId();
        mobArmorEquipmentPacket.slots = new Item[]{this.getHelmet(), this.getChestplate(), this.getLeggings(), this.getBoots()};
        if (player == this.holder) {
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.inventoryId = player.getWindowId(this);
            inventorySlotPacket.slot = n;
            inventorySlotPacket.item = this.getItem(n);
            player.dataPacket(inventorySlotPacket);
        } else {
            player.dataPacket(mobArmorEquipmentPacket);
        }
    }

    @Override
    public void sendContents(Player ... playerArray) {
        for (Player player : playerArray) {
            this.sendContents(player);
        }
    }

    @Override
    public void sendContents(Player player) {
        MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
        mobArmorEquipmentPacket.eid = this.b.getId();
        mobArmorEquipmentPacket.slots = new Item[]{this.getHelmet(), this.getChestplate(), this.getLeggings(), this.getBoots()};
        if (player == this.holder) {
            InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.inventoryId = player.getWindowId(this);
            inventoryContentPacket.slots = new Item[]{this.getHelmet(), this.getChestplate(), this.getLeggings(), this.getBoots()};
            player.dataPacket(inventoryContentPacket);
        } else {
            player.dataPacket(mobArmorEquipmentPacket);
        }
    }

    @Override
    public void onOpen(Player player) {
        this.c.add(player);
    }

    @Override
    public void onClose(Player player) {
        this.c.remove(player);
    }

    @Override
    public Set<Player> getViewers() {
        return this.c;
    }
}

