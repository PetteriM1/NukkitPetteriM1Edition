/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

final class b
extends ThreadLocal<char[]> {
    b() {
    }

    @Override
    protected char[] initialValue() {
        return new char[4096];
    }
}

