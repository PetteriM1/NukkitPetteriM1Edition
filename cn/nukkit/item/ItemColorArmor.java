/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemArmor;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public abstract class ItemColorArmor
extends ItemArmor {
    public ItemColorArmor(int n) {
        super(n);
    }

    public ItemColorArmor(int n, Integer n2) {
        super(n, n2);
    }

    public ItemColorArmor(int n, Integer n2, int n3) {
        super(n, n2, n3);
    }

    public ItemColorArmor(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    public ItemColorArmor setColor(int n) {
        BlockColor blockColor = DyeColor.getByDyeData(n).getColor();
        return this.setColor(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public ItemColorArmor setColor(DyeColor dyeColor) {
        BlockColor blockColor = dyeColor.getColor();
        return this.setColor(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public ItemColorArmor setColor(BlockColor blockColor) {
        return this.setColor(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public ItemColorArmor setColor(int n, int n2, int n3) {
        int n4 = n << 16 | n2 << 8 | n3;
        CompoundTag compoundTag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        compoundTag.putInt("customColor", n4);
        this.setNamedTag(compoundTag);
        return this;
    }

    public BlockColor getColor() {
        if (!this.hasCompoundTag()) {
            return null;
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (!compoundTag.exist("customColor")) {
            return null;
        }
        int n = compoundTag.getInt("customColor");
        return new BlockColor(n);
    }
}

