package cn.nukkit.entity.data;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class NBTEntityData extends EntityData<CompoundTag> {

    public CompoundTag tag;
    public Item item;

    public NBTEntityData(int id, CompoundTag tag) {
        super(id);
        if (tag == null) throw new NullPointerException("data must not be null");
        this.tag = tag;
    }

    public NBTEntityData(int id, Item item) {
        super(id);
        this.item = item;
        this.tag = item.getNamedTag();
    }

    @Override
    public CompoundTag getData() {
        return this.tag;
    }

    @Override
    public void setData(CompoundTag tag) {
        if (tag == null) throw new NullPointerException("data must not be null");
        this.tag = tag;
    }

    @Override
    public int getType() {
        return Entity.DATA_TYPE_NBT;
    }

    @Override
    public String toString() {
        return tag.toString();
    }
}
