/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.event.entity.EntityInventoryChangeEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.CreativeContentPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import java.util.Collection;

public class PlayerInventory
extends BaseInventory {
    protected int itemInHandIndex = 0;

    public PlayerInventory(EntityHumanType entityHumanType) {
        super(entityHumanType, InventoryType.PLAYER);
    }

    @Override
    public int getSize() {
        return super.getSize() - 4;
    }

    @Override
    public void setSize(int n) {
        super.setSize(n + 4);
        this.sendContents(this.getViewers());
    }

    public boolean equipItem(int n) {
        if (!this.a(n)) {
            Server.getInstance().getLogger().debug(this.getHolder().getName() + ": equipItem: slot is not a hotbar slot: " + n);
            return false;
        }
        if (this.getHolder() instanceof Player) {
            Player player = (Player)this.getHolder();
            PlayerItemHeldEvent playerItemHeldEvent = new PlayerItemHeldEvent(player, this.getItem(n), n);
            this.getHolder().getLevel().getServer().getPluginManager().callEvent(playerItemHeldEvent);
            if (playerItemHeldEvent.isCancelled()) {
                this.sendContents(this.getViewers());
                return false;
            }
            if (player.fishing != null && !this.getItem(n).equals(player.fishing.rod)) {
                player.stopFishing(false);
            }
        }
        this.setHeldItemIndex(n, false);
        return true;
    }

    private boolean a(int n) {
        return n >= 0 && n < this.getHotbarSize();
    }

    public int getHotbarSlotIndex(int n) {
        return n;
    }

    public void setHotbarSlotIndex(int n, int n2) {
    }

    public int getHeldItemIndex() {
        return this.itemInHandIndex;
    }

    public void setHeldItemIndex(int n) {
        this.setHeldItemIndex(n, true);
    }

    public void setHeldItemIndex(int n, boolean bl) {
        if (n >= 0 && n < this.getHotbarSize()) {
            this.itemInHandIndex = n;
            if (this.getHolder() instanceof Player && bl) {
                this.sendHeldItem((Player)this.getHolder());
            }
            this.sendHeldItem(this.getHolder().getViewers().values());
        }
    }

    public Item getItemInHand() {
        Item item = this.getItem(this.itemInHandIndex);
        if (item != null) {
            return item;
        }
        return new ItemBlock(Block.get(0), 0, 0);
    }

    public Item getItemInHandFast() {
        Item item = this.getItemFast(this.getHeldItemIndex());
        if (item != null) {
            return item;
        }
        return this.a;
    }

    public boolean setItemInHand(Item item) {
        return this.setItem(this.itemInHandIndex, item);
    }

    public int getHeldItemSlot() {
        return this.itemInHandIndex;
    }

    public void setHeldItemSlot(int n) {
        if (!this.a(n)) {
            return;
        }
        this.itemInHandIndex = n;
        if (this.getHolder() instanceof Player) {
            this.sendHeldItem((Player)this.getHolder());
        }
        this.sendHeldItem(this.getViewers());
    }

    public void sendHeldItem(Player ... playerArray) {
        Item item = this.getItemInHandFast();
        Item item2 = null;
        boolean bl = Server.getInstance().reduceTraffic;
        if (bl) {
            item2 = Item.get(item.getId(), item.getDamage(), 1);
            if (item.hasEnchantments()) {
                item2.addEnchantment(Enchantment.get(0));
            }
        }
        for (Player player : playerArray) {
            MobEquipmentPacket mobEquipmentPacket;
            if (player.equals(this.getHolder())) {
                this.sendSlot(this.itemInHandIndex, player);
                mobEquipmentPacket = new MobEquipmentPacket();
                mobEquipmentPacket.item = item;
                mobEquipmentPacket.inventorySlot = mobEquipmentPacket.hotbarSlot = this.itemInHandIndex;
                mobEquipmentPacket.eid = this.getHolder().getId();
                player.dataPacket(mobEquipmentPacket);
                continue;
            }
            mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.item = bl ? item2 : item;
            mobEquipmentPacket.inventorySlot = mobEquipmentPacket.hotbarSlot = this.itemInHandIndex;
            mobEquipmentPacket.eid = this.getHolder().getId();
            player.dataPacket(mobEquipmentPacket);
        }
    }

    public void sendHeldItemIfNotAir(Player player) {
        if (!this.getHolder().equals(player)) {
            throw new IllegalArgumentException("sendHeldItemIfNotAir: invalid player, expected the inventory holder");
        }
        Item item = this.getItemInHandFast();
        if (item.getId() != 0) {
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.slot = this.itemInHandIndex;
            inventorySlotPacket.item = item.clone();
            inventorySlotPacket.inventoryId = 0;
            player.dataPacket(inventorySlotPacket);
        }
    }

    public void sendHeldItem(Collection<Player> collection) {
        this.sendHeldItem(collection.toArray(new Player[0]));
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        EntityHuman entityHuman = this.getHolder();
        if (entityHuman instanceof Player && !((Player)entityHuman).spawned) {
            return;
        }
        if (n >= this.getSize()) {
            this.sendArmorSlot(n, this.getViewers());
            this.sendArmorSlot(n, this.getHolder().getViewers().values());
        } else {
            super.onSlotChange(n, item, bl);
        }
    }

    public int getHotbarSize() {
        return 9;
    }

    public Item getArmorItem(int n) {
        return this.getItem(this.getSize() + n);
    }

    public boolean setArmorItem(int n, Item item) {
        return this.setArmorItem(n, item, false);
    }

    public boolean setArmorItem(int n, Item item, boolean bl) {
        return this.setItem(this.getSize() + n, item, bl);
    }

    public Item getHelmet() {
        return this.getItem(this.getSize());
    }

    public Item getHelmetFast() {
        return this.getItemFast(36);
    }

    public Item getChestplate() {
        return this.getItem(this.getSize() + 1);
    }

    public Item getChestplateFast() {
        return this.getItemFast(37);
    }

    public Item getLeggings() {
        return this.getItem(this.getSize() + 2);
    }

    public Item getLeggingsFast() {
        return this.getItemFast(38);
    }

    public Item getBoots() {
        return this.getItem(this.getSize() + 3);
    }

    public Item getBootsFast() {
        return this.getItemFast(39);
    }

    public boolean setHelmet(Item item) {
        return this.setItem(this.getSize(), item);
    }

    public boolean setChestplate(Item item) {
        return this.setItem(this.getSize() + 1, item);
    }

    public boolean setLeggings(Item item) {
        return this.setItem(this.getSize() + 2, item);
    }

    public boolean setBoots(Item item) {
        return this.setItem(this.getSize() + 3, item);
    }

    @Override
    public boolean setItem(int n, Item item) {
        return this.setItem(n, item, true);
    }

    @Override
    public boolean setItem(int n, Item item, boolean bl) {
        Object object;
        if (n < 0 || n >= this.size) {
            return false;
        }
        if (item.getId() == 0 || item.getCount() <= 0) {
            return this.clear(n, bl);
        }
        if (n >= this.getSize()) {
            object = new EntityArmorChangeEvent(this.getHolder(), this.getItem(n), item, n);
            Server.getInstance().getPluginManager().callEvent((Event)object);
            if (((Event)object).isCancelled() && this.getHolder() != null) {
                this.sendArmorSlot(n, this.getViewers());
                return false;
            }
            item = ((EntityArmorChangeEvent)object).getNewItem();
        } else {
            object = new EntityInventoryChangeEvent(this.getHolder(), this.getItem(n), item, n);
            Server.getInstance().getPluginManager().callEvent((Event)object);
            if (((Event)object).isCancelled()) {
                this.sendSlot(n, this.getViewers());
                return false;
            }
            item = ((EntityInventoryChangeEvent)object).getNewItem();
        }
        object = this.getItem(n);
        this.slots.put(n, item.clone());
        this.onSlotChange(n, (Item)object, bl);
        return true;
    }

    @Override
    public boolean clear(int n, boolean bl) {
        if (this.slots.containsKey(n)) {
            Item item = new ItemBlock(Block.get(0), null, 0);
            Item item2 = (Item)this.slots.get(n);
            if (n >= this.getSize() && n < this.size) {
                EntityArmorChangeEvent entityArmorChangeEvent = new EntityArmorChangeEvent(this.getHolder(), item2, item, n);
                Server.getInstance().getPluginManager().callEvent(entityArmorChangeEvent);
                if (entityArmorChangeEvent.isCancelled()) {
                    if (n >= this.size) {
                        this.sendArmorSlot(n, this.getViewers());
                    } else {
                        this.sendSlot(n, this.getViewers());
                    }
                    return false;
                }
                item = entityArmorChangeEvent.getNewItem();
            } else {
                EntityInventoryChangeEvent entityInventoryChangeEvent = new EntityInventoryChangeEvent(this.getHolder(), item2, item, n);
                Server.getInstance().getPluginManager().callEvent(entityInventoryChangeEvent);
                if (entityInventoryChangeEvent.isCancelled()) {
                    if (n >= this.size) {
                        this.sendArmorSlot(n, this.getViewers());
                    } else {
                        this.sendSlot(n, this.getViewers());
                    }
                    return false;
                }
                item = entityInventoryChangeEvent.getNewItem();
            }
            if (item.getId() != 0) {
                this.slots.put(n, item.clone());
            } else {
                this.slots.remove(n);
            }
            this.onSlotChange(n, item2, bl);
        }
        return true;
    }

    public Item[] getArmorContents() {
        Item[] itemArray = new Item[4];
        for (int k = 0; k < 4; ++k) {
            itemArray[k] = this.getItem(this.getSize() + k);
        }
        return itemArray;
    }

    @Override
    public void clearAll() {
        int n = this.getSize() + 4;
        for (int k = 0; k < n; ++k) {
            this.clear(k);
        }
        this.getHolder().getOffhandInventory().clearAll();
    }

    public void sendArmorContents(Player player) {
        this.sendArmorContents(new Player[]{player});
    }

    public void sendArmorContents(Player[] playerArray) {
        Item[] itemArray = this.getArmorContents();
        for (Player player : playerArray) {
            DataPacket dataPacket;
            if (player.equals(this.getHolder())) {
                dataPacket = new InventoryContentPacket();
                dataPacket.inventoryId = 120;
                dataPacket.slots = itemArray;
                player.dataPacket(dataPacket);
                continue;
            }
            dataPacket = new MobArmorEquipmentPacket();
            ((MobArmorEquipmentPacket)dataPacket).eid = this.getHolder().getId();
            ((MobArmorEquipmentPacket)dataPacket).slots = itemArray;
            player.dataPacket(dataPacket);
        }
    }

    public void sendArmorContentsIfNotAr(Player player) {
        Item[] itemArray = this.getArmorContents();
        if (itemArray[0].getId() != 0 || itemArray[1].getId() != 0 || itemArray[2].getId() != 0 || itemArray[3].getId() != 0) {
            MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
            mobArmorEquipmentPacket.eid = this.getHolder().getId();
            mobArmorEquipmentPacket.slots = itemArray;
            player.dataPacket(mobArmorEquipmentPacket);
        }
    }

    public void setArmorContents(Item[] itemArray) {
        if (itemArray.length < 4) {
            Item[] itemArray2 = new Item[4];
            System.arraycopy(itemArray, 0, itemArray2, 0, itemArray.length);
            itemArray = itemArray2;
        }
        for (int k = 0; k < 4; ++k) {
            if (itemArray[k] == null) {
                itemArray[k] = new ItemBlock(Block.get(0), null, 0);
            }
            if (itemArray[k].getId() == 0) {
                this.clear(this.getSize() + k);
                continue;
            }
            this.setItem(this.getSize() + k, itemArray[k]);
        }
    }

    public void sendArmorContents(Collection<Player> collection) {
        this.sendArmorContents(collection.toArray(new Player[0]));
    }

    public void sendArmorSlot(int n, Player player) {
        this.sendArmorSlot(n, new Player[]{player});
    }

    public void sendArmorSlot(int n, Player[] playerArray) {
        Item[] itemArray = this.getArmorContents();
        for (Player player : playerArray) {
            DataPacket dataPacket;
            if (player.equals(this.getHolder())) {
                dataPacket = new InventorySlotPacket();
                dataPacket.inventoryId = 120;
                dataPacket.slot = n - this.getSize();
                dataPacket.item = this.getItem(n);
                player.dataPacket(dataPacket);
                continue;
            }
            dataPacket = new MobArmorEquipmentPacket();
            ((MobArmorEquipmentPacket)dataPacket).eid = this.getHolder().getId();
            ((MobArmorEquipmentPacket)dataPacket).slots = itemArray;
            player.dataPacket(dataPacket);
        }
    }

    public void sendArmorSlot(int n, Collection<Player> collection) {
        this.sendArmorSlot(n, collection.toArray(new Player[0]));
    }

    @Override
    public void sendContents(Player player) {
        this.sendContents(new Player[]{player});
    }

    @Override
    public void sendContents(Collection<Player> collection) {
        this.sendContents(collection.toArray(new Player[0]));
    }

    @Override
    public void sendContents(Player[] playerArray) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.slots = new Item[this.getSize()];
        for (int k = 0; k < this.getSize(); ++k) {
            inventoryContentPacket.slots[k] = this.getItem(k);
        }
        for (Player player : playerArray) {
            int n = player.getWindowId(this);
            if (n == -1) {
                if (this.getHolder() == player) continue;
                this.close(player);
                continue;
            }
            inventoryContentPacket.inventoryId = n;
            player.dataPacket(inventoryContentPacket);
        }
    }

    @Override
    public void sendSlot(int n, Player player) {
        this.sendSlot(n, new Player[]{player});
    }

    @Override
    public void sendSlot(int n, Collection<Player> collection) {
        this.sendSlot(n, collection.toArray(new Player[0]));
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.slot = n;
        inventorySlotPacket.item = this.getItem(n);
        for (Player player : playerArray) {
            if (player.equals(this.getHolder())) {
                inventorySlotPacket.inventoryId = 0;
            } else {
                int n2 = player.getWindowId(this);
                if (n2 == -1) {
                    this.close(player);
                    continue;
                }
                inventorySlotPacket.inventoryId = n2;
            }
            player.dataPacket(inventorySlotPacket);
        }
    }

    public void sendCreativeContents() {
        if (!(this.getHolder() instanceof Player)) {
            return;
        }
        Player player = (Player)this.getHolder();
        if (player.protocol < 407) {
            InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.inventoryId = 121;
            if (!player.isSpectator()) {
                inventoryContentPacket.slots = Item.getCreativeItems(player.protocol).toArray(new Item[0]);
            }
            player.dataPacket(inventoryContentPacket);
        } else {
            CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
            creativeContentPacket.entries = player.isSpectator() ? new Item[]{} : Item.getCreativeItems(player.protocol).toArray(new Item[0]);
            player.dataPacket(creativeContentPacket);
        }
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman)super.getHolder();
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.windowId = player.getWindowId(this);
        containerOpenPacket.type = this.getType().getNetworkType();
        containerOpenPacket.x = player.getFloorX();
        containerOpenPacket.y = player.getFloorY();
        containerOpenPacket.z = player.getFloorZ();
        containerOpenPacket.entityId = player.getId();
        player.dataPacket(containerOpenPacket);
    }

    @Override
    public void onClose(Player player) {
        block0: {
            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.windowId = player.getWindowId(this);
            containerClosePacket.wasServerInitiated = player.getClosingWindowId() != containerClosePacket.windowId;
            player.dataPacket(containerClosePacket);
            if (player == this.holder) break block0;
            super.onClose(player);
        }
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

