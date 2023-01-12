/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorOre
extends Populator {
    private final int b;
    private final OreType[] a;

    public PopulatorOre(int n, OreType[] oreTypeArray) {
        this.b = n;
        this.a = oreTypeArray;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3 = n << 4;
        int n4 = n3 + 15;
        int n5 = n2 << 4;
        int n6 = n5 + 15;
        for (OreType oreType : this.a) {
            for (int k = 0; k < oreType.clusterCount; ++k) {
                int n7 = NukkitMath.randomRange(nukkitRandom, n3, n4);
                int n8 = NukkitMath.randomRange(nukkitRandom, n5, n6);
                int n9 = NukkitMath.randomRange(nukkitRandom, oreType.minHeight, oreType.maxHeight);
                if (chunkManager.getBlockIdAt(n7, n9, n8) != this.b) continue;
                if (oreType.clusterSize == 1) {
                    chunkManager.setBlockFullIdAt(n7, n9, n8, oreType.fullId);
                    continue;
                }
                oreType.spawn(chunkManager, nukkitRandom, this.b, n7, n9, n8);
            }
        }
    }
}

