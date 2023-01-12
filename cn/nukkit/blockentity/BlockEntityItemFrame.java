/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityItemFrame
extends BlockEntitySpawnable {
    private Item b;

    public BlockEntityItemFrame(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("Item")) {
            this.b = new ItemBlock(Block.get(0));
            this.namedTag.putCompound("Item", NBTIO.putItemHelper(this.b));
        }
        if (!this.namedTag.contains("ItemRotation")) {
            this.namedTag.putByte("ItemRotation", 0);
        }
        if (!this.namedTag.contains("ItemDropChance")) {
            this.namedTag.putFloat("ItemDropChance", 1.0f);
        }
        this.level.updateComparatorOutputLevel(this);
        super.initBlockEntity();
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 199;
    }

    public int getItemRotation() {
        return this.namedTag.getByte("ItemRotation");
    }

    public void setItemRotation(int n) {
        this.namedTag.putByte("ItemRotation", n);
        this.level.updateComparatorOutputLevel(this);
        this.setDirty();
    }

    public Item getItem() {
        if (this.b == null) {
            CompoundTag compoundTag = this.namedTag.getCompound("Item");
            this.b = NBTIO.getItemHelper(compoundTag);
        }
        return this.b;
    }

    public void setItem(Item item) {
        this.setItem(item, true);
    }

    public void setItem(Item item, boolean bl) {
        this.b = null;
        this.namedTag.putCompound("Item", NBTIO.putItemHelper(item));
        if (bl) {
            this.setDirty();
        }
        this.level.updateComparatorOutputLevel(this);
    }

    public float getItemDropChance() {
        return this.namedTag.getFloat("ItemDropChance");
    }

    public void setItemDropChance(float f2) {
        this.namedTag.putFloat("ItemDropChance", f2);
    }

    @Override
    public void setDirty() {
        this.spawnToAll();
        super.setDirty();
    }

    @Override
    public CompoundTag getSpawnCompound() {
        if (!this.namedTag.contains("Item")) {
            this.setItem(new ItemBlock(Block.get(0)), false);
        }
        CompoundTag compoundTag = this.namedTag.getCompound("Item");
        CompoundTag compoundTag2 = new CompoundTag().putString("id", "ItemFrame").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        int n = compoundTag.getShort("id");
        if (n != 0) {
            CompoundTag compoundTag3;
            if (n == 358) {
                compoundTag3 = compoundTag.copy();
                compoundTag3.setName("Item");
            } else {
                compoundTag3 = new CompoundTag("Item").putShort("id", n).putByte("Count", compoundTag.getByte("Count")).putShort("Damage", compoundTag.getShort("Damage"));
                if (compoundTag.contains("tag")) {
                    CompoundTag compoundTag4 = compoundTag.getCompound("tag");
                    CompoundTag compoundTag5 = new CompoundTag();
                    if (compoundTag4.contains("ench")) {
                        compoundTag5.putList(new ListTag("ench"));
                    }
                    if (compoundTag4.contains("display") && compoundTag4.get("display") instanceof CompoundTag) {
                        compoundTag5.putCompound("display", new CompoundTag("display").putString("Name", ((CompoundTag)compoundTag4.get("display")).getString("Name")));
                    }
                    compoundTag3.put("tag", compoundTag5);
                }
            }
            compoundTag2.putCompound("Item", compoundTag3).putByte("ItemRotation", this.getItemRotation());
        }
        return compoundTag2;
    }

    public int getAnalogOutput() {
        return this.getItem() == null || this.getItem().getId() == 0 ? 0 : this.getItemRotation() % 8 + 1;
    }

    public boolean dropItem(Player player) {
        Item item = this.getItem();
        if (item != null && item.getId() != 0) {
            if (player != null) {
                ItemFrameDropItemEvent itemFrameDropItemEvent = new ItemFrameDropItemEvent(player, this.getBlock(), this, item);
                this.level.getServer().getPluginManager().callEvent(itemFrameDropItemEvent);
                if (itemFrameDropItemEvent.isCancelled()) {
                    this.spawnTo(player);
                    return true;
                }
            }
            this.setItem(Item.get(0));
            this.setItemRotation(0);
            if (this.getItemDropChance() > ThreadLocalRandom.current().nextFloat()) {
                this.level.dropItem(this.add(0.5, 0.0, 0.5), item);
            }
            this.level.addLevelEvent(this, 1043);
            return true;
        }
        return false;
    }
}

