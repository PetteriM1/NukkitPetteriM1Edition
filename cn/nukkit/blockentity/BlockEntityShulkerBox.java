/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.ShulkerBoxInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import java.util.HashSet;

public class BlockEntityShulkerBox
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected ShulkerBoxInventory inventory;

    public BlockEntityShulkerBox(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new ShulkerBoxInventory(this);
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag.getByte("Slot"), item);
        }
        if (!this.namedTag.contains("facing")) {
            this.namedTag.putByte("facing", 0);
        }
        super.initBlockEntity();
    }

    @Override
    public void close() {
        if (!this.closed) {
            for (Player player : new HashSet<Player>(this.getInventory().getViewers())) {
                player.removeWindow(this.getInventory());
            }
            super.close();
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag("Items"));
        for (int k = 0; k < this.getSize(); ++k) {
            this.setItem(k, this.inventory.getItem(k));
        }
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 218 || n == 205;
    }

    @Override
    public int getSize() {
        return 27;
    }

    protected int getSlotIndex(int n) {
        ListTag<CompoundTag> listTag = this.namedTag.getList("Items", CompoundTag.class);
        for (int k = 0; k < listTag.size(); ++k) {
            if (listTag.get(k).getByte("Slot") != n) continue;
            return k;
        }
        return -1;
    }

    @Override
    public Item getItem(int n) {
        int n2 = this.getSlotIndex(n);
        if (n2 < 0) {
            return new ItemBlock(Block.get(0), 0, 0);
        }
        CompoundTag compoundTag = (CompoundTag)this.namedTag.getList("Items").get(n2);
        return NBTIO.getItemHelper(compoundTag);
    }

    @Override
    public void setItem(int n, Item item) {
        int n2 = this.getSlotIndex(n);
        CompoundTag compoundTag = NBTIO.putItemHelper(item, n);
        if (item.getId() == 0 || item.getCount() <= 0) {
            if (n2 >= 0) {
                this.namedTag.getList("Items").remove(n2);
            }
        } else if (n2 < 0) {
            this.namedTag.getList("Items", CompoundTag.class).add(compoundTag);
        } else {
            this.namedTag.getList("Items", CompoundTag.class).add(n2, compoundTag);
        }
    }

    @Override
    public BaseInventory getInventory() {
        return this.inventory;
    }

    public ShulkerBoxInventory getRealInventory() {
        return this.inventory;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Shulker Box";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    public void setName(String string) {
        if (string == null || string.isEmpty()) {
            this.namedTag.remove("CustomName");
            return;
        }
        this.namedTag.putString("CustomName", string);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = BlockEntityShulkerBox.getDefaultCompound(this, "ShulkerBox").putByte("facing", this.namedTag.getByte("facing"));
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }
}

