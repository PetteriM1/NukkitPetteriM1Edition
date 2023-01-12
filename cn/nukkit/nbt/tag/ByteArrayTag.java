/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Binary;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag
extends Tag {
    public byte[] data;

    public ByteArrayTag(String string) {
        super(string);
    }

    public ByteArrayTag(String string, byte[] byArray) {
        super(string);
        this.data = byArray;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        if (this.data == null) {
            nBTOutputStream.writeInt(0);
            return;
        }
        nBTOutputStream.writeInt(this.data.length);
        nBTOutputStream.write(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        int n = nBTInputStream.readInt();
        this.data = new byte[n];
        nBTInputStream.readFully(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public String toString() {
        return "ByteArrayTag " + this.getName() + " (data: 0x" + Binary.bytesToHexString(this.data, true) + " [" + this.data.length + " bytes])";
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            ByteArrayTag byteArrayTag = (ByteArrayTag)object;
            return this.data == null && byteArrayTag.data == null || this.data != null && Arrays.equals(this.data, byteArrayTag.data);
        }
        return false;
    }

    @Override
    public Tag copy() {
        byte[] byArray = new byte[this.data.length];
        System.arraycopy(this.data, 0, byArray, 0, this.data.length);
        return new ByteArrayTag(this.getName(), byArray);
    }

    public byte[] parseValue() {
        return this.data;
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

