/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Flat
extends Generator {
    private ChunkManager c;
    private NukkitRandom f;
    private final List<Populator> e = new ArrayList<Populator>();
    private int[][] d;
    private final Map<String, Object> k;
    private int i;
    private String h = "2;7,2x3,2;1;";
    private boolean g = false;
    private int j;

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.c;
    }

    @Override
    public Map<String, Object> getSettings() {
        return this.k;
    }

    @Override
    public String getName() {
        return "flat";
    }

    public Flat() {
        this(Collections.emptyMap());
    }

    public Flat(Map<String, Object> map) {
        this.k = map;
        if (this.k.containsKey("decoration")) {
            PopulatorOre populatorOre = new PopulatorOre(1, new OreType[]{new OreType(Block.get(16), 20, 17, 0, 128), new OreType(Block.get(15), 20, 9, 0, 64), new OreType(Block.get(73), 8, 8, 0, 16), new OreType(Block.get(21), 1, 7, 0, 30), new OreType(Block.get(14), 2, 9, 0, 32), new OreType(Block.get(56), 1, 8, 0, 16), new OreType(Block.get(3), 10, 33, 0, 128), new OreType(Block.get(13), 8, 33, 0, 128)});
            this.e.add(populatorOre);
        }
    }

    protected void parsePreset(String string) {
        try {
            this.h = string;
            String[] stringArray = string.split(";");
            String string2 = stringArray.length > 1 ? stringArray[1] : "";
            this.j = stringArray.length > 2 ? Integer.parseInt(stringArray[2]) : 1;
            String string3 = stringArray.length > 3 ? stringArray[1] : "";
            this.d = new int[256][];
            int n = 0;
            for (String string4 : string2.split(",")) {
                int n2;
                int n3 = 0;
                int n4 = 1;
                if (Pattern.matches("^[0-9]{1,3}x[0-9]$", string4)) {
                    String[] stringArray2 = string4.split("x");
                    n4 = Integer.parseInt(stringArray2[0]);
                    n2 = Integer.parseInt(stringArray2[1]);
                } else if (Pattern.matches("^[0-9]{1,3}:[0-9]{0,2}$", string4)) {
                    String[] stringArray3 = string4.split(":");
                    n2 = Integer.parseInt(stringArray3[0]);
                    n3 = Integer.parseInt(stringArray3[1]);
                } else {
                    if (!Pattern.matches("^[0-9]{1,3}$", string4)) continue;
                    n2 = Integer.parseInt(string4);
                }
                int n5 = n;
                if ((n += n4) > 255) {
                    n = 255;
                }
                while (n5 < n) {
                    this.d[n5] = new int[]{n2, n3};
                    ++n5;
                }
            }
            this.i = n;
            while (n <= 255) {
                this.d[n] = new int[]{0, 0};
                ++n;
            }
            for (String string4 : string3.split(",")) {
                if (Pattern.matches("^[0-9a-z_]+$", string4)) {
                    this.k.put(string4, true);
                    continue;
                }
                if (!Pattern.matches("^[0-9a-z_]+\\([0-9a-z_ =]+\\)$", string4)) continue;
                String string5 = string4.substring(0, string4.indexOf(40));
                String string6 = string4.substring(string4.indexOf(40) + 1, string4.indexOf(41));
                HashMap<String, Float> hashMap = new HashMap<String, Float>();
                for (String string7 : string6.split(" ")) {
                    String[] stringArray4 = string7.split("=");
                    hashMap.put(stringArray4[0], Float.valueOf(stringArray4[1]));
                }
                this.k.put(string5, hashMap);
            }
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().error("Error while parsing the preset", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.c = chunkManager;
        this.f = nukkitRandom;
    }

    @Override
    public void generateChunk(int n, int n2) {
        if (!this.g) {
            this.g = true;
            if (this.k.containsKey("preset") && !"".equals(this.k.get("preset"))) {
                this.parsePreset((String)this.k.get("preset"));
            } else {
                this.parsePreset(this.h);
            }
        }
        this.a(this.c.getChunk(n, n2));
    }

    private void a(FullChunk fullChunk) {
        fullChunk.setGenerated();
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                fullChunk.setBiomeId(i2, k, this.j);
                for (int i3 = 0; i3 < 256; ++i3) {
                    fullChunk.setBlock(i2, i3, k, this.d[i3][0], this.d[i3][1]);
                }
            }
        }
    }

    @Override
    public void populateChunk(int n, int n2) {
        this.f.setSeed((long)(0xDEADBEEF ^ n << 8 ^ n2) ^ this.c.getSeed());
        for (Populator populator : this.e) {
            populator.populate(this.c, n, n2, this.f, this.c.getChunk(n, n2));
        }
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0.5, this.i, 0.5);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

