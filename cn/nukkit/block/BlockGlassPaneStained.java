/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockGlassPane;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockGlassPaneStained
extends BlockGlassPane {
    private int c;

    public BlockGlassPaneStained() {
        this(0);
    }

    public BlockGlassPaneStained(int n) {
        this.c = n;
    }

    @Override
    public int getFullId() {
        return 2560 + this.c;
    }

    @Override
    public int getId() {
        return 160;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Stained Glass Pane";
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

