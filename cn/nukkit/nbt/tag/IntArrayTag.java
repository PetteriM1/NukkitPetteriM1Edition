/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag
extends Tag {
    public int[] data;

    public IntArrayTag(String string) {
        super(string);
    }

    public IntArrayTag(String string, int[] nArray) {
        super(string);
        this.data = nArray;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeInt(this.data.length);
        for (int n : this.data) {
            nBTOutputStream.writeInt(n);
        }
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        int n = nBTInputStream.readInt();
        this.data = new int[n];
        for (int k = 0; k < n; ++k) {
            this.data[k] = nBTInputStream.readInt();
        }
    }

    public int[] getData() {
        return this.data;
    }

    public int[] parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 11;
    }

    @Override
    public String toString() {
        return "IntArrayTag " + this.getName() + " [" + this.data.length + " bytes]";
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            IntArrayTag intArrayTag = (IntArrayTag)object;
            return this.data == null && intArrayTag.data == null || this.data != null && Arrays.equals(this.data, intArrayTag.data);
        }
        return false;
    }

    @Override
    public Tag copy() {
        int[] nArray = new int[this.data.length];
        System.arraycopy(this.data, 0, nArray, 0, this.data.length);
        return new IntArrayTag(this.getName(), nArray);
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

