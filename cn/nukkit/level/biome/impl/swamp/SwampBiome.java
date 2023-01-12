/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.swamp;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.MushroomPopulator;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;
import cn.nukkit.level.generator.populator.impl.PopulatorLilyPad;
import cn.nukkit.level.generator.populator.impl.PopulatorSeagrass;
import cn.nukkit.level.generator.populator.impl.PopulatorSmallMushroom;
import cn.nukkit.level.generator.populator.impl.PopulatorUnderwaterFloor;
import cn.nukkit.level.generator.populator.impl.tree.SwampTreePopulator;
import java.util.Arrays;

public class SwampBiome
extends GrassyBiome {
    public SwampBiome() {
        PopulatorUnderwaterFloor populatorUnderwaterFloor = new PopulatorUnderwaterFloor(1.0, 82, 1, 2, 1, Arrays.asList(3, 82));
        populatorUnderwaterFloor.setBaseAmount(1);
        this.addPopulator(populatorUnderwaterFloor);
        PopulatorSeagrass populatorSeagrass = new PopulatorSeagrass();
        populatorSeagrass.setBaseAmount(24);
        populatorSeagrass.setBaseAmount(24);
        this.addPopulator(populatorSeagrass);
        PopulatorLilyPad populatorLilyPad = new PopulatorLilyPad();
        populatorLilyPad.setBaseAmount(4);
        populatorLilyPad.setRandomAmount(2);
        this.addPopulator(populatorLilyPad);
        SwampTreePopulator swampTreePopulator = new SwampTreePopulator();
        swampTreePopulator.setBaseAmount(2);
        this.addPopulator(swampTreePopulator);
        PopulatorFlower populatorFlower = new PopulatorFlower();
        populatorFlower.setBaseAmount(2);
        populatorFlower.addType(38, 1);
        this.addPopulator(populatorFlower);
        MushroomPopulator mushroomPopulator = new MushroomPopulator(1);
        mushroomPopulator.setBaseAmount(-5);
        mushroomPopulator.setRandomAmount(7);
        this.addPopulator(mushroomPopulator);
        PopulatorSmallMushroom populatorSmallMushroom = new PopulatorSmallMushroom();
        populatorSmallMushroom.setRandomAmount(2);
        this.addPopulator(populatorSmallMushroom);
        this.setBaseHeight(-0.2f);
        this.setHeightVariation(0.1f);
    }

    @Override
    public String getName() {
        return "Swamp";
    }
}

