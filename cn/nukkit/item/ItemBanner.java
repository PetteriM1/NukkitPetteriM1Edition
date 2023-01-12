/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BannerPattern;
import cn.nukkit.utils.DyeColor;

public class ItemBanner
extends Item {
    public ItemBanner() {
        this((Integer)0);
    }

    public ItemBanner(Integer n) {
        this(n, 1);
    }

    public ItemBanner(Integer n, int n2) {
        super(446, n, n2, "Banner");
        this.block = Block.get(176);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    public int getBaseColor() {
        return this.getDamage() & 0xF;
    }

    public void setBaseColor(DyeColor dyeColor) {
        this.setDamage(dyeColor.getDyeData() & 0xF);
    }

    public int getType() {
        return this.getNamedTag().getInt("Type");
    }

    public void setType(int n) {
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        compoundTag.putInt("Type", n);
        this.setNamedTag(compoundTag);
    }

    public void addPattern(BannerPattern bannerPattern) {
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        ListTag<CompoundTag> listTag = compoundTag.getList("Patterns", CompoundTag.class);
        listTag.add(new CompoundTag("").putInt("Color", bannerPattern.getColor().getDyeData() & 0xF).putString("Pattern", bannerPattern.getType().getName()));
        compoundTag.putList(listTag);
        this.setNamedTag(compoundTag);
    }

    public BannerPattern getPattern(int n) {
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        return BannerPattern.fromCompoundTag(compoundTag.getList("Patterns").size() > n && n >= 0 ? compoundTag.getList("Patterns", CompoundTag.class).get(n) : new CompoundTag());
    }

    public void removePattern(int n) {
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        ListTag<CompoundTag> listTag = compoundTag.getList("Patterns", CompoundTag.class);
        if (listTag.size() > n && n >= 0) {
            listTag.remove(n);
        }
        this.setNamedTag(compoundTag);
    }

    public int getPatternsSize() {
        return (this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).getList("Patterns").size();
    }

    public boolean hasPattern() {
        return (this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).contains("Patterns");
    }
}

