/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class LongTag
extends NumberTag<Long> {
    public long data;

    @Override
    public Long getData() {
        return this.data;
    }

    @Override
    public void setData(Long l) {
        this.data = l == null ? 0L : l;
    }

    public LongTag(String string) {
        super(string);
    }

    public LongTag(String string, long l) {
        super(string);
        this.data = l;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeLong(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readLong();
    }

    @Override
    public Long parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public String toString() {
        return "LongTag " + this.getName() + " (data:" + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new LongTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            LongTag longTag = (LongTag)object;
            return this.data == longTag.data;
        }
        return false;
    }
}

