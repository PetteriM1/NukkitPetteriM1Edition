/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;

public class EndTag
extends Tag {
    public EndTag() {
        super(null);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
    }

    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "EndTag";
    }

    @Override
    public Tag copy() {
        return new EndTag();
    }

    @Override
    public Object parseValue() {
        return null;
    }
}

