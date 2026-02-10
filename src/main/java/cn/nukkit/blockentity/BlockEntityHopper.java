package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by CreeperFace on 8.5.2017.
 */
public class BlockEntityHopper extends BlockEntitySpawnable implements InventoryHolder, BlockEntityContainer, BlockEntityNameable {

    protected HopperInventory inventory;

    public int transferCooldown;

    private AxisAlignedBB pickupArea;

    @Setter
    private InventoryHolder minecartPickupInventory;
    @Setter
    private InventoryHolder minecartPushInventory;

    public BlockEntityHopper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    // Performance: Don't update power state every tick
    private boolean cachedPowerState;
    private int poweredCacheAge = 17; // Always update on first tick

    private int getBlockDataAt() {
        if (this.y < this.level.getMinBlockY() || this.y > this.level.getMaxBlockY()) {
            return 0;
        }
        int cx = (int) this.x >> 4;
        int cz = (int) this.z >> 4;
        FullChunk chunk = this.chunk;
        if (chunk == null || cx != chunk.getX() || cz != chunk.getZ()) {
            chunk = this.level.getChunkIfLoaded(cx, cz);
        }
        if (chunk == null) return 0;
        return chunk.getBlockData((int) this.x & 0x0f, (int) this.y, (int) this.z & 0x0f);
    }

    @Override
    public HopperInventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Hopper";
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag c = new CompoundTag()
                .putString("id", BlockEntity.HOPPER)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        if (this.hasName()) {
            c.put("CustomName", this.namedTag.get("CustomName"));
        }

