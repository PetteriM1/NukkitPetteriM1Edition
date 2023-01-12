/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

final class a
extends ThreadLocal<char[]> {
    a() {
    }

    @Override
    protected char[] initialValue() {
        return new char[4096];
    }
}

