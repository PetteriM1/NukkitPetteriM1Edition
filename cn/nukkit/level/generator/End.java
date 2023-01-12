/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.noise.Noise;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.Collections;
import java.util.Map;

public class End
extends Generator {
    private ChunkManager c;
    private NukkitRandom e;
    private Simplex d;

    public End() {
    }

    public End(Map<String, Object> map) {
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.c;
    }

    @Override
    public Map<String, Object> getSettings() {
        return Collections.emptyMap();
    }

    @Override
    public String getName() {
        return "the_end";
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.c = chunkManager;
        this.e = nukkitRandom;
        this.d = new Simplex(this.e, 4.0, 0.25, 0.015625);
    }

    @Override
    public void generateChunk(int n, int n2) {
        this.e.setSeed((long)(0xA6FE78DC ^ n << 8 ^ n2) ^ this.c.getSeed());
        BaseFullChunk baseFullChunk = this.c.getChunk(n, n2);
        double[][][] dArray = this.a(this.d, 16, 128, 16, 4, 8, 4, n << 4, 0, n2 << 4);
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                baseFullChunk.setBiomeId(k, i2, EnumBiome.END.biome.getId());
                for (int i3 = 12; i3 < 64; ++i3) {
                    double d2 = 0.0 - dArray[k][i2][i3];
                    d2 -= 0.5;
                    double d3 = new Vector3(0.0, 64.0, 0.0).distance(new Vector3((n << 4) + k, (double)i3 / 1.3, (n2 << 4) + i2));
                    if (!(d2 < 0.0 && d3 < 100.0) && (!(d2 < -0.2) || !(d3 > 400.0))) continue;
                    baseFullChunk.setBlockId(k, i3, i2, 121);
                }
            }
        }
    }

    @Override
    public void populateChunk(int n, int n2) {
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.0, 64.0, 0.0);
    }

    private double[][][] a(Noise noise, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        int n10;
        int n11;
        int n12;
        double[][][] dArray = new double[n + 1][n3 + 1][n2 + 1];
        for (n12 = 0; n12 <= n; n12 += n4) {
            for (n11 = 0; n11 <= n3; n11 += n6) {
                for (n10 = 0; n10 <= n2; n10 += n5) {
                    dArray[n12][n11][n10] = noise.noise3D(n7 + n12, n8 + n10, n9 + n11, true);
                }
            }
        }
        for (n12 = 0; n12 < n; ++n12) {
            for (n11 = 0; n11 < n3; ++n11) {
                for (n10 = 0; n10 < n2; ++n10) {
                    if (n12 % n4 == 0 && n11 % n6 == 0 && n10 % n5 == 0) continue;
                    int n13 = n12 / n4 * n4;
                    int n14 = n10 / n5 * n5;
                    int n15 = n11 / n6 * n6;
                    int n16 = n13 + n4;
                    int n17 = n14 + n5;
                    int n18 = n15 + n6;
                    double d2 = (double)(n16 - n12) / (double)(n16 - n13);
                    double d3 = (double)(n12 - n13) / (double)(n16 - n13);
                    double d4 = (double)(n17 - n10) / (double)(n17 - n14);
                    double d5 = (double)(n10 - n14) / (double)(n17 - n14);
                    dArray[n12][n11][n10] = (double)(n18 - n11) / (double)(n18 - n15) * (d4 * (d2 * dArray[n13][n15][n14] + d3 * dArray[n16][n15][n14]) + d5 * (d2 * dArray[n13][n15][n17] + d3 * dArray[n16][n15][n17])) + (double)(n11 - n15) / (double)(n18 - n15) * (d4 * (d2 * dArray[n13][n18][n14] + d3 * dArray[n16][n18][n14]) + d5 * (d2 * dArray[n13][n18][n17] + d3 * dArray[n16][n18][n17]));
                }
            }
        }
        return dArray;
    }
}

