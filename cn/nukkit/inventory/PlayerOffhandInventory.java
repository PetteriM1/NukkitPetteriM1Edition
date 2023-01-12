/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class PlayerOffhandInventory
extends BaseInventory {
    private static final IntSet b = new IntOpenHashSet(new int[]{0, 513, 262, 450, 358, 401, 465, 442});

    public PlayerOffhandInventory(EntityHumanType entityHumanType) {
        super(entityHumanType, InventoryType.OFFHAND);
    }

    @Override
    public void setSize(int n) {
        throw new UnsupportedOperationException("Offhand can only carry one item at a time");
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        EntityHuman entityHuman = this.getHolder();
        if (entityHuman instanceof Player && !((Player)entityHuman).spawned) {
            return;
        }
        this.sendContents(this.getViewers());
        this.sendContents(entityHuman.getViewers().values());
    }

    @Override
    public void sendContents(Player ... playerArray) {
        Item item = this.getItem(0);
        Item item2 = null;
        boolean bl = Server.getInstance().reduceTraffic;
        if (bl) {
            item2 = Item.get(item.getId(), item.getDamage(), 1);
            if (item.hasEnchantments()) {
                item2.addEnchantment(Enchantment.get(0));
            }
        }
        for (Player player : playerArray) {
            DataPacket dataPacket;
            if (player == this.getHolder()) {
                dataPacket = new InventoryContentPacket();
                dataPacket.inventoryId = 119;
                dataPacket.slots = new Item[]{item};
                player.dataPacket(dataPacket);
                continue;
            }
            dataPacket = this.a(bl ? item2 : item);
            player.dataPacket(dataPacket);
        }
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        Item item = this.getItem(0);
        Item item2 = null;
        boolean bl = Server.getInstance().reduceTraffic;
        if (bl) {
            item2 = Item.get(item.getId(), item.getDamage(), 1);
            if (item.hasEnchantments()) {
                item2.addEnchantment(Enchantment.get(0));
            }
        }
        for (Player player : playerArray) {
            DataPacket dataPacket;
            if (player == this.getHolder()) {
                dataPacket = new InventorySlotPacket();
                dataPacket.inventoryId = 119;
                dataPacket.item = item;
                player.dataPacket(dataPacket);
                continue;
            }
            dataPacket = this.a(bl ? item2 : item);
            player.dataPacket(dataPacket);
        }
    }

    private MobEquipmentPacket a(Item item) {
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getHolder().getId();
        mobEquipmentPacket.item = item;
        mobEquipmentPacket.inventorySlot = 1;
        mobEquipmentPacket.windowId = 119;
        return mobEquipmentPacket;
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman)super.getHolder();
    }

    @Override
    public boolean allowedToAdd(int n) {
        return b.contains(n);
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

