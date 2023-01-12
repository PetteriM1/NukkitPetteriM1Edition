/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.util;

import cn.nukkit.level.util.BitArrayVersion;

public interface BitArray {
    public void set(int var1, int var2);

    public int get(int var1);

    public int size();

    public int[] getWords();

    public BitArrayVersion getVersion();

    public BitArray copy();
}

