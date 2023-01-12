/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.ocean;

import cn.nukkit.level.biome.impl.ocean.FrozenOceanBiome;
import cn.nukkit.level.biome.type.WateryBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorKelp;
import cn.nukkit.level.generator.populator.impl.PopulatorSeagrass;
import cn.nukkit.level.generator.populator.impl.PopulatorUnderwaterFloor;
import cn.nukkit.level.generator.populator.type.PopulatorOceanFloorSurfaceBlock;
import java.util.Arrays;

public class OceanBiome
extends WateryBiome {
    public OceanBiome() {
        PopulatorOceanFloorSurfaceBlock populatorOceanFloorSurfaceBlock;
        PopulatorUnderwaterFloor populatorUnderwaterFloor = new PopulatorUnderwaterFloor(1.0, 12, 2, 4, 2, Arrays.asList(2, 3));
        populatorUnderwaterFloor.setBaseAmount(3);
        this.addPopulator(populatorUnderwaterFloor);
        PopulatorUnderwaterFloor populatorUnderwaterFloor2 = new PopulatorUnderwaterFloor(1.0, 82, 1, 2, 1, Arrays.asList(3, 82));
        populatorUnderwaterFloor2.setBaseAmount(1);
        this.addPopulator(populatorUnderwaterFloor2);
        PopulatorUnderwaterFloor populatorUnderwaterFloor3 = new PopulatorUnderwaterFloor(1.0, 13, 2, 3, 2, Arrays.asList(2, 3));
        populatorUnderwaterFloor3.setBaseAmount(1);
        this.addPopulator(populatorUnderwaterFloor3);
        if (!(this instanceof FrozenOceanBiome)) {
            populatorOceanFloorSurfaceBlock = new PopulatorKelp();
            populatorOceanFloorSurfaceBlock.setBaseAmount(-135);
            populatorOceanFloorSurfaceBlock.setRandomAmount(180);
            this.addPopulator(populatorOceanFloorSurfaceBlock);
        }
        populatorOceanFloorSurfaceBlock = new PopulatorSeagrass();
        populatorOceanFloorSurfaceBlock.setBaseAmount(24);
        populatorOceanFloorSurfaceBlock.setBaseAmount(24);
        this.addPopulator(populatorOceanFloorSurfaceBlock);
        this.setBaseHeight(-1.0f);
        this.setHeightVariation(0.1f);
    }

    @Override
    public String getName() {
        return "Ocean";
    }

    @Override
    public int getGroundId(int n, int n2, int n3) {
        return 208;
    }
}

