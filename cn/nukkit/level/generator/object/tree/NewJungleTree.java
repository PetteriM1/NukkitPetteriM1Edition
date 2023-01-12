/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.TreeGenerator;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class NewJungleTree
extends TreeGenerator {
    private final int b;
    private final int c;
    private final Block a = Block.get(17, 3);
    private final Block d = Block.get(18, 3);

    public NewJungleTree(int n) {
        this(n, 3);
    }

    public NewJungleTree(int n, int n2) {
        this.b = n;
        this.c = n2;
    }

    @Override
    public boolean generate(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3) {
        BlockVector3 blockVector3 = new BlockVector3(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
        int n = nukkitRandom.nextBoundedInt(this.c) + this.b;
        boolean bl = true;
        if (blockVector3.getY() >= 1 && blockVector3.getY() + n + 1 <= 256) {
            int n2;
            int n3;
            int n4;
            for (int k = blockVector3.getY(); k <= blockVector3.getY() + 1 + n; ++k) {
                n4 = 1;
                if (k == blockVector3.getY()) {
                    n4 = 0;
                }
                if (k >= blockVector3.getY() + 1 + n - 2) {
                    n4 = 2;
                }
                BlockVector3 blockVector32 = new BlockVector3();
                for (n3 = blockVector3.getX() - n4; n3 <= blockVector3.getX() + n4 && bl; ++n3) {
                    for (n2 = blockVector3.getZ() - n4; n2 <= blockVector3.getZ() + n4 && bl; ++n2) {
                        if (k >= 0 && k < 256) {
                            blockVector32.setComponents(n3, k, n2);
                            if (this.canGrowInto(chunkManager.getBlockIdAt(blockVector32.x, blockVector32.y, blockVector32.z))) continue;
                            bl = false;
                            continue;
                        }
                        bl = false;
                    }
                }
            }
            if (!bl) {
                return false;
            }
            BlockVector3 blockVector33 = blockVector3.down();
            n4 = chunkManager.getBlockIdAt(blockVector33.x, blockVector33.y, blockVector33.z);
            if ((n4 == 2 || n4 == 3 || n4 == 60) && blockVector3.getY() < 256 - n - 1) {
                BlockVector3 blockVector34;
                int n5;
                int n6;
                int n7;
                this.setDirtAt(chunkManager, blockVector33);
                for (n7 = blockVector3.getY() - 3 + n; n7 <= blockVector3.getY() + n; ++n7) {
                    n3 = n7 - (blockVector3.getY() + n);
                    n2 = 1 - (n3 >> 1);
                    for (int k = blockVector3.getX() - n2; k <= blockVector3.getX() + n2; ++k) {
                        n6 = k - blockVector3.getX();
                        for (n5 = blockVector3.getZ() - n2; n5 <= blockVector3.getZ() + n2; ++n5) {
                            int n8 = n5 - blockVector3.getZ();
                            if (Math.abs(n6) == n2 && Math.abs(n8) == n2 && (nukkitRandom.nextBoundedInt(2) == 0 || n3 == 0)) continue;
                            blockVector34 = new BlockVector3(k, n7, n5);
                            int n9 = chunkManager.getBlockIdAt(blockVector34.x, blockVector34.y, blockVector34.z);
                            if (n9 != 0 && n9 != 18 && n9 != 106) continue;
                            this.setBlockAndNotifyAdequately(chunkManager, blockVector34, this.d);
                        }
                    }
                }
                for (n7 = 0; n7 < n; ++n7) {
                    BlockVector3 blockVector35 = blockVector3.up(n7);
                    n2 = chunkManager.getBlockIdAt(blockVector35.x, blockVector35.y, blockVector35.z);
                    if (n2 != 0 && n2 != 18 && n2 != 106) continue;
                    this.setBlockAndNotifyAdequately(chunkManager, blockVector35, this.a);
                    if (n7 <= 0) continue;
                    if (nukkitRandom.nextBoundedInt(3) > 0 && NewJungleTree.a(chunkManager, blockVector3.add(-1, n7, 0))) {
                        this.b(chunkManager, blockVector3.add(-1, n7, 0), 8);
                    }
                    if (nukkitRandom.nextBoundedInt(3) > 0 && NewJungleTree.a(chunkManager, blockVector3.add(1, n7, 0))) {
                        this.b(chunkManager, blockVector3.add(1, n7, 0), 2);
                    }
                    if (nukkitRandom.nextBoundedInt(3) > 0 && NewJungleTree.a(chunkManager, blockVector3.add(0, n7, -1))) {
                        this.b(chunkManager, blockVector3.add(0, n7, -1), 1);
                    }
                    if (nukkitRandom.nextBoundedInt(3) <= 0 || !NewJungleTree.a(chunkManager, blockVector3.add(0, n7, 1))) continue;
                    this.b(chunkManager, blockVector3.add(0, n7, 1), 4);
                }
                for (n7 = blockVector3.getY() - 3 + n; n7 <= blockVector3.getY() + n; ++n7) {
                    int n10 = n7 - (blockVector3.getY() + n);
                    n2 = 2 - (n10 >> 1);
                    BlockVector3 blockVector36 = new BlockVector3();
                    for (n6 = blockVector3.getX() - n2; n6 <= blockVector3.getX() + n2; ++n6) {
                        for (n5 = blockVector3.getZ() - n2; n5 <= blockVector3.getZ() + n2; ++n5) {
                            blockVector36.setComponents(n6, n7, n5);
                            if (chunkManager.getBlockIdAt(blockVector36.x, blockVector36.y, blockVector36.z) != 18) continue;
                            BlockVector3 blockVector37 = blockVector36.west();
                            blockVector34 = blockVector36.east();
                            BlockVector3 blockVector38 = blockVector36.north();
                            BlockVector3 blockVector39 = blockVector36.south();
                            if (nukkitRandom.nextBoundedInt(4) == 0 && chunkManager.getBlockIdAt(blockVector37.x, blockVector37.y, blockVector37.z) == 0) {
                                this.a(chunkManager, blockVector37, 8);
                            }
                            if (nukkitRandom.nextBoundedInt(4) == 0 && chunkManager.getBlockIdAt(blockVector34.x, blockVector34.y, blockVector34.z) == 0) {
                                this.a(chunkManager, blockVector34, 2);
                            }
                            if (nukkitRandom.nextBoundedInt(4) == 0 && chunkManager.getBlockIdAt(blockVector38.x, blockVector38.y, blockVector38.z) == 0) {
                                this.a(chunkManager, blockVector38, 1);
                            }
                            if (nukkitRandom.nextBoundedInt(4) != 0 || chunkManager.getBlockIdAt(blockVector39.x, blockVector39.y, blockVector39.z) != 0) continue;
                            this.a(chunkManager, blockVector39, 4);
                        }
                    }
                }
                if (nukkitRandom.nextBoundedInt(5) == 0 && n > 5) {
                    for (n7 = 0; n7 < 2; ++n7) {
                        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
                            if (nukkitRandom.nextBoundedInt(4 - n7) != 0) continue;
                            BlockFace blockFace2 = blockFace.getOpposite();
                            this.a(chunkManager, blockVector3.add(blockFace2.getXOffset(), n - 5 + n7, blockFace2.getZOffset()), blockFace);
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void a(ChunkManager chunkManager, BlockVector3 blockVector3, BlockFace blockFace) {
        this.setBlockAndNotifyAdequately(chunkManager, blockVector3, Block.get(127, NewJungleTree.a(blockFace.getIndex())));
    }

    private void b(ChunkManager chunkManager, BlockVector3 blockVector3, int n) {
        this.setBlockAndNotifyAdequately(chunkManager, blockVector3, Block.get(106, n));
    }

    private void a(ChunkManager chunkManager, BlockVector3 blockVector3, int n) {
        this.b(chunkManager, blockVector3, n);
        blockVector3 = blockVector3.down();
        for (int k = 4; k > 0 && chunkManager.getBlockIdAt(blockVector3.x, blockVector3.y, blockVector3.z) == 0; --k) {
            this.b(chunkManager, blockVector3, n);
            blockVector3 = blockVector3.down();
        }
    }

    private static boolean a(ChunkManager chunkManager, BlockVector3 blockVector3) {
        return chunkManager.getBlockIdAt(blockVector3.x, blockVector3.y, blockVector3.z) == 0;
    }

    private static int a(int n) {
        int n2 = 0;
        switch (n) {
            case 4: {
                ++n2;
                break;
            }
            case 2: {
                n2 += 2;
                break;
            }
            case 5: {
                n2 += 3;
            }
        }
        return n2;
    }
}

