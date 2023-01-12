/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.type;

import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.FullChunk;

public abstract class CoveredBiome
extends Biome {
    public int getCoverId(int n, int n2) {
        return 0;
    }

    public int getSurfaceDepth(int n, int n2, int n3) {
        return 1;
    }

    public abstract int getSurfaceId(int var1, int var2, int var3);

    public int getGroundDepth(int n, int n2, int n3) {
        return 4;
    }

    public abstract int getGroundId(int var1, int var2, int var3);

    public void doCover(int n, int n2, FullChunk fullChunk) {
        int n3 = fullChunk.getX() << 4 | n;
        int n4 = fullChunk.getZ() << 4 | n2;
        int n5 = this.getCoverId(n3, n4);
        boolean bl = false;
        for (int k = 254; k > 32; --k) {
            if (fullChunk.getFullBlock(n, k, n2) == 16) {
                block8: {
                    if (!bl) {
                        int n6;
                        int n7;
                        int n8;
                        if (k >= 64) {
                            fullChunk.setFullBlockId(n, k + 1, n2, n5);
                            n8 = this.getSurfaceDepth(n3, k, n4);
                            for (n7 = 0; n7 < n8; ++n7) {
                                n6 = k - n7;
                                if (fullChunk.getFullBlock(n, n6, n2) == 16) {
                                    fullChunk.setFullBlockId(n, n6, n2, this.getSurfaceId(n3, n6, n4));
                                    continue;
                                }
                                break block8;
                            }
                            k -= n8;
                        }
                        n8 = this.getGroundDepth(n3, k, n4);
                        for (n7 = 0; n7 < n8; ++n7) {
                            n6 = k - n7;
                            if (fullChunk.getFullBlock(n, n6, n2) == 16) {
                                fullChunk.setFullBlockId(n, n6, n2, this.getGroundId(n3, n6, n4));
                                continue;
                            }
                            break block8;
                        }
                        k -= n8 - 1;
                    }
                }
                bl = true;
                continue;
            }
            if (!bl) continue;
            bl = false;
        }
    }
}

