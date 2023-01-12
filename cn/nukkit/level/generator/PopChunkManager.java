/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.SimpleChunkManager;
import java.util.Arrays;

public class PopChunkManager
extends SimpleChunkManager {
    private boolean d = true;
    private final BaseFullChunk[] c = new BaseFullChunk[9];
    private int a = Integer.MAX_VALUE;
    private int b = Integer.MAX_VALUE;

    public PopChunkManager(long l) {
        super(l);
    }

    @Override
    public void cleanChunks(long l) {
        block0: {
            super.cleanChunks(l);
            if (this.d) break block0;
            Arrays.fill(this.c, null);
            this.a = Integer.MAX_VALUE;
            this.b = Integer.MAX_VALUE;
            this.d = true;
        }
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        int n3;
        switch (n - this.a) {
            case 0: {
                n3 = 0;
                break;
            }
            case 1: {
                n3 = 1;
                break;
            }
            case 2: {
                n3 = 2;
                break;
            }
            default: {
                return null;
            }
        }
        switch (n2 - this.b) {
            case 0: {
                break;
            }
            case 1: {
                n3 += 3;
                break;
            }
            case 2: {
                n3 += 6;
                break;
            }
            default: {
                return null;
            }
        }
        return this.c[n3];
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        if (this.a == Integer.MAX_VALUE) {
            this.a = n;
            this.b = n2;
        }
        switch (n - this.a) {
            case 0: {
                int n3 = 0;
                break;
            }
            case 1: {
                int n3 = 1;
                break;
            }
            case 2: {
                int n3 = 2;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Chunk is outside population area");
            }
        }
        switch (n2 - this.b) {
            case 0: {
                break;
            }
            case 1: {
                n3 += 3;
                break;
            }
            case 2: {
                n3 += 6;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Chunk is outside population area");
            }
        }
        this.d = false;
        this.c[n3] = baseFullChunk;
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

