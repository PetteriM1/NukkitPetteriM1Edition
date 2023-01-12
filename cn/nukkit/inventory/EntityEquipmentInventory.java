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
import cn.nukkit.network.protocol.MobEquipmentPacket;

public class EntityEquipmentInventory
extends BaseInventory {
    private final EntityArmorStand c;
    private static final int b = 0;
    private static final int d = 1;

    public EntityEquipmentInventory(EntityArmorStand entityArmorStand) {
        super(entityArmorStand, InventoryType.ENTITY_EQUIPMENT);
        this.c = entityArmorStand;
    }

    @Override
    public String getName() {
        return "Entity Equipment";
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public InventoryHolder getHolder() {
        return this.holder;
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        for (Player player : playerArray) {
            this.sendSlot(n, player);
        }
    }

    @Override
    public void sendSlot(int n, Player player) {
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.c.getId();
        mobEquipmentPacket.inventorySlot = mobEquipmentPacket.hotbarSlot = n;
        mobEquipmentPacket.item = this.getItem(n);
        player.dataPacket(mobEquipmentPacket);
    }

    public Item getItemInHand() {
        return this.getItem(0);
    }

    public Item getOffHandItem() {
        return this.getItem(1);
    }

    public boolean setItemInHand(Item item, boolean bl) {
        return this.setItem(0, item, bl);
    }

    public boolean setOffhandItem(Item item, boolean bl) {
        return this.setItem(1, item, bl);
    }

    @Override
    public void sendContents(Player player) {
        this.sendSlot(0, player);
        this.sendSlot(1, player);
    }

    @Override
    public void sendContents(Player ... playerArray) {
        for (Player player : playerArray) {
            this.sendContents(player);
        }
    }
}

