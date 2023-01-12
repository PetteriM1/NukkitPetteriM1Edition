/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import com.google.common.base.Preconditions;
import java.util.List;

public abstract class ItemBookWritable
extends Item {
    protected ItemBookWritable(int n) {
        super(n);
    }

    protected ItemBookWritable(int n, Integer n2) {
        super(n, n2);
    }

    protected ItemBookWritable(int n, Integer n2, int n3) {
        super(n, n2, n3);
    }

    protected ItemBookWritable(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    public boolean pageExists(int n) {
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        if (this.hasCompoundTag() && (compoundTag = this.getNamedTag()).contains("pages") && compoundTag.get("pages") instanceof ListTag) {
            return compoundTag.getList("pages", CompoundTag.class).size() > n;
        }
        return false;
    }

    public String getPageText(int n) {
        ListTag<CompoundTag> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        if (this.hasCompoundTag() && (compoundTag = this.getNamedTag()).contains("pages") && compoundTag.get("pages") instanceof ListTag && (listTag = compoundTag.getList("pages", CompoundTag.class)).size() > n) {
            return listTag.get(n).getString("text");
        }
        return null;
    }

    public boolean setPageText(int n, String string) {
        ListTag<Object> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        if (this.hasCompoundTag()) {
            compoundTag = this.getNamedTag();
        } else {
            if (string.isEmpty()) {
                return false;
            }
            compoundTag = new CompoundTag();
        }
        if (!compoundTag.contains("pages") || !(compoundTag.get("pages") instanceof ListTag)) {
            listTag = new ListTag("pages");
            compoundTag.putList(listTag);
        } else {
            listTag = compoundTag.getList("pages", CompoundTag.class);
        }
        if (listTag.size() <= n) {
            for (int k = listTag.size(); k <= n; ++k) {
                listTag.add(ItemBookWritable.createPageTag());
            }
        }
        ((CompoundTag)listTag.get(n)).putString("text", string);
        this.setCompoundTag(compoundTag);
        return true;
    }

    public boolean addPage(int n) {
        ListTag<Object> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        CompoundTag compoundTag2 = compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        if (!compoundTag.contains("pages") || !(compoundTag.get("pages") instanceof ListTag)) {
            listTag = new ListTag("pages");
            compoundTag.putList(listTag);
        } else {
            listTag = compoundTag.getList("pages", CompoundTag.class);
        }
        for (int k = listTag.size(); k <= n; ++k) {
            listTag.add(ItemBookWritable.createPageTag());
        }
        this.setCompoundTag(compoundTag);
        return true;
    }

    public boolean deletePage(int n) {
        ListTag<CompoundTag> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        if (this.hasCompoundTag() && (compoundTag = this.getNamedTag()).contains("pages") && compoundTag.get("pages") instanceof ListTag && (listTag = compoundTag.getList("pages", CompoundTag.class)).size() > n) {
            listTag.remove(n);
            this.setCompoundTag(compoundTag);
        }
        return true;
    }

    public boolean insertPage(int n) {
        return this.insertPage(n, "");
    }

    public boolean insertPage(int n, String string) {
        ListTag<Object> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        CompoundTag compoundTag2 = compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        if (!compoundTag.contains("pages") || !(compoundTag.get("pages") instanceof ListTag)) {
            listTag = new ListTag("pages");
            compoundTag.putList(listTag);
        } else {
            listTag = compoundTag.getList("pages", CompoundTag.class);
        }
        if (listTag.size() <= n) {
            for (int k = listTag.size(); k <= n; ++k) {
                listTag.add(ItemBookWritable.createPageTag());
            }
            ((CompoundTag)listTag.get(n)).putString("text", string);
        } else {
            listTag.add(n, ItemBookWritable.createPageTag(string));
        }
        this.setCompoundTag(compoundTag);
        return true;
    }

    public boolean swapPages(int n, int n2) {
        ListTag<CompoundTag> listTag;
        CompoundTag compoundTag;
        Preconditions.checkArgument(n >= 0 && n < 50, "Page number " + n + " is out of range");
        Preconditions.checkArgument(n2 >= 0 && n2 < 50, "Page number " + n2 + " is out of range");
        if (this.hasCompoundTag() && (compoundTag = this.getNamedTag()).contains("pages") && compoundTag.get("pages") instanceof ListTag && (listTag = compoundTag.getList("pages", CompoundTag.class)).size() > n && listTag.size() > n2) {
            String string = listTag.get(n).getString("text");
            String string2 = listTag.get(n2).getString("text");
            listTag.get(n).putString("text", string2);
            listTag.get(n2).putString("text", string);
            return true;
        }
        return false;
    }

    public List getPages() {
        ListTag<Object> listTag;
        CompoundTag compoundTag;
        CompoundTag compoundTag2 = compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        if (!compoundTag.contains("pages") || !(compoundTag.get("pages") instanceof ListTag)) {
            listTag = new ListTag("pages");
            compoundTag.putList(listTag);
        } else {
            listTag = compoundTag.getList("pages", CompoundTag.class);
        }
        return listTag.parseValue();
    }

    protected static CompoundTag createPageTag() {
        return ItemBookWritable.createPageTag("");
    }

    protected static CompoundTag createPageTag(String string) {
        return new CompoundTag().putString("text", string).putString("photoname", "");
    }
}

