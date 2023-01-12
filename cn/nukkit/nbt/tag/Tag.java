/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.tag;

import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.ByteArrayTag;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.EndTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.IntArrayTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.LongTag;
import cn.nukkit.nbt.tag.ShortTag;
import cn.nukkit.nbt.tag.StringTag;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Tag {
    public static final byte TAG_End = 0;
    public static final byte TAG_Byte = 1;
    public static final byte TAG_Short = 2;
    public static final byte TAG_Int = 3;
    public static final byte TAG_Long = 4;
    public static final byte TAG_Float = 5;
    public static final byte TAG_Double = 6;
    public static final byte TAG_Byte_Array = 7;
    public static final byte TAG_String = 8;
    public static final byte TAG_List = 9;
    public static final byte TAG_Compound = 10;
    public static final byte TAG_Int_Array = 11;
    private String a;

    abstract void write(NBTOutputStream var1) throws IOException;

    abstract void load(NBTInputStream var1) throws IOException;

    public abstract String toString();

    public abstract byte getId();

    protected Tag(String string) {
        this.a = string == null ? "" : string;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag)object;
        return !(this.getId() != tag.getId() || this.a == null && tag.a != null || this.a != null && tag.a == null || this.a != null && !this.a.equals(tag.a));
    }

    public void print(PrintStream printStream) {
        this.print("", printStream);
    }

    public void print(String string, PrintStream printStream) {
        String string2 = this.getName();
        printStream.print(string);
        printStream.print(Tag.getTagName(this.getId()));
        if (!string2.isEmpty()) {
            printStream.print("(\"" + string2 + "\")");
        }
        printStream.print(": ");
        printStream.println(this.toString());
    }

    public Tag setName(String string) {
        this.a = string == null ? "" : string;
        return this;
    }

    public String getName() {
        if (this.a == null) {
            return "";
        }
        return this.a;
    }

    public static Tag readNamedTag(NBTInputStream nBTInputStream) throws IOException {
        byte by = nBTInputStream.readByte();
        if (by == 0) {
            return new EndTag();
        }
        String string = nBTInputStream.readUTF();
        Tag tag = Tag.newTag(by, string);
        tag.load(nBTInputStream);
        return tag;
    }

    public static void writeNamedTag(Tag tag, NBTOutputStream nBTOutputStream) throws IOException {
        Tag.writeNamedTag(tag, tag.getName(), nBTOutputStream);
    }

    public static void writeNamedTag(Tag tag, String string, NBTOutputStream nBTOutputStream) throws IOException {
        nBTOutputStream.writeByte(tag.getId());
        if (tag.getId() == 0) {
            return;
        }
        nBTOutputStream.writeUTF(string);
        tag.write(nBTOutputStream);
    }

    public static Tag newTag(byte by, String string) {
        switch (by) {
            case 0: {
                return new EndTag();
            }
            case 1: {
                return new ByteTag(string);
            }
            case 2: {
                return new ShortTag(string);
            }
            case 3: {
                return new IntTag(string);
            }
            case 4: {
                return new LongTag(string);
            }
            case 5: {
                return new FloatTag(string);
            }
            case 6: {
                return new DoubleTag(string);
            }
            case 7: {
                return new ByteArrayTag(string);
            }
            case 11: {
                return new IntArrayTag(string);
            }
            case 8: {
                return new StringTag(string);
            }
            case 9: {
                return new ListTag(string);
            }
            case 10: {
                return new CompoundTag(string);
            }
        }
        return new EndTag();
    }

    public static String getTagName(byte by) {
        switch (by) {
            case 0: {
                return "TAG_End";
            }
            case 1: {
                return "TAG_Byte";
            }
            case 2: {
                return "TAG_Short";
            }
            case 3: {
                return "TAG_Int";
            }
            case 4: {
                return "TAG_Long";
            }
            case 5: {
                return "TAG_Float";
            }
            case 6: {
                return "TAG_Double";
            }
            case 7: {
                return "TAG_Byte_Array";
            }
            case 11: {
                return "TAG_Int_Array";
            }
            case 8: {
                return "TAG_String";
            }
            case 9: {
                return "TAG_List";
            }
            case 10: {
                return "TAG_Compound";
            }
        }
        return "UNKNOWN";
    }

    public abstract Tag copy();

    public abstract Object parseValue();

    private static IOException b(IOException iOException) {
        return iOException;
    }
}

