package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class BlockEntityItemFrame extends BlockEntitySpawnable {

    private Item item_;

    public BlockEntityItemFrame(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public void setItem(Item item) {
        this.setItem(item, true);
    }

    public void setItemDropChance(float chance) {
        this.namedTag.putFloat("ItemDropChance", chance);
        super.setDirty(); // No need to spawnToAll
    }

    public void setItemRotation(int itemRotation) {
        this.namedTag.putByte("ItemRotation", itemRotation);
        this.level.updateComparatorOutputLevel(this);
        this.setDirty();
    }

    public int getAnalogOutput() {
        return this.getItem() == null || this.getItem().getId() == 0 ? 0 : this.getItemRotation() % 8 + 1;
    }

    public Item getItem() {
        if (item_ == null) {
            CompoundTag NBTTag = this.namedTag.getCompound("Item");
            item_ = NBTIO.getItemHelper(NBTTag);
        }
        return item_;
    }

    public float getItemDropChance() {
        return this.namedTag.getFloat("ItemDropChance");
    }

    public int getItemRotation() {
        return this.namedTag.getByte("ItemRotation");
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return this.getSpawnCompound(ProtocolInfo.CURRENT_PROTOCOL);
    }

    @Override
    public boolean isBlockEntityValid() {
        return level.getBlockIdAt(chunk, (int) x, (int) y, (int) z) == Block.ITEM_FRAME_BLOCK;
    }

    public boolean dropItem(Player player) {
        Item item = this.getItem();
        if (item != null && item.getId() != Item.AIR) {
            if (player != null) {
                ItemFrameDropItemEvent event = new ItemFrameDropItemEvent(player, this.getBlock(), this, item);
                this.level.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    this.spawnTo(player);
                    return true;
                }
            }
            this.setItem(Item.get(Item.AIR));
            this.setItemRotation(0);
            if (this.getItemDropChance() > ThreadLocalRandom.current().nextFloat()) {
                this.level.dropItem(this.add(0.5, 0, 0.5), item);
            }
            this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_REMOVED);
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag getSpawnCompound(int protocol) {
        if (!this.namedTag.contains("Item")) {
            this.setItem(new ItemBlock(Block.get(BlockID.AIR)), false);
        }
        CompoundTag itemOriginal = namedTag.getCompound("Item");

        CompoundTag tag = new CompoundTag()
                .putString("id", this instanceof BlockEntityItemFrameGlow ? BlockEntity.GLOW_ITEM_FRAME : BlockEntity.ITEM_FRAME)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        int itemId = itemOriginal.getShort("id");
        if (itemId != Item.AIR) {
            CompoundTag item;
            // Instead of copying the item's whole nbt just send the data necessary to display the item
            item = new CompoundTag("Item")
                    .putByte("Count", itemOriginal.getByte("Count"))
                    .putShort("Damage", itemOriginal.getShort("Damage"));

            if (protocol > ProtocolInfo.v1_16_0) {
                String identifier = RuntimeItems.getMapping(protocol).toRuntime(itemId, itemOriginal.getShort("Damage")).getIdentifier();
                item.putString("Name", identifier);
            } else {
                item.putShort("id", itemId);
            }

            if (itemOriginal.contains("tag")) {
                CompoundTag oldTag = itemOriginal.getCompound("tag");
                CompoundTag newTag = new CompoundTag();

                if (oldTag.contains("ench")) {
                    newTag.putList(new ListTag<>("ench"));
                }

                if (oldTag.contains("Base")) {
                    newTag.put("Base", oldTag.get("Base"));
                }

                if (oldTag.contains("Trim")) {
                    newTag.put("Trim", oldTag.get("Trim"));
                }

                if (oldTag.contains("Patterns")) {
                    newTag.put("Patterns", oldTag.get("Patterns"));
                }

                if (oldTag.contains("customColor")) {
                    newTag.put("customColor", oldTag.get("customColor"));
                }

                if (oldTag.contains("display") && oldTag.get("display") instanceof CompoundTag) {
                    newTag.putCompound("display", new CompoundTag("display").putString("Name", ((CompoundTag) oldTag.get("display")).getString("Name")));
                }

                if (itemId == Item.MAP && oldTag.contains("map_uuid")) {
                    newTag.put("map_uuid", oldTag.get("map_uuid"));
                }

                item.put("tag", newTag);
            }

            tag.putCompound("Item", item)
                    .putByte("ItemRotation", this.getItemRotation());
        }
        return tag;
    }

    @Override
    protected void initBlockEntity() {
        if (!namedTag.contains("Item")) {
            namedTag.putCompound("Item", NBTIO.putItemHelper(item_ = new ItemBlock(Block.get(BlockID.AIR))));
        }

        if (!namedTag.contains("ItemRotation")) {
            namedTag.putByte("ItemRotation", 0);
        }

        if (!namedTag.contains("ItemDropChance")) {
            namedTag.putFloat("ItemDropChance", 1.0f);
        }

        this.level.updateComparatorOutputLevel(this);

        super.initBlockEntity();
    }

    @Override
    public void onBreak() {
        Item item = null;

        if (level.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
            item = getItem();
        }

        this.namedTag.remove("Item");
        item_ = null;

        if (item != null && item.getId() != BlockID.AIR) {
            level.dropItem(this, item);
        }
    }

    @Override
    public void setDirty() {
        super.setDirty();
        this.spawnToAll();
    }

    public void setItem(Item item, boolean setChanged) {
        item_ = item;
        this.namedTag.putCompound("Item", NBTIO.putItemHelper(item));
        if (setChanged) {
            this.setDirty();
        }

        this.level.updateComparatorOutputLevel(this);
    }

    @Override
    public void spawnTo(Player player) {
        if (!this.closed) {
            player.dataPacket(this.createSpawnPacket(player.protocol));
        }
    }
}
