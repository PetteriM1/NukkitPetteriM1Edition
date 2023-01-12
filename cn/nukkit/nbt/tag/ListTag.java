/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.Tag;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class ListTag<T extends Tag>
extends Tag {
    private List<T> b = new ArrayList<T>();
    public byte type;

    public ListTag() {
        super("");
    }

    public ListTag(String string) {
        super(string);
    }

    @Override
    public void write(NBTOutputStream nBTOutputStream) throws IOException {
        this.type = !this.b.isEmpty() ? ((Tag)this.b.get(0)).getId() : (byte)1;
        nBTOutputStream.writeByte(this.type);
        nBTOutputStream.writeInt(this.b.size());
        for (Tag tag : this.b) {
            tag.write(nBTOutputStream);
        }
    }

    @Override
    public void load(NBTInputStream nBTInputStream) throws IOException {
        this.type = nBTInputStream.readByte();
        int n = nBTInputStream.readInt();
        this.b = new ArrayList<T>(n);
        for (int k = 0; k < n; ++k) {
            Tag tag = Tag.newTag(this.type, null);
            tag.load(nBTInputStream);
            tag.setName("");
            this.b.add(tag);
        }
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",\n\t");
        this.b.forEach(tag -> stringJoiner.add(tag.toString().replace("\n", "\n\t")));
        return "ListTag '" + this.getName() + "' (" + this.b.size() + " entries of type " + Tag.getTagName(this.type) + ") {\n\t" + stringJoiner.toString() + "\n}";
    }

    @Override
    public void print(String string, PrintStream printStream) {
        super.print(string, printStream);
        printStream.println(string + '{');
        String string2 = string;
        string = string + "   ";
        for (Tag tag : this.b) {
            tag.print(string, printStream);
        }
        printStream.println(string2 + '}');
    }

    public ListTag<T> add(T t) {
        this.type = ((Tag)t).getId();
        ((Tag)t).setName("");
        this.b.add(t);
        return this;
    }

    public ListTag<T> add(int n, T t) {
        this.type = ((Tag)t).getId();
        ((Tag)t).setName("");
        if (n >= this.b.size()) {
            this.b.add(n, t);
        } else {
            this.b.set(n, t);
        }
        return this;
    }

    @Override
    public List<Object> parseValue() {
        ArrayList<Object> arrayList = new ArrayList<Object>(this.b.size());
        for (Tag tag : this.b) {
            arrayList.add(tag.parseValue());
        }
        return arrayList;
    }

    public T get(int n) {
        return (T)((Tag)this.b.get(n));
    }

    public List<T> getAll() {
        return new ArrayList<T>(this.b);
    }

    public void setAll(List<T> list) {
        this.b = new ArrayList<T>(list);
    }

    public void remove(T t) {
        this.b.remove(t);
    }

    public void remove(int n) {
        this.b.remove(n);
    }

    public void removeAll(Collection<T> collection) {
        this.b.remove(collection);
    }

    public int size() {
        return this.b.size();
    }

    @Override
    public Tag copy() {
        ListTag<T> listTag = new ListTag<T>(this.getName());
        listTag.type = this.type;
        for (Tag tag : this.b) {
            Tag tag2 = tag.copy();
            listTag.b.add(tag2);
        }
        return listTag;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            ListTag listTag = (ListTag)object;
            if (this.type == listTag.type) {
                return this.b.equals(listTag.b);
            }
        }
        return false;
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

