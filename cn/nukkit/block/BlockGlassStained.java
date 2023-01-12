/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockGlass;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockGlassStained
extends BlockGlass {
    private int c;

    public BlockGlassStained() {
        this(0);
    }

    public BlockGlassStained(int n) {
        this.c = n;
    }

    @Override
    public int getFullId() {
        return 3856 + this.c;
    }

    @Override
    public int getId() {
        return 241;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Stained Glass";
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByWoolData(this.c).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.c);
    }

    @Override
    public final int getDamage() {
        return this.c;
    }

    @Override
    public final void setDamage(int n) {
        this.c = n;
    }
}

