/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

public enum ClientPlayMode {
    NORMAL,
    TEASER,
    SCREEN,
    VIEWER,
    REALITY,
    PLACEMENT,
    LIVING_ROOM,
    EXIT_LEVEL,
    EXIT_LEVEL_LIVING_ROOM;

    private static final ClientPlayMode[] a;

    public static ClientPlayMode fromOrdinal(int n) {
        return a[n];
    }

    static {
        a = ClientPlayMode.values();
    }
}

