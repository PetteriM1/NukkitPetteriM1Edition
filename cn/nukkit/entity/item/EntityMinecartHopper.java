/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.block.BlockRailActivator;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.MinecartHopperInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.MinecartType;

public class EntityMinecartHopper
extends EntityMinecartAbstract
implements InventoryHolder {
    public static final int NETWORK_ID = 96;
    protected MinecartHopperInventory inventory;
    public int transferCooldown;

    public EntityMinecartHopper(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        this.setDisplayBlock(Block.get(154), false);
        this.setName("Minecart with Hopper");
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(5);
    }

    @Override
    public boolean isRideable() {
        return false;
    }

    @Override
    public int getNetworkId() {
        return 96;
    }

    @Override
    public void dropItem() {
        Object object;
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (object = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player && ((Player)object).isCreative()) {
            return;
        }
        this.level.dropItem(this, Item.get(408));
        if (this.inventory != null) {
            for (Item item : this.inventory.getContents().values()) {
                this.level.dropItem(this, item);
            }
            this.inventory.clearAll();
        }
    }

    @Override
    public boolean mountEntity(Entity entity, byte by) {
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        player.addWindow(this.inventory);
        return false;
    }

    @Override
    public MinecartHopperInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.inventory = new MinecartHopperInventory(this);
        if (this.namedTag.contains("Items") && this.namedTag.get("Items") instanceof ListTag) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                this.inventory.setItem(compoundTag.getByte("Slot"), NBTIO.getItemHelper(compoundTag));
            }
        }
        this.dataProperties.putByte(44, 11).putInt(45, this.inventory.getSize()).putInt(46, 0);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag("Items"));
        if (this.inventory != null) {
            for (int k = 0; k < 5; ++k) {
                Item item = this.inventory.getItem(k);
                if (item == null || item.getId() == 0) continue;
                this.namedTag.getList("Items", CompoundTag.class).add(NBTIO.putItemHelper(item, k));
            }
        }
    }

    @Override
    public boolean onUpdate(int n) {
        if (super.onUpdate(n)) {
            if (this.isOnTransferCooldown()) {
                --this.transferCooldown;
                return true;
            }
            Block block = this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ(), false);
            if (block instanceof BlockRailActivator && ((BlockRailActivator)block).isActive()) {
                return false;
            }
            BlockEntity blockEntity = this.server.suomiCraftPEMode() ? this.level.getBlockEntityIfLoaded(this.chunk, this.up()) : this.level.getBlockEntity(this.chunk, this.up());
            Block block2 = null;
            boolean bl = blockEntity instanceof BlockEntityContainer || (block2 = this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY() + 1, this.getFloorZ(), false)) instanceof BlockComposter ? this.a(blockEntity, block2) : this.a(new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 2.0, this.z + 1.0));
            if (bl) {
                this.setTransferCooldown(8);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getInteractButtonText() {
        return "action.interact.opencontainer";
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public void setTransferCooldown(int n) {
        this.transferCooldown = n;
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
                    this.d();
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
                    this.d();
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
                this.d();
                return false;
            }
            InventoryMoveItemEvent inventoryMoveItemEvent = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(inventoryMoveItemEvent);
            if (inventoryMoveItemEvent.isCancelled()) {
                return false;
            }
            Item[] itemArray = this.inventory.addItem(item4);
            return itemArray.length == 0;
        }
        return false;
    }

    private boolean a(AxisAlignedBB axisAlignedBB) {
        if (this.inventory.isFull()) {
            return false;
        }
        boolean bl = false;
        for (Entity entity : this.level.getCollidingEntities(axisAlignedBB)) {
            EntityItem entityItem;
            Item item;
            if (entity.isClosed() || !(entity instanceof EntityItem) || (item = (entityItem = (EntityItem)entity).getItem()).isNull()) continue;
            int n = item.getCount();
            if (!this.inventory.canAddItem(item)) {
                this.d();
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

    private void d() {
        if (this.server.suomiCraftPEMode() || !this.server.unsafeRedstone) {
            this.transferCooldown = 2;
        }
    }
}

