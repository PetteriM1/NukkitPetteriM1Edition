/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;

public class ItemArrow
extends Item {
    public ItemArrow() {
        this((Integer)0, 1);
    }

    public ItemArrow(Integer n) {
        this(n, 1);
    }

    public ItemArrow(Integer n, int n2) {
        super(262, n, n2, "Arrow");
    }

    public static Effect getEffect(int n) {
        switch (n) {
            case 6: {
                return Effect.getEffect(16).setDuration(440);
            }
            case 7: {
                return Effect.getEffect(16).setDuration(1200);
            }
            case 8: {
                return Effect.getEffect(14).setDuration(440);
            }
            case 9: {
                return Effect.getEffect(14).setDuration(1200);
            }
            case 10: {
                return Effect.getEffect(8).setDuration(440);
            }
            case 11: {
                return Effect.getEffect(8).setDuration(1200);
            }
            case 12: {
                return Effect.getEffect(8).setAmplifier(1).setDuration(220);
            }
            case 13: {
                return Effect.getEffect(12).setDuration(440);
            }
            case 14: {
                return Effect.getEffect(12).setDuration(1200);
            }
            case 15: {
                return Effect.getEffect(1).setDuration(440);
            }
            case 16: {
                return Effect.getEffect(1).setDuration(1200);
            }
            case 17: {
                return Effect.getEffect(1).setAmplifier(1).setDuration(220);
            }
            case 18: {
                return Effect.getEffect(2).setDuration(220);
            }
            case 19: {
                return Effect.getEffect(2).setDuration(600);
            }
            case 20: {
                return Effect.getEffect(13).setDuration(440);
            }
            case 21: {
                return Effect.getEffect(13).setDuration(1200);
            }
            case 22: {
                return Effect.getEffect(6).setDuration(1);
            }
            case 23: {
                return Effect.getEffect(6).setAmplifier(1).setDuration(1);
            }
            case 24: {
                return Effect.getEffect(7).setDuration(1);
            }
            case 25: {
                return Effect.getEffect(7).setAmplifier(1).setDuration(1);
            }
            case 26: {
                return Effect.getEffect(19).setDuration(100);
            }
            case 27: {
                return Effect.getEffect(19).setDuration(300);
            }
            case 28: {
                return Effect.getEffect(19).setAmplifier(1).setDuration(40);
            }
            case 29: {
                return Effect.getEffect(10).setDuration(100);
            }
            case 30: {
                return Effect.getEffect(10).setDuration(300);
            }
            case 31: {
                return Effect.getEffect(10).setAmplifier(1).setDuration(40);
            }
            case 32: {
                return Effect.getEffect(5).setDuration(440);
            }
            case 33: {
                return Effect.getEffect(5).setDuration(1200);
            }
            case 34: {
                return Effect.getEffect(5).setAmplifier(1).setDuration(220);
            }
            case 35: {
                return Effect.getEffect(18).setDuration(220);
            }
            case 36: {
                return Effect.getEffect(18).setDuration(600);
            }
            case 37: {
                return Effect.getEffect(20).setAmplifier(1).setDuration(100);
            }
            case 38: {
                return Effect.getEffect(2).setAmplifier(3).setDuration(40);
            }
            case 39: {
                return Effect.getEffect(2).setAmplifier(3).setDuration(100);
            }
            case 40: {
                return Effect.getEffect(2).setAmplifier(5).setDuration(40);
            }
            case 41: {
                return Effect.getEffect(27).setDuration(220);
            }
            case 42: {
                return Effect.getEffect(27).setDuration(600);
            }
            case 43: {
                return Effect.getEffect(2).setAmplifier(3).setDuration(40);
            }
        }
        return null;
    }
}

