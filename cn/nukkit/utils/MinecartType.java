/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum MinecartType {
    MINECART_EMPTY(0, false, "Minecart"),
    MINECART_CHEST(1, true, "Minecart with Chest"),
    MINECART_FURNACE(2, true, "Minecart with Furnace"),
    MINECART_TNT(3, true, "Minecart with TNT"),
    MINECART_MOB_SPAWNER(4, true, "Minecart with Mob Spawner"),
    MINECART_HOPPER(5, true, "Minecart with Hopper"),
    MINECART_COMMAND_BLOCK(6, true, "Minecart with Command Block"),
    MINECART_UNKNOWN(-1, false, "Unknown Minecart");

    private final int a;
    private final boolean c;
    private final String b;
    private static final Int2ObjectMap<MinecartType> d;

    private MinecartType(int n2, boolean bl, String string2) {
        this.a = n2;
        this.c = bl;
        this.b = string2;
    }

    public int getId() {
        return this.a;
    }

    public String getName() {
        return this.b;
    }

    public boolean hasBlockInside() {
        return this.c;
    }

    public static MinecartType valueOf(int n) {
        MinecartType minecartType = (MinecartType)((Object)d.get(n));
        return minecartType == null ? MINECART_UNKNOWN : minecartType;
    }

    static {
        d = new Int2ObjectOpenHashMap<MinecartType>();
        for (MinecartType minecartType : MinecartType.values()) {
            d.put(minecartType.a, minecartType);
        }
    }
}

