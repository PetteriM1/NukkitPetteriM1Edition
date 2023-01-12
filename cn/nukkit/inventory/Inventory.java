/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Inventory {
    public static final int MAX_STACK = 64;

    public int getSize();

    public int getMaxStackSize();

    public void setMaxStackSize(int var1);

    public String getName();

    public String getTitle();

    public Item getItem(int var1);

    default public Item getItemFast(int n) {
        return this.getItem(n);
    }

    default public boolean setItem(int n, Item item) {
        return this.setItem(n, item, true);
    }

    public boolean setItem(int var1, Item var2, boolean var3);

    public Item[] addItem(Item ... var1);

    public boolean canAddItem(Item var1);

    default public boolean allowedToAdd(int n) {
        return true;
    }

    public Item[] removeItem(Item ... var1);

    public Map<Integer, Item> getContents();

    public void setContents(Map<Integer, Item> var1);

    public void sendContents(Player var1);

    public void sendContents(Player ... var1);

    public void sendContents(Collection<Player> var1);

    public void sendSlot(int var1, Player var2);

    public void sendSlot(int var1, Player ... var2);

    public void sendSlot(int var1, Collection<Player> var2);

    public boolean contains(Item var1);

    public Map<Integer, Item> all(Item var1);

    default public int first(Item item) {
        return this.first(item, false);
    }

    public int first(Item var1, boolean var2);

    public int firstEmpty(Item var1);

    public void decreaseCount(int var1);

    public void remove(Item var1);

    default public boolean clear(int n) {
        return this.clear(n, true);
    }

    public boolean clear(int var1, boolean var2);

    public void clearAll();

    public boolean isFull();

    public boolean isEmpty();

    public Set<Player> getViewers();

    public InventoryType getType();

    public InventoryHolder getHolder();

    public void onOpen(Player var1);

    public boolean open(Player var1);

    public void close(Player var1);

    public void onClose(Player var1);

    public void onSlotChange(int var1, Item var2, boolean var3);
}

