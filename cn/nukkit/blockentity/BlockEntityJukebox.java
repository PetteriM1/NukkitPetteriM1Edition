/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemRecord;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.Objects;

public class BlockEntityJukebox
extends BlockEntitySpawnable {
    private Item b;

    public BlockEntityJukebox(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.b = this.namedTag.contains("RecordItem") ? NBTIO.getItemHelper(this.namedTag.getCompound("RecordItem")) : Item.get(0);
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 84;
    }

    public void setRecordItem(Item item) {
        Objects.requireNonNull(item, "Record item cannot be null");
        this.b = item;
    }

    public Item getRecordItem() {
        return this.b;
    }

    public void play() {
        if (this.b instanceof ItemRecord) {
            switch (this.b.getId()) {
                case 500: {
                    this.getLevel().addLevelSoundEvent(this, 101);
                    break;
                }
                case 501: {
                    this.getLevel().addLevelSoundEvent(this, 102);
                    break;
                }
                case 502: {
                    this.getLevel().addLevelSoundEvent(this, 103);
                    break;
                }
                case 503: {
                    this.getLevel().addLevelSoundEvent(this, 104);
                    break;
                }
                case 504: {
                    this.getLevel().addLevelSoundEvent(this, 105);
                    break;
                }
                case 505: {
                    this.getLevel().addLevelSoundEvent(this, 106);
                    break;
                }
                case 506: {
                    this.getLevel().addLevelSoundEvent(this, 107);
                    break;
                }
                case 507: {
                    this.getLevel().addLevelSoundEvent(this, 108);
                    break;
                }
                case 508: {
                    this.getLevel().addLevelSoundEvent(this, 109);
                    break;
                }
                case 509: {
                    this.getLevel().addLevelSoundEvent(this, 110);
                    break;
                }
                case 510: {
                    this.getLevel().addLevelSoundEvent(this, 111);
                    break;
                }
                case 511: {
                    this.getLevel().addLevelSoundEvent(this, 112);
                    break;
                }
                case 759: {
                    this.getLevel().addLevelSoundEvent(this, 314);
                    break;
                }
                case 773: {
                    this.getLevel().addLevelSoundEvent(this, 371);
                    break;
                }
                case 636: {
                    this.getLevel().addLevelSoundEvent(this, 439);
                }
            }
        }
    }

    public void stop() {
        this.getLevel().addLevelSoundEvent(this, 113);
    }

    public void dropItem() {
        if (this.b.getId() != 0) {
            this.stop();
            this.level.dropItem(this.up(), this.b);
            this.b = Item.get(0);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putCompound("RecordItem", NBTIO.putItemHelper(this.b));
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return BlockEntityJukebox.getDefaultCompound(this, "Jukebox").putCompound("RecordItem", NBTIO.putItemHelper(this.b));
    }

    @Override
    public void onBreak() {
        this.dropItem();
    }
}

