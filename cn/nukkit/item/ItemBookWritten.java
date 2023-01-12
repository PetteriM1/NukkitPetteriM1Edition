/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritable;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

public class ItemBookWritten
extends ItemBookWritable {
    public static final int GENERATION_ORIGINAL = 0;
    public static final int GENERATION_COPY = 1;
    public static final int GENERATION_COPY_OF_COPY = 2;
    public static final int GENERATION_TATTERED = 3;

    public ItemBookWritten() {
        this((Integer)0, 1);
    }

    public ItemBookWritten(Integer n) {
        this(n, 1);
    }

    public ItemBookWritten(Integer n, int n2) {
        super(387, 0, n2, "Written Book");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    public Item writeBook(String string, String string2, String[] stringArray) {
        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("pages");
        for (String string3 : stringArray) {
            listTag.add(ItemBookWritten.createPageTag(string3));
        }
        return this.writeBook(string, string2, listTag);
    }

    public Item writeBook(String string, String string2, ListTag<CompoundTag> listTag) {
        if (listTag.size() > 50 || listTag.size() <= 0) {
            return this;
        }
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        compoundTag.putString("author", string);
        compoundTag.putString("title", string2);
        compoundTag.putList(listTag);
        compoundTag.putInt("generation", 0);
        compoundTag.putString("xuid", "");
        return this.setNamedTag(compoundTag);
    }

    public boolean signBook(String string, String string2, String string3, int n) {
        this.setNamedTag((this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).putString("title", string).putString("author", string2).putInt("generation", n).putString("xuid", string3));
        return true;
    }

    public int getGeneration() {
        return this.hasCompoundTag() ? this.getNamedTag().getInt("generation") : -1;
    }

    public void setGeneration(int n) {
        this.setNamedTag((this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).putInt("generation", n));
    }

    public String getAuthor() {
        return this.hasCompoundTag() ? this.getNamedTag().getString("author") : "";
    }

    public void setAuthor(String string) {
        this.setNamedTag((this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).putString("author", string));
    }

    public String getTitle() {
        return this.hasCompoundTag() ? this.getNamedTag().getString("title") : "Written Book";
    }

    public void setTitle(String string) {
        this.setNamedTag((this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).putString("title", string));
    }

    public String getXUID() {
        return this.hasCompoundTag() ? this.getNamedTag().getString("xuid") : "";
    }

    public void setXUID(String string) {
        this.setNamedTag((this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).putString("xuid", string));
    }
}

