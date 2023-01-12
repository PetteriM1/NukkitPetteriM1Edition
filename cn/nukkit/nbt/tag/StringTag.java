/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class StringTag
extends Tag {
    public String data;

    public StringTag(String string) {
        super(string);
    }

    public StringTag(String string, String string2) {
        super(string);
        this.data = string2;
        if (string2 == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeUTF(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readUTF();
    }

    @Override
    public String parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 8;
    }

    @Override
    public String toString() {
        return "StringTag " + this.getName() + " (data: " + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new StringTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            StringTag stringTag = (StringTag)object;
            return this.data == null && stringTag.data == null || this.data != null && this.data.equals(stringTag.data);
        }
        return false;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

