/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

public enum InputMode {
    UNDEFINED(0),
    MOUSE(1),
    TOUCH(2),
    GAME_PAD(3),
    MOTION_CONTROLLER(4),
    COUNT(5);

    private final int c;
    private static final InputMode[] a;

    private InputMode(int n2) {
        this.c = n2;
    }

    public int getOrdinal() {
        return this.c;
    }

    public static InputMode fromOrdinal(int n) {
        return a[n];
    }

    static {
        a = InputMode.values();
    }
}

