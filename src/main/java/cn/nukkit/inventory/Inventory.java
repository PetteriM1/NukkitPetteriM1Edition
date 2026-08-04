package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface Inventory {

    int MAX_STACK = 64;

    void setContents(Map<Integer, Item> items);

    void setMaxStackSize(int size);

    Map<Integer, Item> getContents();

    InventoryHolder getHolder();

    int getMaxStackSize();

    String getName();

    int getSize();

    String getTitle();

    InventoryType getType();

    Set<Player> getViewers();

    boolean isEmpty();

    boolean isFull();

    Item[] addItem(Item... slots);

    Map<Integer, Item> all(Item item);

    default boolean allowedToAdd(Item item) {
        return true;
    }

    boolean canAddItem(Item item);

    default boolean clear(int index) {
        return clear(index, true);
    }

    boolean clear(int index, boolean send);

    void clearAll();

    void close(Player who);

    boolean contains(Item item);

    void decreaseCount(int slot);

    default int first(Item item) {
        return first(item, false);
    }

    int first(Item item, boolean exact);

    int firstEmpty(Item item);

    Item getItem(int index);

    default Item getItemFast(int index) {
        return getItem(index);
    }

    void onClose(Player who);

    void onOpen(Player who);

    void onSlotChange(int index, Item before, boolean send);

    boolean open(Player who);

    void remove(Item item);

    Item[] removeItem(Item... slots);

    void sendContents(Player player);

    void sendContents(Player... players);

    void sendContents(Collection<Player> players);

    void sendSlot(int index, Player player);

    void sendSlot(int index, Player... players);

    void sendSlot(int index, Collection<Player> players);

    default boolean setItem(int index, Item item) {
        return setItem(index, item, true);
    }

    boolean setItem(int index, Item item, boolean send);
}
