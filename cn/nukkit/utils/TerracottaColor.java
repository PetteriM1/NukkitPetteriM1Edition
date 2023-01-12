/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.BlockColor;

public enum TerracottaColor {
    BLACK(0, 15, "Black", "Ink Sac", BlockColor.BLACK_TERRACOTA_BLOCK_COLOR),
    RED(1, 14, "Red", "Rose Red", BlockColor.RED_TERRACOTA_BLOCK_COLOR),
    GREEN(2, 13, "Green", "Cactus Green", BlockColor.GREEN_TERRACOTA_BLOCK_COLOR),
    BROWN(3, 12, "Brown", "Cocoa Beans", BlockColor.BROWN_TERRACOTA_BLOCK_COLOR),
    BLUE(4, 11, "Blue", "Lapis Lazuli", BlockColor.BLUE_TERRACOTA_BLOCK_COLOR),
    PURPLE(5, 10, "Purple", BlockColor.PURPLE_TERRACOTA_BLOCK_COLOR),
    CYAN(6, 9, "Cyan", BlockColor.CYAN_TERRACOTA_BLOCK_COLOR),
    LIGHT_GRAY(7, 8, "Light Gray", BlockColor.LIGHT_GRAY_TERRACOTA_BLOCK_COLOR),
    GRAY(8, 7, "Gray", BlockColor.GRAY_TERRACOTA_BLOCK_COLOR),
    PINK(9, 6, "Pink", BlockColor.PINK_TERRACOTA_BLOCK_COLOR),
    LIME(10, 5, "Lime", BlockColor.LIME_TERRACOTA_BLOCK_COLOR),
    YELLOW(11, 4, "Yellow", "Dandelion Yellow", BlockColor.YELLOW_TERRACOTA_BLOCK_COLOR),
    LIGHT_BLUE(12, 3, "Light Blue", BlockColor.LIGHT_BLUE_TERRACOTA_BLOCK_COLOR),
    MAGENTA(13, 2, "Magenta", BlockColor.MAGENTA_TERRACOTA_BLOCK_COLOR),
    ORANGE(14, 1, "Orange", BlockColor.ORANGE_TERRACOTA_BLOCK_COLOR),
    WHITE(15, 0, "White", "Bone Meal", BlockColor.WHITE_TERRACOTA_BLOCK_COLOR);

    private final int g;
    private final int f;
    private final String e;
    private final String c;
    private final BlockColor b;
    private static final TerracottaColor[] a;
    private static final TerracottaColor[] h;

    private TerracottaColor(int n2, int n3, String string2, BlockColor blockColor) {
        this(n2, n3, string2, string2 + " Dye", blockColor);
    }

    private TerracottaColor(int n2, int n3, String string2, String string3, BlockColor blockColor) {
        this.g = n2;
        this.f = n3;
        this.e = string2;
        this.b = blockColor;
        this.c = string3;
    }

    public BlockColor getColor() {
        return this.b;
    }

    public int getDyeData() {
        return this.g;
    }

    public int getTerracottaData() {
        return this.f;
    }

    public String getName() {
        return this.e;
    }

    public String getDyeName() {
        return this.c;
    }

    public static TerracottaColor getByDyeData(int n) {
        return h[n & 0xF];
    }

    public static TerracottaColor getByTerracottaData(int n) {
        return a[n & 0xF];
    }

    static {
        h = TerracottaColor.values();
        a = TerracottaColor.values();
        TerracottaColor[] terracottaColorArray = TerracottaColor.values();
        int n = terracottaColorArray.length;
        for (int k = 0; k < n; ++k) {
            TerracottaColor terracottaColor;
            TerracottaColor.a[terracottaColor.f & 0xF] = terracottaColor = terracottaColorArray[k];
            TerracottaColor.h[terracottaColor.g & 0xF] = terracottaColor;
        }
    }
}

