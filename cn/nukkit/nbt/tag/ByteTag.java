/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class ByteTag
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

    public ByteTag(String string) {
        super(string);
    }

    public ByteTag(String string, int n) {
        super(string);
        this.data = n;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeByte(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readByte();
    }

    @Override
    public byte getId() {
        return 1;
    }

    @Override
    public Integer parseValue() {
        return this.data;
    }

    @Override
    public String toString() {
        String string = Integer.toHexString(this.data);
        if (string.length() < 2) {
            string = '0' + string;
        }
        return "ByteTag " + this.getName() + " (data: 0x" + string + ')';
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            ByteTag byteTag = (ByteTag)object;
            return this.data == byteTag.data;
        }
        return false;
    }

    @Override
    public Tag copy() {
        return new ByteTag(this.getName(), this.data);
    }
}

