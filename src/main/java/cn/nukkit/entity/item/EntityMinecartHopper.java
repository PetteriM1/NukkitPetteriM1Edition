package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.MinecartHopperInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.MinecartType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class EntityMinecartHopper extends EntityMinecartAbstract implements InventoryHolder {

    public static final int NETWORK_ID = 96;

    protected MinecartHopperInventory inventory;

    @Getter
    @Setter
    private boolean enabled = true;

    public int transferCooldown;

    public EntityMinecartHopper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setDisplayBlock(Block.get(Block.HOPPER_BLOCK), false);
        setName("Minecart with Hopper");
    }

    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    @Override
    public String getInteractButtonText() {
        return "action.interact.opencontainer";
    }

    @Override
    public MinecartHopperInventory getInventory() {
        return inventory;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(5);
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    @Override
    public boolean isRideable() {
        return false;
    }

    @Override
    protected void activate(int x, int y, int z, boolean flag) {
        this.enabled = !flag;
    }

    @Override
    public void dropItem() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) this.lastDamageCause).getDamager();
            if (damager instanceof Player && ((Player) damager).isCreative()) {
                return;
            }
        }
        this.level.dropItem(this, Item.get(Item.HOPPER_MINECART));
        if (this.inventory != null) {
            this.inventory.getViewers().clear();
            for (Item item : this.inventory.getContents().values()) {
                this.level.dropItem(this, item);
            }
            this.inventory.clearAll();
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (!this.closed && this.isAlive()) {
            if (this.server.suomiCraftPEMode() && noPlayersInTickingRange()) {
                return true;
            }

            if (this.isOnTransferCooldown()) {
                this.transferCooldown--;
                return true;
            }

            if (!this.enabled) {
                return true;
            }

            boolean changed;
            BlockEntity blockEntity = server.suomiCraftPEMode() ?
                    this.level.getBlockEntityIfLoaded(this.chunk, this.up()) :
                    this.level.getBlockEntity(this.chunk, this.up());
            Block block = null;
            if (blockEntity instanceof BlockEntityContainer || (block = this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false)) instanceof BlockComposter) {
                changed = pullItems(blockEntity, block);
            } else {
                // Apparently we are 0.5 blocks above the ground
                // Hopper minecart can pick up items through is a block
                changed = pickupItems(new SimpleAxisAlignedBB(this.x, this.y - 0.5, this.z, this.x + 1, this.y + 2, this.z + 1));
            }

            if (changed) {
                this.setTransferCooldown(8);
            }

            return true;
        }

        return hasUpdate;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        if (this.namedTag.contains("Enabled")) {
            this.enabled = this.namedTag.getBoolean("Enabled");
        }

        this.inventory = new MinecartHopperInventory(this);
        if (this.namedTag.contains("Items") && this.namedTag.get("Items") instanceof ListTag) {
            ListTag<CompoundTag> inventoryList = this.namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag item : inventoryList.getAll()) {
                this.inventory.setItem(item.getByte("Slot"), NBTIO.getItemHelper(item));
            }
        }

        this.dataProperties
                .putByte(DATA_CONTAINER_TYPE, 11)
                .putInt(DATA_CONTAINER_BASE_SIZE, this.inventory.getSize())
                .putInt(DATA_CONTAINER_EXTRA_SLOTS_PER_STRENGTH, 0);
    }

    @Override
    public boolean mountEntity(Entity entity, byte mode) {
        return false;
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
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (this.isAlive()) {
            player.addWindow(this.inventory);
        }
        return false; // If true, the count of items player has in hand decreases
    }

    private void optimizeTick() {
        if (server.suomiCraftPEMode()) {
            this.transferCooldown = 4; // Performance: Only loop inventory contents every other tick when none of the items couldn't be added
        }
    }

    private boolean pickupItems(AxisAlignedBB pickupArea) {
        if (this.chunk == null || this.inventory.isFull()) {
            return false;
        }

        boolean pickedUpItem = false;

        for (Entity entity : new ArrayList<>(this.chunk.getEntities().values())) {
            if (entity.isClosed() || !(entity instanceof EntityItem) || !((EntityItem) entity).isAllowNonPlayerPickup()) {
                continue;
            }

            if (!entity.boundingBox.intersectsWith(pickupArea)) {
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

    private boolean pullItems(BlockEntity blockEntity, Block block) {
        if (this.inventory.isFull()) {
            return false;
        }

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
            return items.length == 0;
        }
        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Enabled", this.enabled);

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        if (this.inventory != null) {
            for (int slot = 0; slot < 5; ++slot) {
                Item item = this.inventory.getItem(slot);
                if (item != null && item.getId() != Item.AIR) {
                    this.namedTag.getList("Items", CompoundTag.class)
                            .add(NBTIO.putItemHelper(item, slot));
                }
            }
        }
    }
}
