/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectBirchTree;
import cn.nukkit.level.generator.object.tree.ObjectJungleTree;
import cn.nukkit.level.generator.object.tree.ObjectOakTree;
import cn.nukkit.level.generator.object.tree.ObjectSpruceTree;
import cn.nukkit.level.generator.object.tree.ObjectTallBirchTree;
import cn.nukkit.math.NukkitRandom;

public abstract class ObjectTree {
    protected boolean overridable(int n) {
        switch (n) {
            case 0: 
            case 6: 
            case 17: 
            case 18: 
            case 78: 
            case 161: 
            case 162: {
                return true;
            }
        }
        return false;
    }

    public int getType() {
        return 0;
    }

    public int getTrunkBlock() {
        return 17;
    }

    public int getLeafBlock() {
        return 18;
    }

    public int getTreeHeight() {
        return 7;
    }

    public static void growTree(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        ObjectTree.growTree(chunkManager, n, n2, n3, nukkitRandom, 0);
    }

    public static void growTree(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom, int n4) {
        ObjectTree objectTree;
        switch (n4) {
            case 1: {
                objectTree = new ObjectSpruceTree();
                break;
            }
            case 2: {
                objectTree = new ObjectBirchTree();
                break;
            }
            case 3: {
                objectTree = new ObjectJungleTree();
                break;
            }
            case 10: {
                objectTree = new ObjectTallBirchTree();
                break;
            }
            default: {
                objectTree = new ObjectOakTree();
            }
        }
        if (objectTree.canPlaceObject(chunkManager, n, n2, n3, nukkitRandom)) {
            objectTree.placeObject(chunkManager, n, n2, n3, nukkitRandom);
        }
    }

    public boolean canPlaceObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        int n4 = 0;
        for (int k = 0; k < this.getTreeHeight() + 3; ++k) {
            if (k == 1 || k == this.getTreeHeight()) {
                ++n4;
            }
            for (int i2 = -n4; i2 < n4 + 1; ++i2) {
                for (int i3 = -n4; i3 < n4 + 1; ++i3) {
                    if (this.overridable(chunkManager.getBlockIdAt(n + i2, n2 + k, n3 + i3))) continue;
                    return false;
                }
            }
        }
        return true;
    }

    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.placeTrunk(chunkManager, n, n2, n3, nukkitRandom, this.getTreeHeight() - 1);
        for (int k = n2 - 3 + this.getTreeHeight(); k <= n2 + this.getTreeHeight(); ++k) {
            double d2 = k - (n2 + this.getTreeHeight());
            int n4 = (int)(1.0 - d2 / 2.0);
            for (int i2 = n - n4; i2 <= n + n4; ++i2) {
                int n5 = Math.abs(i2 - n);
                for (int i3 = n3 - n4; i3 <= n3 + n4; ++i3) {
                    int n6 = Math.abs(i3 - n3);
                    if (n5 == n4 && n6 == n4 && (d2 == 0.0 || nukkitRandom.nextBoundedInt(2) == 0) || Block.solid[chunkManager.getBlockIdAt(i2, k, i3)]) continue;
                    chunkManager.setBlockAt(i2, k, i3, this.getLeafBlock(), this.getType());
                }
            }
        }
    }

    protected void placeTrunk(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom, int n4) {
        chunkManager.setBlockAt(n, n2 - 1, n3, 3);
        for (int k = 0; k < n4; ++k) {
            int n5 = chunkManager.getBlockIdAt(n, n2 + k, n3);
            if (!this.overridable(n5)) continue;
            chunkManager.setBlockAt(n, n2 + k, n3, this.getTrunkBlock(), this.getType());
        }
    }
}

