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

public class ObjectSavannaTree
extends TreeGenerator {
    private static final Block a = Block.get(162, 0);
    private static final Block b = Block.get(161, 0);

    @Override
    public boolean generate(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3) {
        int n = nukkitRandom.nextBoundedInt(3) + nukkitRandom.nextBoundedInt(3) + 5;
        boolean bl = true;
        if (vector3.getY() >= 1.0 && vector3.getY() + (double)n + 1.0 <= 256.0) {
            int n2;
            int n3;
            Object object;
            int n4;
            int n5 = (int)vector3.getY();
            while ((double)n5 <= vector3.getY() + 1.0 + (double)n) {
                n4 = 1;
                if ((double)n5 == vector3.getY()) {
                    n4 = 0;
                }
                if ((double)n5 >= vector3.getY() + 1.0 + (double)n - 2.0) {
                    n4 = 2;
                }
                object = new Vector3();
                n3 = (int)vector3.getX() - n4;
                while ((double)n3 <= vector3.getX() + (double)n4 && bl) {
                    n2 = (int)vector3.getZ() - n4;
                    while ((double)n2 <= vector3.getZ() + (double)n4 && bl) {
                        if (n5 >= 0 && n5 < 256) {
                            ((Vector3)object).setComponents(n3, n5, n2);
                            if (!this.canGrowInto(chunkManager.getBlockIdAt((int)((Vector3)object).x, (int)((Vector3)object).y, (int)((Vector3)object).z))) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n2;
                    }
                    ++n3;
                }
                ++n5;
            }
            if (!bl) {
                return false;
            }
            Vector3 vector32 = vector3.down();
            n4 = chunkManager.getBlockIdAt(vector32.getFloorX(), vector32.getFloorY(), vector32.getFloorZ());
            if ((n4 == 2 || n4 == 3) && vector3.getY() < (double)(256 - n - 1)) {
                int n6;
                int n7;
                this.setDirtAt(chunkManager, vector3.down());
                object = BlockFace.Plane.HORIZONTAL.random(nukkitRandom);
                n3 = n - nukkitRandom.nextBoundedInt(4) - 1;
                n2 = 3 - nukkitRandom.nextBoundedInt(3);
                int n8 = vector3.getFloorX();
                int n9 = vector3.getFloorZ();
                int n10 = 0;
                for (int k = 0; k < n; ++k) {
                    Vector3 vector33;
                    n7 = vector3.getFloorY() + k;
                    if (k >= n3 && n2 > 0) {
                        n8 += ((BlockFace)((Object)object)).getXOffset();
                        n9 += ((BlockFace)((Object)object)).getZOffset();
                        --n2;
                    }
                    if ((n6 = chunkManager.getBlockIdAt((vector33 = new Vector3(n8, n7, n9)).getFloorX(), vector33.getFloorY(), vector33.getFloorZ())) != 0 && n6 != 18) continue;
                    this.a(chunkManager, vector33);
                    n10 = n7;
                }
                Vector3 vector34 = new Vector3(n8, n10, n9);
                for (n7 = -3; n7 <= 3; ++n7) {
                    for (int k = -3; k <= 3; ++k) {
                        if (Math.abs(n7) == 3 && Math.abs(k) == 3) continue;
                        this.b(chunkManager, vector34.add(n7, 0.0, k));
                    }
                }
                vector34 = vector34.up();
                for (n7 = -1; n7 <= 1; ++n7) {
                    for (int k = -1; k <= 1; ++k) {
                        this.b(chunkManager, vector34.add(n7, 0.0, k));
                    }
                }
                this.b(chunkManager, vector34.east(2));
                this.b(chunkManager, vector34.west(2));
                this.b(chunkManager, vector34.south(2));
                this.b(chunkManager, vector34.north(2));
                n8 = vector3.getFloorX();
                n9 = vector3.getFloorZ();
                BlockFace blockFace = BlockFace.Plane.HORIZONTAL.random(nukkitRandom);
                if (blockFace != object) {
                    int n11;
                    int n12 = n3 - nukkitRandom.nextBoundedInt(2) - 1;
                    n6 = 1 + nukkitRandom.nextBoundedInt(3);
                    n10 = 0;
                    for (int k = n12; k < n && n6 > 0; ++k, --n6) {
                        if (k < 1) continue;
                        n11 = vector3.getFloorY() + k;
                        Vector3 vector35 = new Vector3(n8 += blockFace.getXOffset(), n11, n9 += blockFace.getZOffset());
                        int n13 = chunkManager.getBlockIdAt(vector35.getFloorX(), vector35.getFloorY(), vector35.getFloorZ());
                        if (n13 != 0 && n13 != 18) continue;
                        this.a(chunkManager, vector35);
                        n10 = n11;
                    }
                    if (n10 > 0) {
                        Vector3 vector36 = new Vector3(n8, n10, n9);
                        for (n11 = -2; n11 <= 2; ++n11) {
                            for (int k = -2; k <= 2; ++k) {
                                if (Math.abs(n11) == 2 && Math.abs(k) == 2) continue;
                                this.b(chunkManager, vector36.add(n11, 0.0, k));
                            }
                        }
                        vector36 = vector36.up();
                        for (n11 = -1; n11 <= 1; ++n11) {
                            for (int k = -1; k <= 1; ++k) {
                                this.b(chunkManager, vector36.add(n11, 0.0, k));
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void a(ChunkManager chunkManager, Vector3 vector3) {
        this.setBlockAndNotifyAdequately(chunkManager, vector3, a);
    }

    private void b(ChunkManager chunkManager, Vector3 vector3) {
        int n = chunkManager.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
        if (n == 0 || n == 18) {
            this.setBlockAndNotifyAdequately(chunkManager, vector3, b);
        }
    }
}

