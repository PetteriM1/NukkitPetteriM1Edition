/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.utils.ServerException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Objects;

public class Attribute
implements Cloneable {
    public static final int ABSORPTION = 0;
    public static final int SATURATION = 1;
    public static final int EXHAUSTION = 2;
    public static final int KNOCKBACK_RESISTANCE = 3;
    public static final int MAX_HEALTH = 4;
    public static final int MOVEMENT_SPEED = 5;
    public static final int FOLLOW_RANGE = 6;
    public static final int FOOD = 7;
    public static final int MAX_HUNGER = 7;
    public static final int ATTACK_DAMAGE = 8;
    public static final int EXPERIENCE_LEVEL = 9;
    public static final int EXPERIENCE = 10;
    public static final int UNDERWATER_MOVEMENT = 11;
    public static final int LUCK = 12;
    public static final int FALL_DAMAGE = 13;
    public static final int HORSE_JUMP_STRENGTH = 14;
    public static final int ZOMBIE_SPAWN_REINFORCEMENTS = 15;
    public static final int LAVA_MOVEMENT = 16;
    protected static Int2ObjectMap<Attribute> attributes = new Int2ObjectOpenHashMap<Attribute>();
    protected float minValue;
    protected float maxValue;
    protected float defaultValue;
    protected float currentValue;
    protected String name;
    protected boolean shouldSend;
    private final int a;

    private Attribute(int n, String string, float f2, float f3, float f4, boolean bl) {
        this.a = n;
        this.name = string;
        this.minValue = f2;
        this.maxValue = f3;
        this.defaultValue = f4;
        this.shouldSend = bl;
        this.currentValue = this.defaultValue;
    }

    public static void init() {
        Attribute.addAttribute(0, "minecraft:absorption", 0.0f, Float.MAX_VALUE, 0.0f);
        Attribute.addAttribute(1, "minecraft:player.saturation", 0.0f, 20.0f, 5.0f);
        Attribute.addAttribute(2, "minecraft:player.exhaustion", 0.0f, 5.0f, 0.0f, false);
        Attribute.addAttribute(3, "minecraft:knockback_resistance", 0.0f, 1.0f, 0.0f);
        Attribute.addAttribute(4, "minecraft:health", 0.0f, 20.0f, 20.0f);
        Attribute.addAttribute(5, "minecraft:movement", 0.0f, Float.MAX_VALUE, 0.1f);
        Attribute.addAttribute(6, "minecraft:follow_range", 0.0f, 2048.0f, 16.0f, false);
        Attribute.addAttribute(7, "minecraft:player.hunger", 0.0f, 20.0f, 20.0f);
        Attribute.addAttribute(8, "minecraft:attack_damage", 0.0f, Float.MAX_VALUE, 1.0f, false);
        Attribute.addAttribute(9, "minecraft:player.level", 0.0f, 24791.0f, 0.0f);
        Attribute.addAttribute(10, "minecraft:player.experience", 0.0f, 1.0f, 0.0f);
        Attribute.addAttribute(11, "minecraft:underwater_movement", 0.0f, Float.MAX_VALUE, 0.02f);
        Attribute.addAttribute(12, "minecraft:luck", -1024.0f, 1024.0f, 0.0f);
        Attribute.addAttribute(13, "minecraft:fall_damage", 0.0f, Float.MAX_VALUE, 1.0f);
        Attribute.addAttribute(14, "minecraft:horse.jump_strength", 0.0f, 2.0f, 0.7f);
        Attribute.addAttribute(15, "minecraft:zombie.spawn_reinforcements", 0.0f, 1.0f, 0.0f);
        Attribute.addAttribute(16, "minecraft:lava_movement", 0.0f, Float.MAX_VALUE, 0.02f);
    }

    public static Attribute addAttribute(int n, String string, float f2, float f3, float f4) {
        return Attribute.addAttribute(n, string, f2, f3, f4, true);
    }

    public static Attribute addAttribute(int n, String string, float f2, float f3, float f4, boolean bl) {
        if (f2 > f3 || f4 > f3 || f4 < f2) {
            throw new IllegalArgumentException("Invalid ranges: min value: " + f2 + ", max value: " + f3 + ", defaultValue: " + f4);
        }
        return attributes.put(n, new Attribute(n, string, f2, f3, f4, bl));
    }

    public static Attribute getAttribute(int n) {
        if (attributes.containsKey(n)) {
            return ((Attribute)attributes.get(n)).clone();
        }
        throw new ServerException("Attribute id: " + n + " not found");
    }

    public static Attribute getAttributeByName(String string) {
        for (Attribute attribute : attributes.values()) {
            if (!Objects.equals(attribute.name, string)) continue;
            return attribute.clone();
        }
        return null;
    }

    public float getMinValue() {
        return this.minValue;
    }

    public Attribute setMinValue(float f2) {
        if (f2 > this.maxValue) {
            throw new IllegalArgumentException("Value " + f2 + " is bigger than the maxValue!");
        }
        this.minValue = f2;
        return this;
    }

    public float getMaxValue() {
        return this.maxValue;
    }

    public Attribute setMaxValue(float f2) {
        if (f2 < this.minValue) {
            throw new IllegalArgumentException("Value " + f2 + " is bigger than the minValue!");
        }
        this.maxValue = f2;
        return this;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public Attribute setDefaultValue(float f2) {
        if (f2 > this.maxValue || f2 < this.minValue) {
            throw new IllegalArgumentException("Value " + f2 + " exceeds the range!");
        }
        this.defaultValue = f2;
        return this;
    }

    public float getValue() {
        return this.currentValue;
    }

    public Attribute setValue(float f2) {
        return this.setValue(f2, true);
    }

    public Attribute setValue(float f2, boolean bl) {
        if (f2 > this.maxValue || f2 < this.minValue) {
            if (!bl) {
                throw new IllegalArgumentException("Value " + f2 + " exceeds the range!");
            }
            f2 = Math.min(Math.max(f2, this.minValue), this.maxValue);
        }
        this.currentValue = f2;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.a;
    }

    public boolean isSyncable() {
        return this.shouldSend;
    }

    public Attribute clone() {
        try {
            return (Attribute)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

