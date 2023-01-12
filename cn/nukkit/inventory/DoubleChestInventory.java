/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import java.util.HashMap;
import java.util.Map;

public class DoubleChestInventory
extends ContainerInventory
implements InventoryHolder {
    private final ChestInventory c;
    private final ChestInventory b;

    public DoubleChestInventory(BlockEntityChest blockEntityChest, BlockEntityChest blockEntityChest2) {
        super(null, InventoryType.DOUBLE_CHEST);
        int n;
        this.holder = this;
        this.c = blockEntityChest.getRealInventory();
        this.c.setDoubleInventory(this);
        this.b = blockEntityChest2.getRealInventory();
        this.b.setDoubleInventory(this);
        HashMap<Integer, Item> hashMap = new HashMap<Integer, Item>();
        for (n = 0; n < this.c.getSize(); ++n) {
            if (!this.c.getContents().containsKey(n)) continue;
            hashMap.put(n, this.c.getContents().get(n));
        }
        for (n = 0; n < this.b.getSize(); ++n) {
            if (!this.b.getContents().containsKey(n)) continue;
            hashMap.put(n + this.c.getSize(), this.b.getContents().get(n));
        }
        this.setContents(hashMap);
    }

    @Override
    public Inventory getInventory() {
        return this;
    }

    @Override
    public BlockEntityChest getHolder() {
        return this.c.getHolder();
    }

    @Override
    public Item getItem(int n) {
        return n < this.c.getSize() ? this.c.getItem(n) : this.b.getItem(n - this.b.getSize());
    }

    @Override
    public Item getItemFast(int n) {
        return n < this.c.getSize() ? this.c.getItemFast(n) : this.b.getItemFast(n - this.b.getSize());
    }

    @Override
    public boolean setItem(int n, Item item, boolean bl) {
        return n < this.c.getSize() ? this.c.setItem(n, item, bl) : this.b.setItem(n - this.b.getSize(), item, bl);
    }

    @Override
    public boolean clear(int n) {
        return n < this.c.getSize() ? this.c.clear(n) : this.b.clear(n - this.b.getSize());
    }

    @Override
    public Map<Integer, Item> getContents() {
        HashMap<Integer, Item> hashMap = new HashMap<Integer, Item>();
        for (int k = 0; k < this.getSize(); ++k) {
            hashMap.put(k, this.getItem(k));
        }
        return hashMap;
    }

    @Override
    public void setContents(Map<Integer, Item> map) {
        if (map.size() > this.size) {
            HashMap<Integer, Item> hashMap = new HashMap<Integer, Item>();
            for (int k = 0; k < this.size; ++k) {
                hashMap.put(k, map.get(k));
            }
            map = hashMap;
        }
        for (int k = 0; k < this.size; ++k) {
            if (!map.containsKey(k)) {
                if (k < this.c.size) {
                    if (!this.c.slots.containsKey(k)) continue;
                    this.clear(k);
                    continue;
                }
                if (!this.b.slots.containsKey(k - this.c.size)) continue;
                this.clear(k);
                continue;
            }
            if (this.setItem(k, map.get(k))) continue;
            this.clear(k);
        }
    }

    @Override
    public void onOpen(Player player) {
        Level level;
        super.onOpen(player);
        this.c.viewers.add(player);
        this.b.viewers.add(player);
        if (this.getViewers().size() == 1 && (level = this.c.getHolder().getLevel()) != null) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)this.c.getHolder().getX();
            blockEventPacket.y = (int)this.c.getHolder().getY();
            blockEventPacket.z = (int)this.c.getHolder().getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 2;
            BlockEventPacket blockEventPacket2 = new BlockEventPacket();
            blockEventPacket2.x = (int)this.b.getHolder().getX();
            blockEventPacket2.y = (int)this.b.getHolder().getY();
            blockEventPacket2.z = (int)this.b.getHolder().getZ();
            blockEventPacket2.case1 = 1;
            blockEventPacket2.case2 = 2;
            level.addLevelSoundEvent(this.c.getHolder().add(0.5, 0.5, 0.5), 67);
            level.addChunkPacket((int)this.c.getHolder().getX() >> 4, (int)this.c.getHolder().getZ() >> 4, blockEventPacket);
            level.addChunkPacket((int)this.b.getHolder().getX() >> 4, (int)this.b.getHolder().getZ() >> 4, blockEventPacket2);
        }
    }

    @Override
    public void onClose(Player player) {
        Level level;
        if (this.getViewers().size() == 1 && (level = this.b.getHolder().getLevel()) != null) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)this.b.getHolder().getX();
            blockEventPacket.y = (int)this.b.getHolder().getY();
            blockEventPacket.z = (int)this.b.getHolder().getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 0;
            BlockEventPacket blockEventPacket2 = new BlockEventPacket();
            blockEventPacket2.x = (int)this.c.getHolder().getX();
            blockEventPacket2.y = (int)this.c.getHolder().getY();
            blockEventPacket2.z = (int)this.c.getHolder().getZ();
            blockEventPacket2.case1 = 1;
            blockEventPacket2.case2 = 0;
            level.addLevelSoundEvent(this.c.getHolder().add(0.5, 0.5, 0.5), 68);
            level.addChunkPacket((int)this.b.getHolder().getX() >> 4, (int)this.b.getHolder().getZ() >> 4, blockEventPacket);
            level.addChunkPacket((int)this.c.getHolder().getX() >> 4, (int)this.c.getHolder().getZ() >> 4, blockEventPacket2);
        }
        this.c.viewers.remove(player);
        this.b.viewers.remove(player);
        super.onClose(player);
    }

    public ChestInventory getLeftSide() {
        return this.c;
    }

    public ChestInventory getRightSide() {
        return this.b;
    }

    public void sendSlot(Inventory inventory, int n, Player ... playerArray) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.slot = inventory == this.b ? this.c.getSize() + n : n;
        inventorySlotPacket.item = inventory.getItem(n).clone();
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
}