        return c;
    }

    @Override
    public boolean isBlockEntityValid() {
        return level.getBlockIdAt(chunk, (int) x, (int) y, (int) z) == Block.HOPPER_BLOCK;
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    private boolean isPowered() {
        if (this.server.suomiCraftPEMode()) {
            if (this.poweredCacheAge > 16) {
                this.cachedPowerState = this.level.isBlockPowered(this.chunk, this);
                this.poweredCacheAge = 0;
            }
            return this.cachedPowerState;
        } else {
            return this.level.isBlockPowered(this.chunk, this);
        }
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.namedTag.remove("CustomName");
            return;
        }

        this.namedTag.putString("CustomName", name);
    }

    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    @Override
    public void close() {
        if (!closed) {
            for (Player player : new ArrayList<>(this.inventory.getViewers())) {
                player.removeWindow(this.inventory);
            }
            super.close();
        }
    }

    @Override
    public Item getItem(int index) {
        int i = this.getSlotIndex(index);
        if (i < 0) {
            return new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        } else {
            CompoundTag data = (CompoundTag) this.namedTag.getList("Items").get(i);
            return NBTIO.getItemHelper(data);
        }
    }

    protected int getSlotIndex(int index) {
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getByte("Slot") == index) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains("TransferCooldown")) {
            this.transferCooldown = this.namedTag.getInt("TransferCooldown");
        } else {
            this.transferCooldown = 8;
        }

        this.inventory = new HopperInventory(this);

        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        }

        ListTag<CompoundTag> list = (ListTag<CompoundTag>) this.namedTag.getList("Items");
        for (CompoundTag compound : list.getAll()) {
            Item item = NBTIO.getItemHelper(compound);
            if (item.getId() != 0 && item.getCount() > 0) {
                this.inventory.slots.put(compound.getByte("Slot"), item);
            }
        }

        this.pickupArea = new SimpleAxisAlignedBB(this.x, this.y, this.z, this.x + 1, this.y + 2, this.z + 1);

        this.scheduleUpdate();

        super.initBlockEntity();
    }

    private boolean noPlayersInTickingRange() {
        for (Player player : this.level.getPlayersList()) {
            if (player.distanceSquared(this) < 6400) { // 80 blocks
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        inventory.clearAll();
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }

        if (this.server.suomiCraftPEMode() && noPlayersInTickingRange()) {
            return true;
        }

        this.transferCooldown--;

        if (this.server.suomiCraftPEMode()) {
            this.poweredCacheAge++;
        }

        if (!this.isOnTransferCooldown()) {
            if (this.isPowered()) {
                return true;
            }

            // Note: Initial inventory full/empty checks are moved here from pull/push methods

            boolean changed = !this.inventory.slots.isEmpty() && (pushItems() || pushItemsToMinecart());

            if (!changed && !this.inventory.isFull()) {
                BlockEntity blockEntity = server.suomiCraftPEMode() ?
                        this.level.getBlockEntityIfLoaded(this.chunk, this.up()) :
                        this.level.getBlockEntity(this.chunk, this.up());
                Block block = null;
                if (blockEntity instanceof BlockEntityContainer || (block = this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false)) instanceof BlockComposter) {
                    changed = pullItems(blockEntity, block);
                } else {
                    changed = pullItemsFromMinecart() || pickupItems();
                }
            }

            if (changed) {
                this.setTransferCooldown((server.suomiCraftPEMode() ? 16 : 8));
                setDirty();
            }
        }


        return true;
    }

    private void optimizeTick() {
        if (this.server.suomiCraftPEMode()) {
            this.transferCooldown = 8; // Performance: Only loop inventory contents every other tick when none of the items couldn't be added
        }
    }

    public boolean pickupItems() {
        if (this.chunk == null) {
            return false;
        }

        boolean pickedUpItem = false;

        for (Entity entity : this.chunk.getEntities().values()) {
            if (entity.isClosed() || !(entity instanceof EntityItem) || !((EntityItem) entity).isAllowNonPlayerPickup()) {
                continue;
            }

            if (!entity.boundingBox.intersectsWith(this.pickupArea)) {
                continue;
            }

            EntityItem itemEntity = (EntityItem) entity;
            Item item = itemEntity.getItem();

            if (item.isNull()) {
                continue;
            }

            int originalCount = item.getCount();

            if (!this.inventory.canAddItem(item)) {
                this.optimizeTick();
                continue;
            }

            InventoryMoveItemEvent ev = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
                continue;
            }

            Item[] items = this.inventory.addItem(item);

            if (items.length == 0) {
                entity.close();
                pickedUpItem = true;
                continue;
            }

            if (items[0].getCount() != originalCount) {
                pickedUpItem = true;
                item.setCount(items[0].getCount());
            }
        }

        return pickedUpItem;
    }

    public boolean pullItems() {
        return this.pullItems(
                this.server.suomiCraftPEMode() ? this.level.getBlockEntityIfLoaded(this.chunk, this.up()) : this.level.getBlockEntity(this.chunk, this.up()),
                this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false));
    }

    private boolean pullItems(BlockEntity blockEntity, Block block) {
        if (blockEntity instanceof BlockEntityFurnace) {
            FurnaceInventory inv = ((BlockEntityFurnace) blockEntity).getInventory();
            Item item = inv.getResult();

            if (!item.isNull()) {
                Item itemToAdd = item.clone();
                itemToAdd.count = 1;

                if (!this.inventory.canAddItem(itemToAdd)) {
                    this.optimizeTick();
                    return false;
                }

                InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent(ev);

                if (ev.isCancelled()) {
                    this.optimizeTick();
                    return false;
                }

                Item[] items = this.inventory.addItem(itemToAdd);

                if (items.length == 0) {
                    item.count--;
                    inv.setResult(item);
                    return true;
                }
            }
        } else if (blockEntity instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) blockEntity).getInventory();

            for (int i = 0; i < inv.getSize(); i++) {
                Item item = inv.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.count = 1;

                    if (!this.inventory.canAddItem(itemToAdd)) {
                        this.optimizeTick();
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    Item[] items = this.inventory.addItem(itemToAdd);

                    if (items.length >= 1) {
                        continue;
                    }

                    item.count--;

                    inv.setItem(i, item);
                    return true;
                }
            }
        } else if (block instanceof BlockComposter) {
            BlockComposter composter = (BlockComposter) block;
            if (!composter.isFull()) {
                this.optimizeTick();
                return false;
            }
            Item item = composter.empty();
            if (item == null || item.isNull()) {
                this.optimizeTick();
                return false;
            }
            Item itemToAdd = item.clone();
            itemToAdd.setCount(1);
            if (!this.inventory.canAddItem(itemToAdd)) {
                this.optimizeTick();
                return false;
            }
            InventoryMoveItemEvent ev = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                this.optimizeTick();
                return false;
            }
            Item[] items = inventory.addItem(itemToAdd);
            return items.length < 1;
        }
        return false;
    }

    private boolean pullItemsFromMinecart() {
        if (this.minecartPickupInventory != null) {
            Inventory inv = this.minecartPickupInventory.getInventory();

            for (int i = 0; i < inv.getSize(); i++) {
                Item item = inv.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.count = 1;
                    if (!this.inventory.canAddItem(itemToAdd)) {
                        this.optimizeTick();
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        continue;
                    }

                    Item[] items = this.inventory.addItem(itemToAdd);
                    if (items.length >= 1) {
                        continue;
                    }

                    item.count--;
                    inv.setItem(i, item);

                    this.minecartPickupInventory = null;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean pushItems() {
        int blockData = (server.suomiCraftPEMode() ? getBlockDataAt() :
                this.level.getBlockDataAt(this.chunk, (int) x, (int) y, (int) z, Block.LAYER_NORMAL)) & 0x7;
        BlockEntity be = server.suomiCraftPEMode() ?
                this.level.getBlockEntityIfLoaded(this.chunk, this.getSide(BlockFace.fromIndex(blockData))) :
                this.level.getBlockEntity(this.chunk, this.getSide(BlockFace.fromIndex(blockData))); // Cached chunk doesn't have to be correct

        if (!(be instanceof InventoryHolder) || (be instanceof BlockEntityHopper && blockData == 0)) {
            return false;
        }

        InventoryMoveItemEvent event;

        if (be instanceof BlockEntityFurnace) {
            FurnaceInventory inventory = ((BlockEntityFurnace) be).getInventory();
            if (inventory.isFull()) {
                return false;
            }

            boolean pushedItem = false;

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);
                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    if (blockData == 0) {
                        Item smelting = inventory.getSmelting();
                        if (smelting.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setSmelting(itemToAdd);
                                item.count--;
                                pushedItem = true;
                            }
                        } else if (smelting.getId() == itemToAdd.getId() && smelting.getDamage() == itemToAdd.getDamage() && smelting.count < smelting.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                smelting.count++;
                                inventory.setSmelting(smelting);
                                item.count--;
                                pushedItem = true;
                            }
                        }
                    } else if (Fuel.duration.containsKey(itemToAdd.getId())) {
                        Item fuel = inventory.getFuel();
                        if (fuel.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setFuel(itemToAdd);
                                item.count--;
                                pushedItem = true;
                            }
                        } else if (fuel.getId() == itemToAdd.getId() && fuel.getDamage() == itemToAdd.getDamage() && fuel.count < fuel.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                fuel.count++;
                                inventory.setFuel(fuel);
                                item.count--;
                                pushedItem = true;
                            }
                        }
                    }

                    if (pushedItem) {
                        this.inventory.setItem(i, item);
                        return true;
                    }
                }
            }

            return false;
        } else if (be instanceof BlockEntityBrewingStand) {
            BrewingInventory inventory = ((BlockEntityBrewingStand) be).getInventory();
            if (inventory.isFull()) {
                return false;
            }

            boolean pushedItem = false;

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);
                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    boolean isPotion = itemToAdd.getId() == Item.GLASS_BOTTLE || itemToAdd.getId() == Item.POTION || itemToAdd.getId() == Item.SPLASH_POTION;

                    if (blockData == 0) { // Hopper is above the brewing stand
                        if (isPotion) {
                            continue; // No potions as ingredient
                        }

                        Item ingredient = inventory.getIngredient();
                        if (ingredient.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setIngredient(itemToAdd);
                                pushedItem = true;
                            }
                        } else if (ingredient.getId() == itemToAdd.getId() && ingredient.getDamage() == itemToAdd.getDamage() && ingredient.count < ingredient.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                ingredient.count++;
                                inventory.setIngredient(ingredient);
                                pushedItem = true;
                            }
                        }
                    } else if (itemToAdd.getId() == Item.BLAZE_POWDER) { // Only blaze powder to fuel slot
                        Item fuel = inventory.getFuel();
                        if (fuel.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setFuel(itemToAdd);
                                pushedItem = true;
                            }
                        } else if (fuel.getId() == itemToAdd.getId() && fuel.getDamage() == itemToAdd.getDamage() && fuel.count < fuel.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                fuel.count++;
                                inventory.setFuel(fuel);
                                pushedItem = true;
                            }
                        }
                    } else if (isPotion) { // Only bottles/potions to bottle slots
                        if (inventory.addItem(itemToAdd).length > 0) { // BrewingInventory overrides addItemSize to check the correct slots
                            continue;
                        } else {
                            pushedItem = true;
                        }
                    }

                    if (pushedItem) {
                        item.count--;
                        this.inventory.setItem(i, item);
                        return true;
                    }
                }
            }

            return false;
        } else {
            Inventory inventory = ((InventoryHolder) be).getInventory();

            if (inventory.isFull()) {
                return false;
            }

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    if (!inventory.canAddItem(itemToAdd)) {
                        this.optimizeTick();
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    Item[] items = inventory.addItem(itemToAdd);

                    if (items.length > 0) {
                        continue;
                    }

                    item.count--;
                    this.inventory.setItem(i, item);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean pushItemsToMinecart() {
        if (this.minecartPushInventory != null) {
            Inventory holderInventory = this.minecartPushInventory.getInventory();

            if (holderInventory.isFull()) {
                return false;
            }

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    if (!holderInventory.canAddItem(itemToAdd)) {
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(this.inventory, holderInventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    Item[] items = holderInventory.addItem(itemToAdd);
                    if (items.length > 0) {
                        continue;
                    }

                    item.count--;
                    this.inventory.setItem(i, item);

                    this.minecartPushInventory = null;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.getSize(); index++) {
            this.setItem(index, this.inventory.getItem(index));
        }

        this.namedTag.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public void setItem(int index, Item item) {
        int i = this.getSlotIndex(index);

        CompoundTag d = NBTIO.putItemHelper(item, index);

        if (item.getId() == Item.AIR || item.getCount() <= 0) {
            if (i >= 0) {
                this.namedTag.getList("Items").getAll().remove(i);
            }
        } else if (i < 0) {
            (this.namedTag.getList("Items", CompoundTag.class)).add(d);
        } else {
            (this.namedTag.getList("Items", CompoundTag.class)).add(i, d);
        }
    }
}
