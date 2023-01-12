/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.MinecartChestInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.MinecartType;

public class EntityMinecartChest
extends EntityMinecartAbstract
implements InventoryHolder {
    public static final int NETWORK_ID = 98;
    protected MinecartChestInventory inventory;

    public EntityMinecartChest(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        this.setDisplayBlock(Block.get(54), false);
        this.setName("Minecart with Chest");
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(1);
    }

    @Override
    public boolean isRideable() {
        return false;
    }

    @Override
    public int getNetworkId() {
        return 98;
    }

    @Override
    public void dropItem() {
        Object object;
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (object = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player && ((Player)object).isCreative()) {
            return;
        }
        this.level.dropItem(this, Item.get(342));
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
    public MinecartChestInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.inventory = new MinecartChestInventory(this);
        if (this.namedTag.contains("Items") && this.namedTag.get("Items") instanceof ListTag) {
            ListTag<CompoundTag> listTag = this.namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag compoundTag : listTag.getAll()) {
                this.inventory.setItem(compoundTag.getByte("Slot"), NBTIO.getItemHelper(compoundTag));
            }
        }
        this.dataProperties.putByte(44, 10).putInt(45, this.inventory.getSize()).putInt(46, 0);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag("Items"));
        if (this.inventory != null) {
            for (int k = 0; k < 27; ++k) {
                Item item = this.inventory.getItem(k);
                if (item == null || item.getId() == 0) continue;
                this.namedTag.getList("Items", CompoundTag.class).add(NBTIO.putItemHelper(item, k));
            }
        }
    }

    @Override
    public String getInteractButtonText() {
        return "action.interact.opencontainer";
    }
}

