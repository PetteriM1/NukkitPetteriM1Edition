/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class SimpleChunkManager
extends cn.nukkit.level.generator.SimpleChunkManager {
    protected Long2ObjectOpenHashMap<FullChunk> chunks = new Long2ObjectOpenHashMap();

    public SimpleChunkManager(long l) {
        super(l);
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        long l = Level.chunkHash(n, n2);
        return this.chunks.containsKey(l) ? (BaseFullChunk)this.chunks.get(l) : null;
    }

    @Override
    public void setChunk(int n, int n2) {
        this.setChunk(n, n2, null);
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        if (baseFullChunk == null) {
            this.chunks.remove(Level.chunkHash(n, n2));
            return;
        }
        this.chunks.put(Level.chunkHash(n, n2), (FullChunk)baseFullChunk);
    }

    public void cleanChunks() {
        this.chunks.clear();
    }
}

