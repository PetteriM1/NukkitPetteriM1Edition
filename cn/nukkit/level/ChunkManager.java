/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.format.generic.BaseFullChunk;

public interface ChunkManager {
    public int getBlockIdAt(int var1, int var2, int var3);

    public void setBlockFullIdAt(int var1, int var2, int var3, int var4);

    public void setBlockIdAt(int var1, int var2, int var3, int var4);

    default public void setBlockAt(int n, int n2, int n3, int n4) {
        this.setBlockAt(n, n2, n3, n4, 0);
    }

    public void setBlockAt(int var1, int var2, int var3, int var4, int var5);

    public int getBlockDataAt(int var1, int var2, int var3);

    public void setBlockDataAt(int var1, int var2, int var3, int var4);

    public BaseFullChunk getChunk(int var1, int var2);

    public void setChunk(int var1, int var2);

    public void setChunk(int var1, int var2, BaseFullChunk var3);

    public long getSeed();
}

