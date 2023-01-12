/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class ShortTag
extends NumberTag<Integer> {
    public int data;

    @Override
    public Integer getData() {
        return this.data;
    }

    @Override
    public void setData(Integer n) {
        this.data = n == null ? 0 : n;
    }

    public ShortTag(String string) {
        super(string);
    }

    public ShortTag(String string, int n) {
        super(string);
        this.data = n;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeShort(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readShort();
    }

    @Override
    public Integer parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    public String toString() {
        return "ShortTag " + this.getName() + "(data: " + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new ShortTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            ShortTag shortTag = (ShortTag)object;
            return this.data == shortTag.data;
        }
        return false;
    }
}

