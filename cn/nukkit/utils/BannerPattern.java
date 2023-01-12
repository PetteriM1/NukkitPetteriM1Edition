/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;
import java.util.HashMap;
import java.util.Map;

public class BannerPattern {
    private final Type b;
    private final DyeColor a;

    public BannerPattern(Type type, DyeColor dyeColor) {
        this.b = type;
        this.a = dyeColor;
    }

    public DyeColor getColor() {
        return this.a;
    }

    public Type getType() {
        return this.b;
    }

    public static BannerPattern fromCompoundTag(CompoundTag compoundTag) {
        return new BannerPattern(Type.getByName(compoundTag.contains("Pattern") ? compoundTag.getString("Pattern") : ""), compoundTag.contains("Color") ? DyeColor.getByDyeData(compoundTag.getInt("Color")) : DyeColor.BLACK);
    }

    public static enum Type {
        PATTERN_BOTTOM_STRIPE("bs"),
        PATTERN_TOP_STRIPE("ts"),
        PATTERN_LEFT_STRIPE("ls"),
        PATTERN_RIGHT_STRIPE("rs"),
        PATTERN_CENTER_STRIPE("cs"),
        PATTERN_MIDDLE_STRIPE("ms"),
        PATTERN_DOWN_RIGHT_STRIPE("drs"),
        PATTERN_DOWN_LEFT_STRIPE("dls"),
        PATTERN_SMALL_STRIPES("ss"),
        PATTERN_DIAGONAL_CROSS("cr"),
        PATTERN_SQUARE_CROSS("sc"),
        PATTERN_LEFT_OF_DIAGONAL("ld"),
        PATTERN_RIGHT_OF_UPSIDE_DOWN_DIAGONAL("rud"),
        PATTERN_LEFT_OF_UPSIDE_DOWN_DIAGONAL("lud"),
        PATTERN_RIGHT_OF_DIAGONAL("rd"),
        PATTERN_VERTICAL_HALF_LEFT("vh"),
        PATTERN_VERTICAL_HALF_RIGHT("vhr"),
        PATTERN_HORIZONTAL_HALF_TOP("hh"),
        PATTERN_HORIZONTAL_HALF_BOTTOM("hhb"),
        PATTERN_BOTTOM_LEFT_CORNER("bl"),
        PATTERN_BOTTOM_RIGHT_CORNER("br"),
        PATTERN_TOP_LEFT_CORNER("tl"),
        PATTERN_TOP_RIGHT_CORNER("tr"),
        PATTERN_BOTTOM_TRIANGLE("bt"),
        PATTERN_TOP_TRIANGLE("tt"),
        PATTERN_BOTTOM_TRIANGLE_SAWTOOTH("bts"),
        PATTERN_TOP_TRIANGLE_SAWTOOTH("tts"),
        PATTERN_MIDDLE_CIRCLE("mc"),
        PATTERN_MIDDLE_RHOMBUS("mr"),
        PATTERN_BORDER("bo"),
        PATTERN_CURLY_BORDER("cbo"),
        PATTERN_BRICK("bri"),
        PATTERN_GRADIENT("gra"),
        PATTERN_GRADIENT_UPSIDE_DOWN("gru"),
        PATTERN_CREEPER("cre"),
        PATTERN_SKULL("sku"),
        PATTERN_FLOWER("flo"),
        PATTERN_MOJANG("moj");

        private static final Map<String, Type> a;
        private final String b;

        private Type(String string2) {
            this.b = string2;
        }

        public String getName() {
            return this.b;
        }

        public static Type getByName(String string) {
            return a.get(string);
        }

        static {
            a = new HashMap<String, Type>();
            for (Type type : Type.values()) {
                a.put(type.getName(), type);
            }
        }
    }
}

