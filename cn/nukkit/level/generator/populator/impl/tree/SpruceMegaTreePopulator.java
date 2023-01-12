/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.tree.ObjectBigSpruceTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class SpruceMegaTreePopulator
extends Populator {
    private ChunkManager c;
    private int b;
    private int a;

    public void setRandomAmount(int n) {
        this.b = n;
    }

    public void setBaseAmount(int n) {
        this.a = n;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        this.c = chunkManager;
        int n3 = nukkitRandom.nextBoundedInt(this.b + 1) + this.a;
        Vector3 vector3 = new Vector3();
        for (int k = 0; k < n3; ++k) {
            int n4;
            int n5 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
            int n6 = this.a(n5, n4 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15));
            if (n6 == -1) continue;
            vector3.x = n5;
            vector3.y = n6;
            vector3.z = n4;
            new ObjectBigSpruceTree(0.25f, 5).placeObject(this.c, (int)vector3.x, (int)vector3.y, (int)vector3.z, nukkitRandom);
        }
    }

    private int a(int n, int n2) {
        int n3;
        int n4;
        for (n4 = 255; n4 > 0 && (n3 = this.c.getBlockIdAt(n, n4, n2)) != 3 && n3 != 2; --n4) {
            if (n3 == 0 || n3 == 78) continue;
            return -1;
        }
        return ++n4;
    }
}

