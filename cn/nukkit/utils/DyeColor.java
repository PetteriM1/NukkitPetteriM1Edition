/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.BlockColor;

public enum DyeColor {
    BLACK(0, 15, "Black", "Ink Sac", BlockColor.BLACK_BLOCK_COLOR, new BlockColor(0, 0, 0)),
    RED(1, 14, "Red", BlockColor.RED_BLOCK_COLOR, new BlockColor(176, 46, 38)),
    GREEN(2, 13, "Green", BlockColor.GREEN_BLOCK_COLOR, new BlockColor(94, 124, 22)),
    BROWN(3, 12, "Brown", "Cocoa Beans", BlockColor.BROWN_BLOCK_COLOR, new BlockColor(131, 84, 50)),
    BLUE(4, 11, "Blue", "Lapis Lazuli", BlockColor.BLUE_BLOCK_COLOR, new BlockColor(60, 68, 170)),
    PURPLE(5, 10, "Purple", BlockColor.PURPLE_BLOCK_COLOR, new BlockColor(137, 50, 184)),
    CYAN(6, 9, "Cyan", BlockColor.CYAN_BLOCK_COLOR, new BlockColor(22, 156, 156)),
    LIGHT_GRAY(7, 8, "Light Gray", BlockColor.LIGHT_GRAY_BLOCK_COLOR, new BlockColor(157, 157, 151)),
    GRAY(8, 7, "Gray", BlockColor.GRAY_BLOCK_COLOR, new BlockColor(71, 79, 82)),
    PINK(9, 6, "Pink", BlockColor.PINK_BLOCK_COLOR, new BlockColor(243, 139, 170)),
    LIME(10, 5, "Lime", BlockColor.LIME_BLOCK_COLOR, new BlockColor(128, 199, 31)),
    YELLOW(11, 4, "Yellow", BlockColor.YELLOW_BLOCK_COLOR, new BlockColor(254, 216, 61)),
    LIGHT_BLUE(12, 3, "Light Blue", BlockColor.LIGHT_BLUE_BLOCK_COLOR, new BlockColor(58, 179, 218)),
    MAGENTA(13, 2, "Magenta", BlockColor.MAGENTA_BLOCK_COLOR, new BlockColor(199, 78, 189)),
    ORANGE(14, 1, "Orange", BlockColor.ORANGE_BLOCK_COLOR, new BlockColor(249, 128, 29)),
    WHITE(15, 0, "White", "Bone Meal", BlockColor.WHITE_BLOCK_COLOR, new BlockColor(240, 240, 240));

    private final int h;
    private final int g;
    private final String f;
    private final String b;
    private final BlockColor d;
    private final BlockColor e;
    private static final DyeColor[] i;
    private static final DyeColor[] a;

    private DyeColor(int n2, int n3, String string2, BlockColor blockColor, BlockColor blockColor2) {
        this(n2, n3, string2, string2 + " Dye", blockColor, blockColor2);
    }

    private DyeColor(int n2, int n3, String string2, String string3, BlockColor blockColor, BlockColor blockColor2) {
        this.h = n2;
        this.g = n3;
        this.f = string2;
        this.d = blockColor;
        this.e = blockColor2;
        this.b = string3;
    }

    public BlockColor getColor() {
        return this.d;
    }

    public BlockColor getSignColor() {
        return this.e;
    }

    public int getDyeData() {
        return this.h;
    }

    public int getWoolData() {
        return this.g;
    }

    public String getName() {
        return this.f;
    }

    public String getDyeName() {
        return this.b;
    }

    public static DyeColor getByDyeData(int n) {
        return a[n & 0xF];
    }

    public static DyeColor getByWoolData(int n) {
        return i[n & 0xF];
    }

    static {
        a = DyeColor.values();
        i = DyeColor.values();
        DyeColor[] dyeColorArray = DyeColor.values();
        int n = dyeColorArray.length;
        for (int k = 0; k < n; ++k) {
            DyeColor dyeColor;
            DyeColor.i[dyeColor.g & 0xF] = dyeColor = dyeColorArray[k];
            DyeColor.a[dyeColor.h & 0xF] = dyeColor;
        }
    }
}

