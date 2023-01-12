/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.DyeColor;

public class BlockColor {
    public static final BlockColor TRANSPARENT_BLOCK_COLOR;
    public static final BlockColor VOID_BLOCK_COLOR;
    public static final BlockColor AIR_BLOCK_COLOR;
    public static final BlockColor GRASS_BLOCK_COLOR;
    public static final BlockColor SAND_BLOCK_COLOR;
    public static final BlockColor CLOTH_BLOCK_COLOR;
    public static final BlockColor TNT_BLOCK_COLOR;
    public static final BlockColor ICE_BLOCK_COLOR;
    public static final BlockColor IRON_BLOCK_COLOR;
    public static final BlockColor FOLIAGE_BLOCK_COLOR;
    public static final BlockColor SNOW_BLOCK_COLOR;
    public static final BlockColor CLAY_BLOCK_COLOR;
    public static final BlockColor DIRT_BLOCK_COLOR;
    public static final BlockColor STONE_BLOCK_COLOR;
    public static final BlockColor WATER_BLOCK_COLOR;
    public static final BlockColor LAVA_BLOCK_COLOR;
    public static final BlockColor WOOD_BLOCK_COLOR;
    public static final BlockColor QUARTZ_BLOCK_COLOR;
    public static final BlockColor ADOBE_BLOCK_COLOR;
    public static final BlockColor WHITE_BLOCK_COLOR;
    public static final BlockColor ORANGE_BLOCK_COLOR;
    public static final BlockColor MAGENTA_BLOCK_COLOR;
    public static final BlockColor LIGHT_BLUE_BLOCK_COLOR;
    public static final BlockColor YELLOW_BLOCK_COLOR;
    public static final BlockColor LIME_BLOCK_COLOR;
    public static final BlockColor PINK_BLOCK_COLOR;
    public static final BlockColor GRAY_BLOCK_COLOR;
    public static final BlockColor LIGHT_GRAY_BLOCK_COLOR;
    public static final BlockColor CYAN_BLOCK_COLOR;
    public static final BlockColor PURPLE_BLOCK_COLOR;
    public static final BlockColor BLUE_BLOCK_COLOR;
    public static final BlockColor BROWN_BLOCK_COLOR;
    public static final BlockColor GREEN_BLOCK_COLOR;
    public static final BlockColor RED_BLOCK_COLOR;
    public static final BlockColor BLACK_BLOCK_COLOR;
    public static final BlockColor GOLD_BLOCK_COLOR;
    public static final BlockColor DIAMOND_BLOCK_COLOR;
    public static final BlockColor LAPIS_BLOCK_COLOR;
    public static final BlockColor EMERALD_BLOCK_COLOR;
    public static final BlockColor OBSIDIAN_BLOCK_COLOR;
    public static final BlockColor SPRUCE_BLOCK_COLOR;
    public static final BlockColor NETHERRACK_BLOCK_COLOR;
    public static final BlockColor REDSTONE_BLOCK_COLOR;
    public static final BlockColor WHITE_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor ORANGE_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor MAGENTA_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor LIGHT_BLUE_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor YELLOW_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor LIME_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor PINK_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor GRAY_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor LIGHT_GRAY_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor CYAN_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor PURPLE_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor BLUE_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor BROWN_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor GREEN_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor RED_TERRACOTA_BLOCK_COLOR;
    public static final BlockColor BLACK_TERRACOTA_BLOCK_COLOR;
    private final int b;
    private final int d;
    private final int c;
    private final int a;

    public BlockColor(int n, int n2, int n3, int n4) {
        this.b = n & 0xFF;
        this.d = n2 & 0xFF;
        this.c = n3 & 0xFF;
        this.a = n4 & 0xFF;
    }

    public BlockColor(int n, int n2, int n3) {
        this(n, n2, n3, 255);
    }

    public BlockColor(int n) {
        this(n, false);
    }

    public BlockColor(int n, boolean bl) {
        this.b = n >> 16 & 0xFF;
        this.d = n >> 8 & 0xFF;
        this.c = n & 0xFF;
        this.a = bl ? n >> 24 & 0xFF : 255;
    }

