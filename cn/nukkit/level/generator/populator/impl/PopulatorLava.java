/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public class PopulatorLava
extends Populator {
    private ChunkManager c;
    private int d;
    private int a;
    private NukkitRandom b;

    public void setRandomAmount(int n) {
        this.d = n;
    }

    public void setBaseAmount(int n) {
        this.a = n;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        this.b = nukkitRandom;
        if (nukkitRandom.nextRange(0, 100) < 5) {
            this.c = chunkManager;
            int n3 = nukkitRandom.nextRange(0, this.d + 1) + this.a;
            int n4 = n << 4;
            int n5 = n2 << 4;
            for (int k = 0; k < n3; ++k) {
                int n6;
                int n7 = nukkitRandom.nextRange(0, 15);
                int n8 = PopulatorLava.a(fullChunk, n7, n6 = nukkitRandom.nextRange(0, 15));
                if (n8 == -1 || fullChunk.getBlockId(n7, n8, n6) != 0) continue;
                fullChunk.setBlock(n7, n8, n6, 10);
                fullChunk.setBlockLight(n7, n8, n6, Block.light[10]);
                this.c(n4 + n7, n8, n5 + n6);
            }
        }
    }

    private int a(int n, int n2, int n3, int n4, int n5, int n6) {
        if (this.c.getBlockIdAt(n, n2, n3) != this.c.getBlockIdAt(n4, n5, n6)) {
            return -1;
        }
        return this.c.getBlockDataAt(n4, n5, n6);
    }

    private void c(int n, int n2, int n3) {
        int n4;
        if (this.c.getChunk(n >> 4, n3 >> 4) == null) {
            return;
        }
        int n5 = this.a(n, n2, n3, n, n2, n3);
        int n6 = 2;
        if (n5 > 0) {
            int n7;
            int n8 = -100;
            n8 = this.a(n, n2, n3, n, n2, n3 - 1, n8);
            n8 = this.a(n, n2, n3, n, n2, n3 + 1, n8);
            n8 = this.a(n, n2, n3, n - 1, n2, n3, n8);
            n4 = (n8 = this.a(n, n2, n3, n + 1, n2, n3, n8)) + n6;
            if (n4 >= 8 || n8 < 0) {
                n4 = -1;
            }
            if ((n7 = this.a(n, n2, n3, n, n2 + 1, n3)) >= 0) {
                n4 = n7 >= 8 ? n7 : n7 | 8;
            }
            if (n5 < 8 && n4 < 8 && n4 > 1 && this.b.nextRange(0, 4) != 0) {
                n4 = n5;
            }
            if (n4 != n5) {
                n5 = n4;
                if (n5 < 0) {
                    this.c.setBlockAt(n, n2, n3, 0);
                } else {
                    this.c.setBlockAt(n, n2, n3, 10, n5);
                    this.c(n, n2, n3);
                    return;
                }
            }
        }
        if (this.a(n, n2 - 1, n3)) {
            if (n5 >= 8) {
                this.a(n, n2 - 1, n3, n5);
            } else {
                this.a(n, n2 - 1, n3, n5 | 8);
            }
        } else if (!(n5 < 0 || n5 != 0 && this.a(n, n2 - 1, n3))) {
            boolean[] blArray = this.b(n, n2, n3);
            n4 = n5 + n6;
            if (n5 >= 8) {
                n4 = 1;
            }
            if (n4 >= 8) {
                return;
            }
            if (blArray[0]) {
                this.a(n - 1, n2, n3, n4);
            }
            if (blArray[1]) {
                this.a(n + 1, n2, n3, n4);
            }
            if (blArray[2]) {
                this.a(n, n2, n3 - 1, n4);
            }
            if (blArray[3]) {
                this.a(n, n2, n3 + 1, n4);
            }
        }
    }

    private void a(int n, int n2, int n3, int n4) {
        if (this.c.getBlockIdAt(n, n2, n3) == 0) {
            this.c.setBlockAt(n, n2, n3, 10, n4);
            this.c(n, n2, n3);
        }
    }

    private boolean a(int n, int n2, int n3) {
        int n4 = this.c.getBlockIdAt(n, n2, n3);
        return n4 == 0 || n4 == 10 || n4 == 11;
    }

    private int a(int n, int n2, int n3, int n4, int n5) {
        int n6 = 1000;
        for (int k = 0; k < 4; ++k) {
            int n7;
            if (!(k == 0 && n5 == 1 || k == 1 && n5 == 0 || k == 2 && n5 == 3) && (k != 3 || n5 != 2)) continue;
            int n8 = n;
            int n9 = n3;
            if (k == 0) {
                --n8;
            } else if (k == 1) {
                ++n8;
            } else if (k == 2) {
                --n9;
            } else if (k == 3) {
                ++n9;
            }
            if (!this.a(n8, n2, n9) || this.a(n8, n2, n9) && this.c.getBlockDataAt(n8, n2, n9) == 0) continue;
            if (this.a(n8, n2 - 1, n9)) {
                return n4;
            }
            if (n4 >= 4 || (n7 = this.a(n8, n2, n9, n4 + 1, k)) >= n6) continue;
            n6 = n7;
        }
        return n6;
    }

    private boolean[] b(int n, int n2, int n3) {
        int n4;
        int n5;
        int[] nArray = new int[]{0, 0, 0, 0};
        boolean[] blArray = new boolean[]{false, false, false, false};
        for (n5 = 0; n5 < 4; ++n5) {
            nArray[n5] = 1000;
            n4 = n;
            int n6 = n3;
            if (n5 == 0) {
                --n4;
            } else if (n5 == 1) {
                ++n4;
            } else if (n5 == 2) {
                --n6;
            } else if (n5 == 3) {
                ++n6;
            }
            nArray[n5] = this.a(n4, n2 - 1, n6) ? 0 : this.a(n4, n2, n6, 1, n5);
        }
        n5 = nArray[0];
        for (n4 = 1; n4 < 4; ++n4) {
            if (nArray[n4] >= n5) continue;
            n5 = nArray[n4];
        }
        for (n4 = 0; n4 < 4; ++n4) {
            blArray[n4] = nArray[n4] == n5;
        }
        return blArray;
    }

    private int a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = this.a(n, n2, n3, n4, n5, n6);
        if (n8 < 0) {
            return n7;
        }
        if (n8 >= 8) {
            n8 = 0;
        }
        return n7 >= 0 && n8 >= n7 ? n7 : n8;
    }

    private static int a(FullChunk fullChunk, int n, int n2) {
        int n3;
        int n4;
        for (n4 = 127; n4 >= 0 && (n3 = fullChunk.getBlockId(n, n4, n2)) != 0; --n4) {
        }
        return n4 == 0 ? -1 : n4;
    }
}

