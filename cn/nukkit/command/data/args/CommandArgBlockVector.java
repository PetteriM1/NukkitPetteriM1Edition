/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data.args;

public class CommandArgBlockVector {
    private int a;
    private int f;
    private int c;
    private boolean b;
    private boolean e;
    private boolean d;

    public int getX() {
        return this.a;
    }

    public int getY() {
        return this.f;
    }

    public int getZ() {
        return this.c;
    }

    public boolean isXrelative() {
        return this.b;
    }

    public boolean isYrelative() {
        return this.e;
    }

    public boolean isZrelative() {
        return this.d;
    }
}

