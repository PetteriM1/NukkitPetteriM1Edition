/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.inventory.DropperInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;

public class BlockEntityDropper
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected final DropperInventory inventory = new DropperInventory(this);

    public BlockEntityDropper(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag2 : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag2);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag2.getByte("Slot"), item);
        }
        this.scheduleUpdate();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 125;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Dropper";
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
    public int getSize() {
        return 9;
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
                this.namedTag.getList("Items").getAll().remove(n2);
            }
        } else if (n2 < 0) {
            this.namedTag.getList("Items", CompoundTag.class).add(compoundTag);
        } else {
            this.namedTag.getList("Items", CompoundTag.class).add(n2, compoundTag);
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
    public DropperInventory getInventory() {
        return this.inventory;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Dropper").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }

    @Override
    public void onBreak() {
        for (Item item : this.inventory.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.inventory.clearAll();
    }
}