    public BlockColor(String string) {
        this.b = Integer.valueOf(string.substring(1, 3), 16);
        this.d = Integer.valueOf(string.substring(3, 5), 16);
        this.c = Integer.valueOf(string.substring(5, 7), 16);
        this.a = 255;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BlockColor)) {
            return false;
        }
        BlockColor blockColor = (BlockColor)object;
        return this.b == blockColor.b && this.d == blockColor.d && this.c == blockColor.c && this.a == blockColor.a;
    }

    public String toString() {
        return "BlockColor[r=" + this.b + ",g=" + this.d + ",b=" + this.c + ",a=" + this.a + ']';
    }

    public int getRed() {
        return this.b;
    }

    public int getGreen() {
        return this.d;
    }

    public int getBlue() {
        return this.c;
    }

    public int getAlpha() {
        return this.a;
    }

    public int getRGB() {
        return (this.b << 16 | this.d << 8 | this.c) & 0xFFFFFF;
    }

    public int getARGB() {
        return this.a << 24 | this.b << 16 | this.d << 8 | this.c;
    }

    public static BlockColor getDyeColor(int n) {
        return DyeColor.getByDyeData(n).getColor();
    }

    static {
        VOID_BLOCK_COLOR = TRANSPARENT_BLOCK_COLOR = new BlockColor(0, 0, 0, 0);
        AIR_BLOCK_COLOR = TRANSPARENT_BLOCK_COLOR;
        GRASS_BLOCK_COLOR = new BlockColor(127, 178, 56);
        SAND_BLOCK_COLOR = new BlockColor(247, 233, 163);
        CLOTH_BLOCK_COLOR = new BlockColor(199, 199, 199);
        TNT_BLOCK_COLOR = new BlockColor(255, 0, 0);
        ICE_BLOCK_COLOR = new BlockColor(160, 160, 255);
        IRON_BLOCK_COLOR = new BlockColor(167, 167, 167);
        FOLIAGE_BLOCK_COLOR = new BlockColor(0, 124, 0);
        SNOW_BLOCK_COLOR = new BlockColor(255, 255, 255);
        CLAY_BLOCK_COLOR = new BlockColor(164, 168, 184);
        DIRT_BLOCK_COLOR = new BlockColor(151, 109, 77);
        STONE_BLOCK_COLOR = new BlockColor(112, 112, 112);
        WATER_BLOCK_COLOR = new BlockColor(64, 64, 255);
        LAVA_BLOCK_COLOR = TNT_BLOCK_COLOR;
        WOOD_BLOCK_COLOR = new BlockColor(143, 119, 72);
        QUARTZ_BLOCK_COLOR = new BlockColor(255, 252, 245);
        ADOBE_BLOCK_COLOR = new BlockColor(216, 127, 51);
        WHITE_BLOCK_COLOR = SNOW_BLOCK_COLOR;
        ORANGE_BLOCK_COLOR = ADOBE_BLOCK_COLOR;
        MAGENTA_BLOCK_COLOR = new BlockColor(178, 76, 216);
        LIGHT_BLUE_BLOCK_COLOR = new BlockColor(102, 153, 216);
        YELLOW_BLOCK_COLOR = new BlockColor(229, 229, 51);
        LIME_BLOCK_COLOR = new BlockColor(127, 204, 25);
        PINK_BLOCK_COLOR = new BlockColor(242, 127, 165);
        GRAY_BLOCK_COLOR = new BlockColor(76, 76, 76);
        LIGHT_GRAY_BLOCK_COLOR = new BlockColor(153, 153, 153);
        CYAN_BLOCK_COLOR = new BlockColor(76, 127, 153);
        PURPLE_BLOCK_COLOR = new BlockColor(127, 63, 178);
        BLUE_BLOCK_COLOR = new BlockColor(51, 76, 178);
        BROWN_BLOCK_COLOR = new BlockColor(102, 76, 51);
        GREEN_BLOCK_COLOR = new BlockColor(102, 127, 51);
        RED_BLOCK_COLOR = new BlockColor(153, 51, 51);
        BLACK_BLOCK_COLOR = new BlockColor(25, 25, 25);
        GOLD_BLOCK_COLOR = new BlockColor(250, 238, 77);
        DIAMOND_BLOCK_COLOR = new BlockColor(92, 219, 213);
        LAPIS_BLOCK_COLOR = new BlockColor(74, 128, 255);
        EMERALD_BLOCK_COLOR = new BlockColor(0, 217, 58);
        OBSIDIAN_BLOCK_COLOR = new BlockColor(21, 20, 31);
        SPRUCE_BLOCK_COLOR = new BlockColor(129, 86, 49);
        NETHERRACK_BLOCK_COLOR = new BlockColor(112, 2, 0);
        REDSTONE_BLOCK_COLOR = TNT_BLOCK_COLOR;
        WHITE_TERRACOTA_BLOCK_COLOR = new BlockColor(209, 177, 161);
        ORANGE_TERRACOTA_BLOCK_COLOR = new BlockColor(159, 82, 36);
        MAGENTA_TERRACOTA_BLOCK_COLOR = new BlockColor(149, 87, 108);
        LIGHT_BLUE_TERRACOTA_BLOCK_COLOR = new BlockColor(112, 108, 138);
        YELLOW_TERRACOTA_BLOCK_COLOR = new BlockColor(186, 133, 36);
        LIME_TERRACOTA_BLOCK_COLOR = new BlockColor(103, 117, 53);
        PINK_TERRACOTA_BLOCK_COLOR = new BlockColor(160, 77, 78);
        GRAY_TERRACOTA_BLOCK_COLOR = new BlockColor(57, 41, 35);
        LIGHT_GRAY_TERRACOTA_BLOCK_COLOR = new BlockColor(135, 107, 98);
        CYAN_TERRACOTA_BLOCK_COLOR = new BlockColor(87, 92, 92);
        PURPLE_TERRACOTA_BLOCK_COLOR = new BlockColor(122, 73, 88);
        BLUE_TERRACOTA_BLOCK_COLOR = new BlockColor(76, 62, 92);
        BROWN_TERRACOTA_BLOCK_COLOR = new BlockColor(76, 50, 35);
        GREEN_TERRACOTA_BLOCK_COLOR = new BlockColor(76, 82, 42);
        RED_TERRACOTA_BLOCK_COLOR = new BlockColor(142, 60, 46);
        BLACK_TERRACOTA_BLOCK_COLOR = new BlockColor(37, 22, 16);
    }
}

