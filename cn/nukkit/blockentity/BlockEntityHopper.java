/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.Fuel;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.HopperInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import java.util.HashSet;

public class BlockEntityHopper
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected HopperInventory inventory;
    public int transferCooldown;
    private AxisAlignedBB c;
    private boolean b;
    private int d;

    public BlockEntityHopper(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.transferCooldown = this.namedTag.contains("TransferCooldown") ? this.namedTag.getInt("TransferCooldown") : 8;
        this.inventory = new HopperInventory(this);
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag.getByte("Slot"), item);
        }
        this.c = new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 2.0, this.z + 1.0);
        this.scheduleUpdate();
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 154;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Hopper";
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

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public void setTransferCooldown(int n) {
        this.transferCooldown = n;
    }

    @Override
    public int getSize() {
        return 5;
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
        this.namedTag.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public HopperInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        --this.transferCooldown;
        if (this.a()) {
            ++this.d;
        }
        if (!this.isOnTransferCooldown()) {
            if (this.b()) {
                return true;
            }
            boolean bl = this.pushItems();
            if (!bl) {
                BlockEntity blockEntity = this.server.suomiCraftPEMode() ? this.level.getBlockEntityIfLoaded(this.chunk, this.up()) : this.level.getBlockEntity(this.chunk, this.up());
                Block block = null;
                bl = blockEntity instanceof BlockEntityContainer || (block = this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false)) instanceof BlockComposter ? this.a(blockEntity, block) : this.pickupItems();
            }
            if (bl) {
                this.setTransferCooldown(8);
                this.setDirty();
            }
        }
        return true;
    }

    public boolean pullItems() {
        return this.a(this.server.suomiCraftPEMode() ? this.level.getBlockEntityIfLoaded(this.chunk, this.up()) : this.level.getBlockEntity(this.chunk, this.up()), this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false));
    }

    private boolean a(BlockEntity blockEntity, Block block) {
        if (this.inventory.isFull()) {
            return false;
        }
        if (blockEntity instanceof BlockEntityFurnace) {
            FurnaceInventory furnaceInventory = ((BlockEntityFurnace)blockEntity).getInventory();
            Item item = furnaceInventory.getResult();
            if (!item.isNull()) {
                Item item2 = item.clone();
                item2.count = 1;
                if (!this.inventory.canAddItem(item2)) {
                    this.c();
                    return false;
                }
                InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(furnaceInventory, this.inventory, this, item2, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                if (inventoryMoveItemEvent.isCancelled()) {
                    return false;
                }
                Item[] itemArray = this.inventory.addItem(item2);
                if (itemArray.length == 0) {
                    --item.count;
                    furnaceInventory.setResult(item);
                    return true;
                }
            }
        } else if (blockEntity instanceof InventoryHolder) {
            Inventory inventory = ((InventoryHolder)((Object)blockEntity)).getInventory();
            for (int k = 0; k < inventory.getSize(); ++k) {
                Item[] itemArray;
                Item item = inventory.getItem(k);
                if (item.isNull()) continue;
                Item item3 = item.clone();
                item3.count = 1;
                if (!this.inventory.canAddItem(item3)) {
                    this.c();
                    continue;
                }
                InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(inventory, this.inventory, this, item3, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                if (inventoryMoveItemEvent.isCancelled() || (itemArray = this.inventory.addItem(item3)).length >= 1) continue;
                --item.count;
                inventory.setItem(k, item);
                return true;
            }
        } else if (block instanceof BlockComposter) {
            BlockComposter blockComposter = (BlockComposter)block;
            Item item = blockComposter.empty();
            if (item == null || item.isNull()) {
                return false;
            }
            Item item4 = item.clone();
            item4.setCount(1);
            if (!this.inventory.canAddItem(item4)) {
                this.c();
                return false;
            }
            InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
            if (inventoryMoveItemEvent.isCancelled()) {
                return false;
            }
            Item[] itemArray = this.inventory.addItem(item4);
            return itemArray.length < 1;
        }
        return false;
    }

    public boolean pickupItems() {
        if (this.inventory.isFull()) {
            return false;
        }
        boolean bl = false;
        for (Entity entity : this.level.getCollidingEntities(this.c)) {
            EntityItem entityItem;
            Item item;
            if (entity.isClosed() || !(entity instanceof EntityItem) || (item = (entityItem = (EntityItem)entity).getItem()).isNull()) continue;
            int n = item.getCount();
            if (!this.inventory.canAddItem(item)) {
                this.c();
                continue;
            }
            InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
            if (inventoryMoveItemEvent.isCancelled()) continue;
            Item[] itemArray = this.inventory.addItem(item);
            if (itemArray.length == 0) {
                entity.close();
                bl = true;
                continue;
            }
            if (itemArray[0].getCount() == n) continue;
            bl = true;
            item.setCount(itemArray[0].getCount());
        }
        return bl;
    }

    @Override
    public void close() {
        if (!this.closed) {
            for (Player player : new HashSet<Player>(this.inventory.getViewers())) {
                player.removeWindow(this.inventory);
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        for (Item item : this.inventory.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.inventory.clearAll();
    }

    public boolean pushItems() {
        BlockEntity blockEntity;
        if (this.inventory.slots.isEmpty()) {
            return false;
        }
        int n = this.level.getBlockDataAt((int)this.x, (int)this.y, (int)this.z);
        BlockEntity blockEntity2 = blockEntity = this.server.suomiCraftPEMode() ? this.level.getBlockEntityIfLoaded(this.chunk, this.getSide(BlockFace.fromIndex(n))) : this.level.getBlockEntity(this.chunk, this.getSide(BlockFace.fromIndex(n)));
        if (!(blockEntity instanceof InventoryHolder) || blockEntity instanceof BlockEntityHopper && n == 0) {
            return false;
        }
        if (blockEntity instanceof BlockEntityFurnace) {
            BlockEntityFurnace blockEntityFurnace = (BlockEntityFurnace)blockEntity;
            FurnaceInventory furnaceInventory = blockEntityFurnace.getInventory();
            if (furnaceInventory.isFull()) {
                return false;
            }
            boolean bl = false;
            for (int k = 0; k < this.inventory.getSize(); ++k) {
                InventoryMoveItemEvent inventoryMoveItemEvent;
                Item item;
                Item item2 = this.inventory.getItem(k);
                if (item2.isNull()) continue;
                Item item3 = item2.clone();
                item3.setCount(1);
                if (n == 0) {
                    item = furnaceInventory.getSmelting();
                    if (item.isNull()) {
                        inventoryMoveItemEvent = new InventoryMoveItemEvent(this.inventory, furnaceInventory, this, item3, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                        if (!inventoryMoveItemEvent.isCancelled()) {
                            furnaceInventory.setSmelting(item3);
                            --item2.count;
                            bl = true;
                        }
                    } else if (furnaceInventory.getSmelting().getId() == item3.getId() && furnaceInventory.getSmelting().getDamage() == item3.getDamage() && item.count < item.getMaxStackSize()) {
                        inventoryMoveItemEvent = new InventoryMoveItemEvent(this.inventory, furnaceInventory, this, item3, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                        if (!inventoryMoveItemEvent.isCancelled()) {
                            ++item.count;
                            furnaceInventory.setSmelting(item);
                            --item2.count;
                            bl = true;
                        }
                    }
                } else if (Fuel.duration.containsKey(item3.getId())) {
                    item = furnaceInventory.getFuel();
                    if (item.isNull()) {
                        inventoryMoveItemEvent = new InventoryMoveItemEvent(this.inventory, furnaceInventory, this, item3, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                        if (!inventoryMoveItemEvent.isCancelled()) {
                            furnaceInventory.setFuel(item3);
                            --item2.count;
                            bl = true;
                        }
                    } else if (item.getId() == item3.getId() && item.getDamage() == item3.getDamage() && item.count < item.getMaxStackSize()) {
                        inventoryMoveItemEvent = new InventoryMoveItemEvent(this.inventory, furnaceInventory, this, item3, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
                        if (!inventoryMoveItemEvent.isCancelled()) {
                            ++item.count;
                            furnaceInventory.setFuel(item);
                            --item2.count;
                            bl = true;
                        }
                    }
                }
                if (!bl) continue;
                this.inventory.setItem(k, item2);
                return true;
            }
            return false;
        }
        Inventory inventory = ((InventoryHolder)((Object)blockEntity)).getInventory();
        if (inventory.isFull()) {
            return false;
        }
        for (int k = 0; k < this.inventory.getSize(); ++k) {
            Item[] itemArray;
            Item item = this.inventory.getItem(k);
            if (item.isNull()) continue;
            Item item4 = item.clone();
            item4.setCount(1);
            if (!inventory.canAddItem(item4)) {
                this.c();
                continue;
            }
            InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(this.inventory, inventory, this, item4, InventoryMoveItemEvent.Action.SLOT_CHANGE);
            this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
            if (inventoryMoveItemEvent.isCancelled() || (itemArray = inventory.addItem(item4)).length > 0) continue;
            --item.count;
            this.inventory.setItem(k, item);
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Hopper").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }

    private void c() {
        if (this.a()) {
            this.transferCooldown = 2;
        }
    }

    private boolean b() {
        if (this.a()) {
            if (this.d > 1) {
                this.b = this.level.isBlockPowered(this.chunk, this);
                this.d = 0;
            }
            return this.b;
        }
        return this.level.isBlockPowered(this.chunk, this);
    }

    private boolean a() {
        return this.server.suomiCraftPEMode() || !this.server.unsafeRedstone;
    }
}

