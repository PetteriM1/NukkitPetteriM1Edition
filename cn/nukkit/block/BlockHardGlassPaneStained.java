/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockHardGlassPane;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockHardGlassPaneStained
extends BlockHardGlassPane {
    private int c;

    public BlockHardGlassPaneStained() {
        this(0);
    }

    public BlockHardGlassPaneStained(int n) {
        this.c = n;
    }

    @Override
    public int getFullId() {
        return 3056 + this.c;
    }

    @Override
    public int getId() {
        return 191;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Hardened Stained Glass Pane";
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
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

