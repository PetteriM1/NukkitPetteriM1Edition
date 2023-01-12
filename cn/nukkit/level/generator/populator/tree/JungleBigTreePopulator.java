/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.tree.ObjectJungleBigTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class JungleBigTreePopulator
extends Populator {
    private ChunkManager a;
    private int c;
    private int b;

    public JungleBigTreePopulator() {
        this(3);
    }

    public JungleBigTreePopulator(int n) {
    }

    public void setRandomAmount(int n) {
        this.c = n;
    }

    public void setBaseAmount(int n) {
        this.b = n;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        this.a = chunkManager;
        int n3 = nukkitRandom.nextBoundedInt(this.c + 1) + this.b;
        Vector3 vector3 = new Vector3();
        for (int k = 0; k < n3; ++k) {
            int n4;
            int n5 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
            int n6 = this.a(n5, n4 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15));
            if (n6 == -1) continue;
            new ObjectJungleBigTree(10, 20, Block.get(17, 3), Block.get(18, 3)).generate(this.a, nukkitRandom, vector3.setComponents(n5, n6, n4));
        }
    }

    private int a(int n, int n2) {
        int n3;
        int n4;
        for (n4 = 127; n4 > 0 && (n3 = this.a.getBlockIdAt(n, n4, n2)) != 3 && n3 != 2; --n4) {
            if (n3 == 0 || n3 == 78) continue;
            return -1;
        }
        return ++n4;
    }
}

