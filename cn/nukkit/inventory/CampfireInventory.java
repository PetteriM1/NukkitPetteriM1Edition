/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityCampfire;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;

public class CampfireInventory
extends ContainerInventory {
    public CampfireInventory(BlockEntityCampfire blockEntityCampfire) {
        super(blockEntityCampfire, InventoryType.CAMPFIRE);
    }

    public CampfireInventory(BlockEntityCampfire blockEntityCampfire, InventoryType inventoryType) {
        super(blockEntityCampfire, inventoryType);
    }

    @Override
    public BlockEntityCampfire getHolder() {
        return (BlockEntityCampfire)this.holder;
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        super.onSlotChange(n, item, bl);
        this.getHolder().scheduleUpdate();
        this.getHolder().spawnToAll();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
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
                int n3 = 1 - item2.getCount();
                if (n3 > 0) {
                    n -= n3;
                }
            } else if (item2.getId() == 0) {
                --n;
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
                if (!item2.equals(item) || item.getCount() >= 1) continue;
                int n = Math.min(1 - item.getCount(), item2.getCount());
                if ((n = Math.min(n, 1)) <= 0) continue;
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
                int n2 = Math.min(1, item.getCount());
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
}

