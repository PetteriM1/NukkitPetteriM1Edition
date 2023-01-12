/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event;

public enum EventPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4),
    MONITOR(5);

    private final int b;

    private EventPriority(int n2) {
        this.b = n2;
    }

    public int getSlot() {
        return this.b;
    }
}

