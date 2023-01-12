/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.SimpleChunkManager;

public class SingleChunkManager
extends SimpleChunkManager {
    private int b = Integer.MAX_VALUE;
    private int a = Integer.MAX_VALUE;
    private BaseFullChunk c;

    public SingleChunkManager(long l) {
        super(l);
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        if (n == this.b && n2 == this.a) {
            return this.c;
        }
        return null;
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        if (baseFullChunk == null) {
            this.c = null;
            this.b = Integer.MAX_VALUE;
            this.a = Integer.MAX_VALUE;
        } else {
            if (this.c != null) {
                throw new UnsupportedOperationException("Replacing chunks is not allowed behavior");
            }
            this.c = baseFullChunk;
            this.b = baseFullChunk.getX();
            this.a = baseFullChunk.getZ();
        }
    }

    @Override
    public void cleanChunks(long l) {
        super.cleanChunks(l);
        this.c = null;
        this.b = Integer.MAX_VALUE;
        this.a = Integer.MAX_VALUE;
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

