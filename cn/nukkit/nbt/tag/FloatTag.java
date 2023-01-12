/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class FloatTag
extends NumberTag<Float> {
    public float data;

    @Override
    public Float getData() {
        return Float.valueOf(this.data);
    }

    @Override
    public void setData(Float f2) {
        this.data = f2 == null ? 0.0f : f2.floatValue();
    }

    public FloatTag(String string) {
        super(string);
    }

    public FloatTag(String string, float f2) {
        super(string);
        this.data = f2;
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeFloat(this.data);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.data = nBTInputStream.readFloat();
    }

    @Override
    public Float parseValue() {
        return Float.valueOf(this.data);
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public String toString() {
        return "FloatTag " + this.getName() + " (data: " + this.data + ')';
    }

    @Override
    public Tag copy() {
        return new FloatTag(this.getName(), this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            FloatTag floatTag = (FloatTag)object;
            return this.data == floatTag.data;
        }
        return false;
    }
}

