/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

public class NBTEntityData
extends EntityData<CompoundTag> {
    public CompoundTag tag;
    public Item item;

    public NBTEntityData(int n, CompoundTag compoundTag) {
        super(n);
        this.tag = compoundTag;
    }

    public NBTEntityData(int n, Item item) {
        super(n);
        this.item = item;
        this.tag = item.getNamedTag();
    }

    @Override
    public CompoundTag getData() {
        return this.tag;
    }

    @Override
    public void setData(CompoundTag compoundTag) {
        this.tag = compoundTag;
    }

    @Override
    public int getType() {
        return 5;
    }
}

