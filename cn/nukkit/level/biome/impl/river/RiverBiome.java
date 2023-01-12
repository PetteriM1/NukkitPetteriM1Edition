/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.river;

import cn.nukkit.block.Block;
import cn.nukkit.level.biome.type.WateryBiome;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.impl.PopulatorSeagrass;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorTallSugarcane;
import cn.nukkit.level.generator.populator.impl.PopulatorUnderwaterFloor;
import java.util.Arrays;

public class RiverBiome
extends WateryBiome {
    public RiverBiome() {
        PopulatorUnderwaterFloor populatorUnderwaterFloor = new PopulatorUnderwaterFloor(1.0, 12, 2, 4, 2, Arrays.asList(2, 3));
        populatorUnderwaterFloor.setBaseAmount(3);
        this.addPopulator(populatorUnderwaterFloor);
        PopulatorUnderwaterFloor populatorUnderwaterFloor2 = new PopulatorUnderwaterFloor(1.0, 82, 1, 2, 1, Arrays.asList(3, 82));
        populatorUnderwaterFloor2.setBaseAmount(1);
        this.addPopulator(populatorUnderwaterFloor2);
        PopulatorUnderwaterFloor populatorUnderwaterFloor3 = new PopulatorUnderwaterFloor(1.0, 13, 2, 3, 2, Arrays.asList(2, 3));
        populatorUnderwaterFloor3.setBaseAmount(1);
        this.addPopulator(populatorUnderwaterFloor3);
        PopulatorSeagrass populatorSeagrass = new PopulatorSeagrass();
        populatorSeagrass.setBaseAmount(24);
        populatorSeagrass.setBaseAmount(24);
        this.addPopulator(populatorSeagrass);
        PopulatorSugarcane populatorSugarcane = new PopulatorSugarcane();
        populatorSugarcane.setRandomAmount(3);
        this.addPopulator(populatorSugarcane);
        PopulatorTallSugarcane populatorTallSugarcane = new PopulatorTallSugarcane();
        populatorTallSugarcane.setRandomAmount(1);
        this.addPopulator(populatorTallSugarcane);
        PopulatorOre populatorOre = new PopulatorOre(3, new OreType[]{new OreType(Block.get(13), 13, 33, 50, 60), new OreType(Block.get(82), 8, 22, 50, 60)});
        this.addPopulator(populatorOre);
        this.setBaseHeight(-0.5f);
        this.setHeightVariation(0.0f);
    }

    @Override
    public String getName() {
        return "River";
    }
}

