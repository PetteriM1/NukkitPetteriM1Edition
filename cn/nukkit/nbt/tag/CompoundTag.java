/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.NOBF;
import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.ByteArrayTag;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.IntArrayTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.LongTag;
import cn.nukkit.nbt.tag.NumberTag;
import cn.nukkit.nbt.tag.ShortTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CompoundTag
extends Tag
implements Cloneable {
    @NOBF
    private final Map<String, Tag> tags = new HashMap<String, Tag>();

    public CompoundTag() {
        super("");
    }

    public CompoundTag(String string) {
        super(string);
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
            Tag.writeNamedTag(entry.getValue(), entry.getKey(), nBTOutputStream);
        }
        nBTOutputStream.writeByte(0);
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        Tag tag;
        this.tags.clear();
        while ((tag = Tag.readNamedTag(nBTInputStream)).getId() != 0) {
            this.tags.put(tag.getName(), tag);
        }
    }

    public Collection<Tag> getAllTags() {
        return this.tags.values();
    }

    @Override
    public byte getId() {
        return 10;
    }

    public CompoundTag put(String string, Tag tag) {
        this.tags.put(string, tag.setName(string));
        return this;
    }

    public CompoundTag putByte(String string, int n) {
        this.tags.put(string, new ByteTag(string, n));
        return this;
    }

    public CompoundTag putShort(String string, int n) {
        this.tags.put(string, new ShortTag(string, n));
        return this;
    }

    public CompoundTag putInt(String string, int n) {
        this.tags.put(string, new IntTag(string, n));
        return this;
    }

    public CompoundTag putLong(String string, long l) {
        this.tags.put(string, new LongTag(string, l));
        return this;
    }

    public CompoundTag putFloat(String string, float f2) {
        this.tags.put(string, new FloatTag(string, f2));
        return this;
    }

    public CompoundTag putDouble(String string, double d2) {
        this.tags.put(string, new DoubleTag(string, d2));
        return this;
    }

    public CompoundTag putString(String string, String string2) {
        this.tags.put(string, new StringTag(string, string2));
        return this;
    }

    public CompoundTag putByteArray(String string, byte[] byArray) {
        this.tags.put(string, new ByteArrayTag(string, byArray));
        return this;
    }

    public CompoundTag putIntArray(String string, int[] nArray) {
        this.tags.put(string, new IntArrayTag(string, nArray));
        return this;
    }

    public CompoundTag putList(ListTag<? extends Tag> listTag) {
        this.tags.put(listTag.getName(), listTag);
        return this;
    }

    public CompoundTag putCompound(String string, CompoundTag compoundTag) {
        this.tags.put(string, compoundTag.setName(string));
        return this;
    }

    public CompoundTag putBoolean(String string, boolean bl) {
        this.putByte(string, bl ? 1 : 0);
        return this;
    }

    public Tag get(String string) {
        return this.tags.get(string);
    }

    public boolean contains(String string) {
        return this.tags.containsKey(string);
    }

    public CompoundTag remove(String string) {
        this.tags.remove(string);
        return this;
    }

    public <T extends Tag> T removeAndGet(String string) {
        return (T)this.tags.remove(string);
    }

    public int getByte(String string) {
        if (!this.tags.containsKey(string)) {
            return 0;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).intValue();
    }

    public int getShort(String string) {
        if (!this.tags.containsKey(string)) {
            return 0;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).intValue();
    }

    public int getInt(String string) {
        if (!this.tags.containsKey(string)) {
            return 0;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).intValue();
    }

    public long getLong(String string) {
        if (!this.tags.containsKey(string)) {
            return 0L;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).longValue();
    }

    public float getFloat(String string) {
        if (!this.tags.containsKey(string)) {
            return 0.0f;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).floatValue();
    }

    public double getDouble(String string) {
        if (!this.tags.containsKey(string)) {
            return 0.0;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).doubleValue();
    }

    public String getString(String string) {
        if (!this.tags.containsKey(string)) {
            return "";
        }
        Tag tag = this.tags.get(string);
        if (tag instanceof NumberTag) {
            return ((StringBuilder)((NumberTag)tag).getData()).toString();
        }
        return ((StringTag)tag).data;
    }

    public byte[] getByteArray(String string) {
        if (!this.tags.containsKey(string)) {
            return new byte[0];
        }
        return ((ByteArrayTag)this.tags.get((Object)string)).data;
    }

    public byte[] getByteArray(String string, int n) {
        if (!this.tags.containsKey(string)) {
            return new byte[n];
        }
        return ((ByteArrayTag)this.tags.get((Object)string)).data;
    }

    public int[] getIntArray(String string) {
        if (!this.tags.containsKey(string)) {
            return new int[0];
        }
        return ((IntArrayTag)this.tags.get((Object)string)).data;
    }

    public CompoundTag getCompound(String string) {
        if (!this.tags.containsKey(string)) {
            return new CompoundTag(string);
        }
        return (CompoundTag)this.tags.get(string);
    }

    public ListTag<? extends Tag> getList(String string) {
        if (!this.tags.containsKey(string)) {
            return new ListTag(string);
        }
        return (ListTag)this.tags.get(string);
    }

    public <T extends Tag> ListTag<T> getList(String string, Class<T> clazz) {
        if (this.tags.containsKey(string)) {
            return (ListTag)this.tags.get(string);
        }
        return new ListTag(string);
    }

    public Map<String, Tag> getTags() {
        return new HashMap<String, Tag>(this.tags);
    }

    @Override
    public Map<String, Object> parseValue() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(this.tags.size());
        for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue().parseValue());
        }
        return hashMap;
    }

    public boolean getBoolean(String string) {
        return this.getByte(string) != 0;
    }

    public boolean getBoolean(String string, boolean bl) {
        if (!this.tags.containsKey(string)) {
            return bl;
        }
        return ((Number)((NumberTag)this.tags.get(string)).getData()).intValue() != 0;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",\n\t");
        this.tags.forEach((string, tag) -> stringJoiner.add('\'' + string + "' : " + tag.toString().replace("\n", "\n\t")));
        return "CompoundTag '" + this.getName() + "' (" + this.tags.size() + " entries) {\n\t" + stringJoiner.toString() + "\n}";
    }

    @Override
    public void print(String string, PrintStream printStream) {
        super.print(string, printStream);
        printStream.println(string + '{');
        String string2 = string;
        string = string + "   ";
        for (Tag tag : this.tags.values()) {
            tag.print(string, printStream);
        }
        printStream.println(string2 + '}');
    }

    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    @Override
    public CompoundTag copy() {
        CompoundTag compoundTag = new CompoundTag(this.getName());
        for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
            compoundTag.put(entry.getKey(), entry.getValue().copy());
        }
        return compoundTag;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            CompoundTag compoundTag = (CompoundTag)object;
            return this.tags.entrySet().equals(compoundTag.tags.entrySet());
        }
        return false;
    }

    public boolean exist(String string) {
        return this.tags.containsKey(string);
    }

    public CompoundTag clone() {
        CompoundTag compoundTag = new CompoundTag();
        this.getTags().forEach((string, tag) -> compoundTag.put((String)string, tag.copy()));
        return compoundTag;
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

