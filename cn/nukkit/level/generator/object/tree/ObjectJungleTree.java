/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.math.NukkitRandom;

public class ObjectJungleTree
extends ObjectTree {
    private int a = 8;

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getTreeHeight() {
        return this.a;
    }

    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.a = nukkitRandom.nextBoundedInt(6) + 4;
        super.placeObject(chunkManager, n, n2, n3, nukkitRandom);
    }
}

