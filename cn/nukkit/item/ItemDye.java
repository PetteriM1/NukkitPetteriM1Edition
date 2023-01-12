/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class ItemDye
extends Item {
    public static final int WHITE = DyeColor.WHITE.getDyeData();
    public static final int ORANGE = DyeColor.ORANGE.getDyeData();
    public static final int MAGENTA = DyeColor.MAGENTA.getDyeData();
    public static final int LIGHT_BLUE = DyeColor.LIGHT_BLUE.getDyeData();
    public static final int YELLOW = DyeColor.YELLOW.getDyeData();
    public static final int LIME = DyeColor.LIME.getDyeData();
    public static final int PINK = DyeColor.PINK.getDyeData();
    public static final int GRAY = DyeColor.GRAY.getDyeData();
    public static final int LIGHT_GRAY = DyeColor.LIGHT_GRAY.getDyeData();
    public static final int CYAN = DyeColor.CYAN.getDyeData();
    public static final int PURPLE = DyeColor.PURPLE.getDyeData();
    public static final int BLUE = DyeColor.BLUE.getDyeData();
    public static final int BROWN = DyeColor.BROWN.getDyeData();
    public static final int GREEN = DyeColor.GREEN.getDyeData();
    public static final int RED = DyeColor.RED.getDyeData();
    public static final int BLACK = DyeColor.BLACK.getDyeData();
    public static final int INK_SAC = 0;
    public static final int COCOA_BEANS = 3;
    public static final int LAPIS_LAZULI = 4;
    public static final int BONE_MEAL = 15;
    public static final int BLACK_NEW = 16;
    public static final int BROWN_NEW = 17;
    public static final int BLUE_NEW = 18;
    public static final int WHITE_NEW = 19;
    public static final int GLOW_INK_SAC = 20;
    private static final String[] x = new String[21];

    public ItemDye() {
        this((Integer)0, 1);
    }

    public ItemDye(Integer n) {
        this(n, 1);
    }

    public ItemDye(DyeColor dyeColor) {
        this((Integer)dyeColor.getDyeData(), 1);
    }

    public ItemDye(DyeColor dyeColor, int n) {
        this((Integer)dyeColor.getDyeData(), n);
    }

    public ItemDye(Integer n, int n2) {
        super(351, n, n2, n >= 0 && n <= 20 ? x[n] : "Unknown");
        if (this.meta == DyeColor.BROWN.getDyeData()) {
            this.block = Block.get(127);
        }
    }

    public static BlockColor getColor(int n) {
        return DyeColor.getByDyeData(n).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByDyeData(this.meta);
    }

    public static String getColorName(int n) {
        return DyeColor.getByDyeData(n).getName();
    }

    @Override
    public boolean isSupportedOn(int n) {
        return this.meta < 20 || n >= 440;
    }

    static {
        for (int k = 0; k < 16; ++k) {
            ItemDye.x[k] = DyeColor.getByDyeData(k).getDyeName();
        }
        ItemDye.x[16] = "Black Dye";
        ItemDye.x[17] = "Brown Dye";
        ItemDye.x[18] = "Blue Dye";
        ItemDye.x[19] = "White Dye";
        ItemDye.x[20] = "Glow Ink Sac";
    }
}

