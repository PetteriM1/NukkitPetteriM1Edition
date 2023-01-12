/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class DoubleTag
extends NumberTag<Double> {
    public double data;

    @Override
    public Double getData() {
        return this.data;
    }

    @Override
    public void setData(Double d2) {
        this.data = d2 == null ? 0.0 : d2;
    }

    public DoubleTag(String string) {
        super(string);
    }

    public DoubleTag(String string, double d2) {
        super(string);
        this.data = d2;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeDouble(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readDouble();
    }

    @Override
    public Double parseValue() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public String toString() {
        return "DoubleTag " + this.getName() + " (data: " + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new DoubleTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            DoubleTag doubleTag = (DoubleTag)object;
            return this.data == doubleTag.data;
        }
        return false;
    }
}

