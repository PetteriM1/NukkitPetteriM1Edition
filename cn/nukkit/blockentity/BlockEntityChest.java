/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.ChestInventory;
import cn.nukkit.inventory.DoubleChestInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import java.util.HashSet;

public class BlockEntityChest
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected ChestInventory inventory;
    protected DoubleChestInventory doubleInventory = null;

    public BlockEntityChest(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new ChestInventory(this);
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag.getByte("Slot"), item);
        }
        super.initBlockEntity();
    }

    @Override
    public void close() {
        if (!this.closed) {
            if (this.doubleInventory != null) {
                for (Player player : new HashSet<Player>(this.doubleInventory.getViewers())) {
                    player.removeWindow(this.doubleInventory);
                }
                this.doubleInventory = null;
            }
            for (Player player : new HashSet<Player>(this.inventory.getViewers())) {
                player.removeWindow(this.inventory);
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        this.unpair();
        for (Item item : this.inventory.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.inventory.clearAll();
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
        return n == 54 || n == 146;
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
        if (this.doubleInventory == null && this.isPaired()) {
            this.checkPairing();
        }
        return this.doubleInventory != null ? this.doubleInventory : this.inventory;
    }

    public ChestInventory getRealInventory() {
        return this.inventory;
    }

    protected void checkPairing() {
        BlockEntityChest blockEntityChest = this.getPair();
        if (blockEntityChest != null) {
            if (!blockEntityChest.isPaired()) {
                blockEntityChest.createPair(this);
                blockEntityChest.checkPairing();
            }
            if (blockEntityChest.doubleInventory != null) {
                this.doubleInventory = blockEntityChest.doubleInventory;
            } else if (this.doubleInventory == null) {
                this.doubleInventory = blockEntityChest.x + (double)((int)blockEntityChest.z << 15) > this.x + (double)((int)this.z << 15) ? new DoubleChestInventory(blockEntityChest, this) : new DoubleChestInventory(this, blockEntityChest);
            }
        } else if (this.level.isChunkLoaded(this.namedTag.getInt("pairx") >> 4, this.namedTag.getInt("pairz") >> 4)) {
            this.doubleInventory = null;
            this.namedTag.remove("pairx");
            this.namedTag.remove("pairz");
        }
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Chest";
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

    public boolean isPaired() {
        return this.namedTag.contains("pairx") && this.namedTag.contains("pairz");
    }

    public BlockEntityChest getPair() {
        BlockEntity blockEntity;
        if (this.isPaired() && (blockEntity = this.getLevel().getBlockEntityIfLoaded(this.chunk, new Vector3(this.namedTag.getInt("pairx"), this.y, this.namedTag.getInt("pairz")))) instanceof BlockEntityChest) {
            return (BlockEntityChest)blockEntity;
        }
        return null;
    }

    public boolean pairWith(BlockEntityChest blockEntityChest) {
        if (this.isPaired() || blockEntityChest.isPaired() || this.getBlock().getId() != blockEntityChest.getBlock().getId()) {
            return false;
        }
        this.createPair(blockEntityChest);
        blockEntityChest.spawnToAll();
        this.spawnToAll();
        this.checkPairing();
        return true;
    }

    public void createPair(BlockEntityChest blockEntityChest) {
        this.namedTag.putInt("pairx", (int)blockEntityChest.x);
        this.namedTag.putInt("pairz", (int)blockEntityChest.z);
        blockEntityChest.namedTag.putInt("pairx", (int)this.x);
        blockEntityChest.namedTag.putInt("pairz", (int)this.z);
    }

    public boolean unpair() {
        if (!this.isPaired()) {
            return false;
        }
        BlockEntityChest blockEntityChest = this.getPair();
        this.doubleInventory = null;
        this.namedTag.remove("pairx");
        this.namedTag.remove("pairz");
        this.spawnToAll();
        if (blockEntityChest != null) {
            blockEntityChest.namedTag.remove("pairx");
            blockEntityChest.namedTag.remove("pairz");
            blockEntityChest.doubleInventory = null;
            blockEntityChest.checkPairing();
            blockEntityChest.spawnToAll();
        }
        this.checkPairing();
        return true;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = this.isPaired() ? new CompoundTag().putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putInt("pairx", this.namedTag.getInt("pairx")).putInt("pairz", this.namedTag.getInt("pairz")) : new CompoundTag().putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }
}

