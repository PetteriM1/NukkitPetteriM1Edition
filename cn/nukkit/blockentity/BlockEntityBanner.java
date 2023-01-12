/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BannerPattern;
import cn.nukkit.utils.DyeColor;

public class BlockEntityBanner
extends BlockEntitySpawnable {
    public int color;

    public BlockEntityBanner(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("color")) {
            this.namedTag.putByte("color", 0);
        }
        this.color = this.namedTag.getByte("color");
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 177 || n == 176;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("color", this.color);
    }

    @Override
    public String getName() {
        return "Banner";
    }

    public int getBaseColor() {
        return this.namedTag.getInt("Base");
    }

    public void setBaseColor(DyeColor dyeColor) {
        this.namedTag.putInt("Base", dyeColor.getDyeData() & 0xF);
    }

    public int getType() {
        return this.namedTag.getInt("Type");
    }

    public void setType(int n) {
        this.namedTag.putInt("Type", n);
    }

    public void addPattern(BannerPattern bannerPattern) {
        ListTag<CompoundTag> listTag = this.namedTag.getList("Patterns", CompoundTag.class);
        listTag.add(new CompoundTag("").putInt("Color", bannerPattern.getColor().getDyeData() & 0xF).putString("Pattern", bannerPattern.getType().getName()));
        this.namedTag.putList(listTag);
    }

    public BannerPattern getPattern(int n) {
        return BannerPattern.fromCompoundTag(this.namedTag.getList("Patterns").size() > n && n >= 0 ? this.namedTag.getList("Patterns", CompoundTag.class).get(n) : new CompoundTag());
    }

    public void removePattern(int n) {
        ListTag<CompoundTag> listTag = this.namedTag.getList("Patterns", CompoundTag.class);
        if (listTag.size() > n && n >= 0) {
            listTag.remove(n);
        }
    }

    public int getPatternsSize() {
        return this.namedTag.getList("Patterns").size();
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return BlockEntityBanner.getDefaultCompound(this, "Banner").putInt("Base", this.getBaseColor()).putList(this.namedTag.getList("Patterns")).putInt("Type", this.getType()).putByte("color", this.color);
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.color);
    }
}

