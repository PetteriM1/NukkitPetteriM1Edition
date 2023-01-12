/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.mushroom;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class BigMushroom
extends BasicGenerator {
    public static final int NORTH_WEST = 1;
    public static final int NORTH = 2;
    public static final int NORTH_EAST = 3;
    public static final int WEST = 4;
    public static final int CENTER = 5;
    public static final int EAST = 6;
    public static final int SOUTH_WEST = 7;
    public static final int SOUTH = 8;
    public static final int SOUTH_EAST = 9;
    public static final int STEM = 10;
    public static final int ALL_INSIDE = 0;
    public static final int ALL_OUTSIDE = 14;
    public static final int ALL_STEM = 15;
    public static final int BROWN = 0;
    public static final int RED = 1;
    private final int a;

    public BigMushroom(int n) {
        this.a = n;
    }

    public BigMushroom() {
        this.a = -1;
    }

    @Override
    public boolean generate(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3) {
        int n = this.a;
        if (n < 0) {
            n = nukkitRandom.nextBoolean() ? 1 : 0;
        }
        Block block = n == 0 ? Block.get(99) : Block.get(100);
        int n2 = nukkitRandom.nextBoundedInt(3) + 4;
        if (nukkitRandom.nextBoundedInt(12) == 0) {
            n2 <<= 1;
        }
        boolean bl = true;
        if (vector3.getY() >= 1.0 && vector3.getY() + (double)n2 + 1.0 < 256.0) {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7 = vector3.getFloorY();
            while ((double)n7 <= vector3.getY() + 1.0 + (double)n2) {
                n6 = 3;
                if ((double)n7 <= vector3.getY() + 3.0) {
                    n6 = 0;
                }
                Vector3 vector32 = new Vector3();
                n5 = vector3.getFloorX() - n6;
                while ((double)n5 <= vector3.getX() + (double)n6 && bl) {
                    n4 = vector3.getFloorZ() - n6;
                    while ((double)n4 <= vector3.getZ() + (double)n6 && bl) {
                        if (n7 >= 0 && n7 < 256) {
                            vector32.setComponents(n5, n7, n4);
                            n3 = chunkManager.getBlockIdAt(vector32.getFloorX(), vector32.getFloorY(), vector32.getFloorZ());
                            if (n3 != 0 && n3 != 18) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n4;
                    }
                    ++n5;
                }
                ++n7;
            }
            if (!bl) {
                return false;
            }
            Vector3 vector33 = vector3.down();
            n6 = chunkManager.getBlockIdAt(vector33.getFloorX(), vector33.getFloorY(), vector33.getFloorZ());
            if (n6 != 3 && n6 != 2 && n6 != 110) {
                return false;
            }
            int n8 = vector3.getFloorY() + n2;
            if (n == 1) {
                n8 = vector3.getFloorY() + n2 - 3;
            }
            n5 = n8;
            while ((double)n5 <= vector3.getY() + (double)n2) {
                n4 = 1;
                if ((double)n5 < vector3.getY() + (double)n2) {
                    ++n4;
                }
                if (n == 0) {
                    n4 = 3;
                }
                n3 = vector3.getFloorX() - n4;
                int n9 = vector3.getFloorX() + n4;
                int n10 = vector3.getFloorZ() - n4;
                int n11 = vector3.getFloorZ() + n4;
                for (int k = n3; k <= n9; ++k) {
                    for (int i2 = n10; i2 <= n11; ++i2) {
                        Vector3 vector34;
                        int n12 = 5;
                        if (k == n3) {
                            --n12;
                        } else if (k == n9) {
                            ++n12;
                        }
                        if (i2 == n10) {
                            n12 -= 3;
                        } else if (i2 == n11) {
                            n12 += 3;
                        }
                        int n13 = n12;
                        if (n == 0 || (double)n5 < vector3.getY() + (double)n2) {
                            if ((k == n3 || k == n9) && (i2 == n10 || i2 == n11)) continue;
                            if ((double)k == vector3.getX() - (double)(n4 - 1) && i2 == n10) {
                                n13 = 1;
                            }
                            if (k == n3 && (double)i2 == vector3.getZ() - (double)(n4 - 1)) {
                                n13 = 1;
                            }
                            if ((double)k == vector3.getX() + (double)(n4 - 1) && i2 == n10) {
                                n13 = 3;
                            }
                            if (k == n9 && (double)i2 == vector3.getZ() - (double)(n4 - 1)) {
                                n13 = 3;
                            }
                            if ((double)k == vector3.getX() - (double)(n4 - 1) && i2 == n11) {
                                n13 = 7;
                            }
                            if (k == n3 && (double)i2 == vector3.getZ() + (double)(n4 - 1)) {
                                n13 = 7;
                            }
                            if ((double)k == vector3.getX() + (double)(n4 - 1) && i2 == n11) {
                                n13 = 9;
                            }
                            if (k == n9 && (double)i2 == vector3.getZ() + (double)(n4 - 1)) {
                                n13 = 9;
                            }
                        }
                        if (n13 == 5 && (double)n5 < vector3.getY() + (double)n2) {
                            n13 = 0;
                        }
                        if (!(vector3.getY() >= vector3.getY() + (double)n2 - 1.0) && n13 == 0 || Block.solid[chunkManager.getBlockIdAt((vector34 = new Vector3(k, n5, i2)).getFloorX(), vector34.getFloorY(), vector34.getFloorZ())]) continue;
                        block.setDamage(n13);
                        this.setBlockAndNotifyAdequately(chunkManager, vector34, block);
                    }
                }
                ++n5;
            }
            for (n5 = 0; n5 < n2; ++n5) {
                Vector3 vector35 = vector3.up(n5);
                n3 = chunkManager.getBlockIdAt(vector35.getFloorX(), vector35.getFloorY(), vector35.getFloorZ());
                if (Block.solid[n3]) continue;
                block.setDamage(10);
                this.setBlockAndNotifyAdequately(chunkManager, vector35, block);
            }
            return true;
        }
        return false;
    }
}

