/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.inventory.ChestBoatInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

public class EntityChestBoat
extends EntityBoat
implements InventoryHolder {
    public static final int NETWORK_ID = 218;
    private ChestBoatInventory n;

    public EntityChestBoat(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 218;
    }

    @Override
    public String getName() {
        return "Boat with Chest";
    }

    @Override
    public String getInteractButtonText(Player player) {
        if (player.isSneaking()) {
            return "action.interact.opencontainer";
        }
        return super.getInteractButtonText();
    }

    @Override
    public boolean isFull() {
        return this.passengers.size() >= 1;
    }

    @Override
    public Inventory getInventory() {
        return this.n;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (player.isSneaking()) {
            player.addWindow(this.n);
            return false;
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.n = new ChestBoatInventory(this);
        if (this.namedTag.contains("Items") && this.namedTag.get("Items") instanceof ListTag) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                this.n.setItem(compoundTag.getByte("Slot"), NBTIO.getItemHelper(compoundTag));
            }
        }
        this.dataProperties.putByte(44, InventoryType.CHEST_BOAT.getNetworkType()).putInt(45, this.n.getSize()).putInt(46, 0);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag("Items"));
        if (this.n != null) {
            for (int k = 0; k < 27; ++k) {
                Item item = this.n.getItem(k);
                if (item == null || item.getId() == 0) continue;
                this.namedTag.getList("Items", CompoundTag.class).add(NBTIO.putItemHelper(item, k));
            }
        }
    }

    @Override
    protected void dropItem() {
        switch (this.getVariant()) {
            case 0: {
                this.level.dropItem(this, Item.get(638));
                break;
            }
            case 1: {
                this.level.dropItem(this, Item.get(641));
                break;
            }
            case 2: {
                this.level.dropItem(this, Item.get(639));
                break;
            }
            case 3: {
                this.level.dropItem(this, Item.get(640));
                break;
            }
            case 4: {
                this.level.dropItem(this, Item.get(642));
                break;
            }
            case 5: {
                this.level.dropItem(this, Item.get(643));
                break;
            }
            case 6: {
                this.level.dropItem(this, Item.get(644));
            }
        }
        if (this.n != null) {
            for (Item item : this.n.getContents().values()) {
                this.level.dropItem(this, item);
            }
            this.n.clearAll();
        }
    }
}

