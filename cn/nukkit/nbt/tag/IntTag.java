/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class IntTag
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

    public IntTag(String string) {
        super(string);
    }

    public IntTag(String string, int n) {
        super(string);
        this.data = n;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeInt(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readInt();
    }

    @Override
    public Integer parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public String toString() {
        return "IntTag " + this.getName() + "(data: " + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new IntTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            IntTag intTag = (IntTag)object;
            return this.data == intTag.data;
        }
        return false;
    }
}

