/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.TreeGenerator;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class ObjectDarkOakTree
extends TreeGenerator {
    private static final Block b = Block.get(162, 1);
    private static final Block a = Block.get(161, 1);

    @Override
    public boolean generate(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3) {
        int n = nukkitRandom.nextBoundedInt(3) + nukkitRandom.nextBoundedInt(2) + 6;
        int n2 = vector3.getFloorX();
        int n3 = vector3.getFloorY();
        int n4 = vector3.getFloorZ();
        if (n3 >= 1 && n3 + n + 1 < 256) {
            int n5;
            int n6;
            int n7;
            Vector3 vector32 = vector3.down();
            int n8 = chunkManager.getBlockIdAt(vector32.getFloorX(), vector32.getFloorY(), vector32.getFloorZ());
            if (n8 != 2 && n8 != 3) {
                return false;
            }
            if (!this.a(chunkManager, vector3, n)) {
                return false;
            }
            this.setDirtAt(chunkManager, vector32);
            this.setDirtAt(chunkManager, vector32.east());
            this.setDirtAt(chunkManager, vector32.south());
            this.setDirtAt(chunkManager, vector32.south().east());
            BlockFace blockFace = BlockFace.Plane.HORIZONTAL.random(nukkitRandom);
            int n9 = n - nukkitRandom.nextBoundedInt(4);
            int n10 = 2 - nukkitRandom.nextBoundedInt(3);
            int n11 = n2;
            int n12 = n4;
            int n13 = n3 + n - 1;
            for (n7 = 0; n7 < n; ++n7) {
                Vector3 vector33;
                if (n7 >= n9 && n10 > 0) {
                    n11 += blockFace.getXOffset();
                    n12 += blockFace.getZOffset();
                    --n10;
                }
                if ((n6 = chunkManager.getBlockIdAt((vector33 = new Vector3(n11, n5 = n3 + n7, n12)).getFloorX(), vector33.getFloorY(), vector33.getFloorZ())) != 0 && n6 != 18) continue;
                this.a(chunkManager, vector33);
                this.a(chunkManager, vector33.east());
                this.a(chunkManager, vector33.south());
                this.a(chunkManager, vector33.east().south());
            }
            for (n7 = -2; n7 <= 0; ++n7) {
                for (n5 = -2; n5 <= 0; ++n5) {
                    int n14 = -1;
                    this.a(chunkManager, n11 + n7, n13 + n14, n12 + n5);
                    this.a(chunkManager, 1 + n11 - n7, n13 + n14, n12 + n5);
                    this.a(chunkManager, n11 + n7, n13 + n14, 1 + n12 - n5);
                    this.a(chunkManager, 1 + n11 - n7, n13 + n14, 1 + n12 - n5);
                    if (n7 <= -2 && n5 <= -1 || n7 == -1 && n5 == -2) continue;
                    n14 = 1;
                    this.a(chunkManager, n11 + n7, n13 + n14, n12 + n5);
                    this.a(chunkManager, 1 + n11 - n7, n13 + n14, n12 + n5);
                    this.a(chunkManager, n11 + n7, n13 + n14, 1 + n12 - n5);
                    this.a(chunkManager, 1 + n11 - n7, n13 + n14, 1 + n12 - n5);
                }
            }
            if (nukkitRandom.nextBoolean()) {
                this.a(chunkManager, n11, n13 + 2, n12);
                this.a(chunkManager, n11 + 1, n13 + 2, n12);
                this.a(chunkManager, n11 + 1, n13 + 2, n12 + 1);
                this.a(chunkManager, n11, n13 + 2, n12 + 1);
            }
            for (n7 = -3; n7 <= 4; ++n7) {
                for (n5 = -3; n5 <= 4; ++n5) {
                    if (n7 == -3 && n5 == -3 || n7 == -3 && n5 == 4 || n7 == 4 && n5 == -3 || n7 == 4 && n5 == 4 || Math.abs(n7) >= 3 && Math.abs(n5) >= 3) continue;
                    this.a(chunkManager, n11 + n7, n13, n12 + n5);
                }
            }
            for (n7 = -1; n7 <= 2; ++n7) {
                for (n5 = -1; n5 <= 2; ++n5) {
                    int n15;
                    if (n7 >= 0 && n7 <= 1 && n5 >= 0 && n5 <= 1 || nukkitRandom.nextBoundedInt(3) > 0) continue;
                    int n16 = nukkitRandom.nextBoundedInt(3) + 2;
                    for (n6 = 0; n6 < n16; ++n6) {
                        this.a(chunkManager, new Vector3(n2 + n7, n13 - n6 - 1, n4 + n5));
                    }
                    for (n6 = -1; n6 <= 1; ++n6) {
                        for (n15 = -1; n15 <= 1; ++n15) {
                            this.a(chunkManager, n11 + n7 + n6, n13, n12 + n5 + n15);
                        }
                    }
                    for (n6 = -2; n6 <= 2; ++n6) {
                        for (n15 = -2; n15 <= 2; ++n15) {
                            if (Math.abs(n6) == 2 && Math.abs(n15) == 2) continue;
                            this.a(chunkManager, n11 + n7 + n6, n13 - 1, n12 + n5 + n15);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean a(ChunkManager chunkManager, Vector3 vector3, int n) {
        int n2 = vector3.getFloorX();
        int n3 = vector3.getFloorY();
        int n4 = vector3.getFloorZ();
        Vector3 vector32 = new Vector3();
        for (int k = 0; k <= n + 1; ++k) {
            int n5 = 1;
            if (k == 0) {
                n5 = 0;
            }
            if (k >= n - 1) {
                n5 = 2;
            }
            for (int i2 = -n5; i2 <= n5; ++i2) {
                for (int i3 = -n5; i3 <= n5; ++i3) {
                    vector32.setComponents(n2 + i2, n3 + k, n4 + i3);
                    if (this.canGrowInto(chunkManager.getBlockIdAt(vector32.getFloorX(), vector32.getFloorY(), vector32.getFloorZ()))) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private void a(ChunkManager chunkManager, Vector3 vector3) {
        if (this.canGrowInto(chunkManager.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ()))) {
            this.setBlockAndNotifyAdequately(chunkManager, vector3, b);
        }
    }

    private void a(ChunkManager chunkManager, int n, int n2, int n3) {
        Vector3 vector3 = new Vector3(n, n2, n3);
        int n4 = chunkManager.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
        if (n4 == 0) {
            this.setBlockAndNotifyAdequately(chunkManager, vector3, a);
        }
    }
}

