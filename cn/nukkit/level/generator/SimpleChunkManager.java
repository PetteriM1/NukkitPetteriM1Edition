/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;

public abstract class SimpleChunkManager
implements ChunkManager {
    protected long seed;

    public SimpleChunkManager(long l) {
        this.seed = l;
    }

    @Override
    public int getBlockIdAt(int n, int n2, int n3) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            return baseFullChunk.getBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF);
        }
        return 0;
    }

    @Override
    public void setBlockIdAt(int n, int n2, int n3, int n4) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            baseFullChunk.setBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF, n4);
        }
    }

    @Override
    public void setBlockAt(int n, int n2, int n3, int n4, int n5) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            baseFullChunk.setBlock(n & 0xF, n2 & 0xFF, n3 & 0xF, n4, n5);
        }
    }

    @Override
    public void setBlockFullIdAt(int n, int n2, int n3, int n4) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            baseFullChunk.setFullBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF, n4);
        }
    }

    @Override
    public int getBlockDataAt(int n, int n2, int n3) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            return baseFullChunk.getBlockData(n & 0xF, n2 & 0xFF, n3 & 0xF);
        }
        return 0;
    }

    @Override
    public void setBlockDataAt(int n, int n2, int n3, int n4) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        if (baseFullChunk != null) {
            baseFullChunk.setBlockData(n & 0xF, n2 & 0xFF, n3 & 0xF, n4);
        }
    }

    @Override
    public void setChunk(int n, int n2) {
        this.setChunk(n, n2, null);
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long l) {
        this.seed = l;
    }

    public void cleanChunks(long l) {
        this.seed = l;
    }
}

