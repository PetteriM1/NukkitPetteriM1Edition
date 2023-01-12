/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityInventoryChangeEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.inventory.DoubleChestInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class BaseInventory
implements Inventory {
    protected final InventoryType type;
    protected int maxStackSize = 64;
    protected int size;
    protected final String name;
    protected final String title;
    public final Map<Integer, Item> slots = new HashMap<Integer, Item>();
    protected final Set<Player> viewers = new HashSet<Player>();
    protected InventoryHolder holder;
    final Item a = new ItemBlock(Block.get(0, null), 0, 0);

    public BaseInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        this(inventoryHolder, inventoryType, new HashMap<Integer, Item>());
    }

    public BaseInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map) {
        this(inventoryHolder, inventoryType, map, null);
    }

    public BaseInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n) {
        this(inventoryHolder, inventoryType, map, n, null);
    }

    public BaseInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n, String string) {
        this.holder = inventoryHolder;
        this.type = inventoryType;
        this.size = n != null ? n.intValue() : this.type.getDefaultSize();
        this.title = string != null ? string : this.type.getDefaultTitle();
        this.name = this.type.getDefaultTitle();
        if (!(this instanceof DoubleChestInventory)) {
            this.setContents(map);
        }
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public void setSize(int n) {
        this.size = n;
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Item getItem(int n) {
        Item item = this.slots.get(n);
        return item == null ? new ItemBlock(Block.get(0), null, 0) : item.clone();
    }

    @Override
    public Item getItemFast(int n) {
        Item item = this.slots.get(n);
        return item == null ? this.a : item;
    }

    @Override
    public Map<Integer, Item> getContents() {
        return new HashMap<Integer, Item>(this.slots);
    }

    @Override
    public void setContents(Map<Integer, Item> map) {
        if (map.size() > this.size) {
            map = new TreeMap<Integer, Item>(map);
            TreeMap<Integer, Item> treeMap = new TreeMap<Integer, Item>();
            int n = 0;
            for (Map.Entry<Integer, Item> entry : map.entrySet()) {
                treeMap.put(entry.getKey(), entry.getValue());
                if (++n < this.size) continue;
                break;
            }
            map = treeMap;
        }
        for (int k = 0; k < this.size; ++k) {
            if (!map.containsKey(k)) {
                if (!this.slots.containsKey(k)) continue;
                this.clear(k);
                continue;
            }
            if (this.setItem(k, map.get(k))) continue;
            this.clear(k);
        }
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
        InventoryHolder inventoryHolder = this.getHolder();
        if (inventoryHolder instanceof Entity) {
            object = new EntityInventoryChangeEvent((Entity)((Object)inventoryHolder), this.getItem(n), item, n);
            Server.getInstance().getPluginManager().callEvent((Event)object);
            if (((Event)object).isCancelled()) {
                this.sendSlot(n, this.getViewers());
                return false;
            }
            item = ((EntityInventoryChangeEvent)object).getNewItem();
        }
        if (inventoryHolder instanceof BlockEntity) {
            ((BlockEntity)((Object)inventoryHolder)).setDirty();
        }
        object = this.getItem(n);
        this.slots.put(n, item.clone());
        this.onSlotChange(n, (Item)object, bl);
        return true;
    }

    @Override
    public boolean contains(Item item) {
        int n = Math.max(1, item.getCount());
        boolean bl = item.hasMeta() && item.getDamage() >= 0;
        boolean bl2 = item.getCompoundTag() != null;
        for (Item item2 : this.getContents().values()) {
            if (!item.equals(item2, bl, bl2) || (n -= item2.getCount()) > 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public Map<Integer, Item> all(Item item) {
        HashMap<Integer, Item> hashMap = new HashMap<Integer, Item>();
        boolean bl = item.hasMeta() && item.getDamage() >= 0;
        boolean bl2 = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (!item.equals(entry.getValue(), bl, bl2)) continue;
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    @Override
    public void remove(Item item) {
        boolean bl = item.hasMeta();
        boolean bl2 = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (!item.equals(entry.getValue(), bl, bl2)) continue;
            this.clear(entry.getKey());
        }
    }

    @Override
    public int first(Item item, boolean bl) {
        int n = Math.max(1, item.getCount());
        boolean bl2 = item.hasMeta();
        boolean bl3 = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (!item.equals(entry.getValue(), bl2, bl3) || entry.getValue().getCount() != n && (bl || entry.getValue().getCount() <= n)) continue;
            return entry.getKey();
        }
        return -1;
    }

    @Override
    public int firstEmpty(Item item) {
        for (int k = 0; k < this.size; ++k) {
            if (this.getItemFast(k).getId() != 0) continue;
            return k;
        }
        return -1;
    }

    @Override
    public void decreaseCount(int n) {
        Item item = this.getItem(n);
        if (item.getCount() > 0) {
            --item.count;
            this.setItem(n, item);
        }
    }

    @Override
    public boolean canAddItem(Item item) {
        int n = item.getCount();
        boolean bl = item.hasMeta();
        boolean bl2 = item.getCompoundTag() != null;
        int n2 = this.getSize();
        for (int k = 0; k < n2; ++k) {
            Item item2 = this.getItemFast(k);
            if (item.equals(item2, bl, bl2)) {
                int n3 = item2.getMaxStackSize() - item2.getCount();
                if (n3 > 0) {
                    n -= n3;
                }
            } else if (item2.getId() == 0) {
                n -= this.getMaxStackSize();
            }
            if (n > 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public Item[] addItem(Item ... itemArray) {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (Item object : itemArray) {
            if (object.getId() == 0 || object.getCount() <= 0) continue;
            arrayList.add(object.clone());
        }
        IntArrayList intArrayList = new IntArrayList();
        for (int k = 0; k < this.getSize(); ++k) {
            Item item = this.getItem(k);
            if (item.getId() == 0 || item.getCount() <= 0) {
                intArrayList.add(k);
            }
            for (Item item2 : new ArrayList(arrayList)) {
                if (!item2.equals(item) || item.getCount() >= item.getMaxStackSize()) continue;
                int n = Math.min(item.getMaxStackSize() - item.getCount(), item2.getCount());
                if ((n = Math.min(n, this.maxStackSize)) <= 0) continue;
                item2.setCount(item2.getCount() - n);
                item.setCount(item.getCount() + n);
                this.setItem(k, item);
                if (item2.getCount() > 0) continue;
                arrayList.remove(item2);
            }
            if (arrayList.isEmpty()) break;
        }
        if (!arrayList.isEmpty() && !intArrayList.isEmpty()) {
            IntListIterator intListIterator = intArrayList.iterator();
            while (intListIterator.hasNext()) {
                int n = (Integer)intListIterator.next();
                if (arrayList.isEmpty()) continue;
                Item item = (Item)arrayList.get(0);
                int n2 = Math.min(item.getMaxStackSize(), item.getCount());
                n2 = Math.min(n2, this.maxStackSize);
                item.setCount(item.getCount() - n2);
                Item item3 = item.clone();
                item3.setCount(n2);
                this.setItem(n, item3);
                if (item.getCount() > 0) continue;
                arrayList.remove(item);
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public Item[] removeItem(Item ... itemArray) {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (Item item2 : itemArray) {
            if (item2.getId() == 0 || item2.getCount() <= 0) continue;
            arrayList.add(item2.clone());
        }
        for (int k = 0; k < this.size; ++k) {
            Item item = this.getItem(k);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            for (Item item2 : new ArrayList(arrayList)) {
                if (!item2.equals(item, item.hasMeta(), item.getCompoundTag() != null)) continue;
                int n = Math.min(item.getCount(), item2.getCount());
                item2.setCount(item2.getCount() - n);
                item.setCount(item.getCount() - n);
                this.setItem(k, item);
                if (item2.getCount() > 0) continue;
                arrayList.remove(item2);
            }
            if (arrayList.isEmpty()) break;
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public boolean clear(int n, boolean bl) {
        if (this.slots.containsKey(n)) {
            Item item = new ItemBlock(Block.get(0), null, 0);
            Item item2 = this.slots.get(n);
            InventoryHolder inventoryHolder = this.getHolder();
            if (inventoryHolder instanceof Entity) {
                EntityInventoryChangeEvent entityInventoryChangeEvent = new EntityInventoryChangeEvent((Entity)((Object)inventoryHolder), item2, item, n);
                Server.getInstance().getPluginManager().callEvent(entityInventoryChangeEvent);
                if (entityInventoryChangeEvent.isCancelled()) {
                    this.sendSlot(n, this.getViewers());
                    return false;
                }
                item = entityInventoryChangeEvent.getNewItem();
            }
            if (inventoryHolder instanceof BlockEntity) {
                ((BlockEntity)((Object)inventoryHolder)).setDirty();
            }
            if (item.getId() != 0) {
                this.slots.put(n, ((Item)item).clone());
            } else {
                this.slots.remove(n);
            }
            this.onSlotChange(n, item2, bl);
        }
        return true;
    }

    @Override
    public void clearAll() {
        for (Integer n : this.getContents().keySet()) {
            this.clear(n);
        }
    }

    @Override
    public Set<Player> getViewers() {
        return this.viewers;
    }

    @Override
    public InventoryHolder getHolder() {
        return this.holder;
    }

    @Override
    public void setMaxStackSize(int n) {
        this.maxStackSize = n;
    }

    @Override
    public boolean open(Player player) {
        InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(this, player);
        player.getServer().getPluginManager().callEvent(inventoryOpenEvent);
        if (inventoryOpenEvent.isCancelled()) {
            return false;
        }
        this.onOpen(player);
        return true;
    }

    @Override
    public void close(Player player) {
        this.onClose(player);
    }

    @Override
    public void onOpen(Player player) {
        this.viewers.add(player);
    }

    @Override
    public void onClose(Player player) {
        this.viewers.remove(player);
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        if (bl) {
            this.sendSlot(n, this.getViewers());
        }
    }

    @Override
    public void sendContents(Player player) {
        this.sendContents(new Player[]{player});
    }

    @Override
    public void sendContents(Player ... playerArray) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.slots = new Item[this.getSize()];
        for (int k = 0; k < this.getSize(); ++k) {
            inventoryContentPacket.slots[k] = this.getItem(k);
        }
        for (Player player : playerArray) {
            int n = player.getWindowId(this);
            if (n == -1) {
                this.close(player);
                continue;
            }
            inventoryContentPacket.inventoryId = n;
            player.dataPacket(inventoryContentPacket);
        }
    }

    @Override
    public boolean isFull() {
        if (this.slots.size() < this.getSize()) {
            return false;
        }
        for (Item item : this.slots.values()) {
            if (item != null && item.getId() != 0 && item.getCount() >= item.getMaxStackSize() && item.getCount() >= this.maxStackSize) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        if (this.maxStackSize <= 0) {
            return false;
        }
        for (Item item : this.slots.values()) {
            if (item == null || item.getId() == 0 || item.getCount() <= 0) continue;
            return false;
        }
        return true;
    }

    public int getFreeSpace(Item item) {
        int n = Math.min(item.getMaxStackSize(), this.maxStackSize);
        int n2 = (this.getSize() - this.slots.size()) * n;
        for (Item item2 : this.getContents().values()) {
            if (item2 == null || item2.getId() == 0) {
                n2 += n;
                continue;
            }
            if (!item2.equals(item, true, true)) continue;
            n2 += n - item2.getCount();
        }
        return n2;
    }

    @Override
    public void sendContents(Collection<Player> collection) {
        this.sendContents(collection.toArray(new Player[0]));
    }

    @Override
    public void sendSlot(int n, Player player) {
        this.a(n, player);
    }

    private void a(int n, Player player) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.slot = n;
        inventorySlotPacket.item = this.getItem(n);
        int n2 = player.getWindowId(this);
        if (n2 == -1) {
            this.close(player);
            return;
        }
        inventorySlotPacket.inventoryId = n2;
        player.dataPacket(inventorySlotPacket);
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.slot = n;
        inventorySlotPacket.item = this.getItem(n);
        for (Player player : playerArray) {
            int n2 = player.getWindowId(this);
            if (n2 == -1) {
                this.close(player);
                continue;
            }
            inventorySlotPacket.inventoryId = n2;
            player.dataPacket(inventorySlotPacket);
        }
    }

    @Override
    public void sendSlot(int n, Collection<Player> collection) {
        this.sendSlot(n, collection.toArray(new Player[0]));
    }

    @Override
    public InventoryType getType() {
        return this.type;
    }
}

