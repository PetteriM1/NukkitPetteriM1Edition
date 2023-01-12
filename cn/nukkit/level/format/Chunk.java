/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format;

import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.FullChunk;

public interface Chunk
extends FullChunk {
    public static final byte SECTION_COUNT = 16;

    public boolean isSectionEmpty(float var1);

    public ChunkSection getSection(float var1);

    public boolean setSection(float var1, ChunkSection var2);

    public ChunkSection[] getSections();

    public static class Entry {
        public final int chunkX;
        public final int chunkZ;

        public Entry(int n, int n2) {
            this.chunkX = n;
            this.chunkZ = n2;
        }
    }
}

