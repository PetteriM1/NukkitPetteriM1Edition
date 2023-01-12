/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.utils.BinaryStream;

public interface ChunkSection {
    public int getY();

    public int getBlockId(int var1, int var2, int var3);

    public void setBlockId(int var1, int var2, int var3, int var4);

    public int getBlockData(int var1, int var2, int var3);

    public void setBlockData(int var1, int var2, int var3, int var4);

    public int getFullBlock(int var1, int var2, int var3);

    public Block getAndSetBlock(int var1, int var2, int var3, Block var4);

    public boolean setFullBlockId(int var1, int var2, int var3, int var4);

    public boolean setBlock(int var1, int var2, int var3, int var4);

    public boolean setBlock(int var1, int var2, int var3, int var4, int var5);

    public int getBlockSkyLight(int var1, int var2, int var3);

    public void setBlockSkyLight(int var1, int var2, int var3, int var4);

    public int getBlockLight(int var1, int var2, int var3);

    public void setBlockLight(int var1, int var2, int var3, int var4);

    public byte[] getIdArray();

    public byte[] getIdArray(int var1);

    public byte[] getDataArray();

    public byte[] getSkyLightArray();

    public byte[] getLightArray();

    public boolean isEmpty();

    public byte[] getBytes(boolean var1);

    public void writeTo(int var1, BinaryStream var2, boolean var3);

    public ChunkSection copy();
}

