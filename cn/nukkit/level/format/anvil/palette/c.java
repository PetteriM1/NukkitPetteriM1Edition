/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.palette;

final class c
extends ThreadLocal<boolean[]> {
    c() {
    }

    @Override
    protected boolean[] initialValue() {
        return new boolean[4096];
    }
}

