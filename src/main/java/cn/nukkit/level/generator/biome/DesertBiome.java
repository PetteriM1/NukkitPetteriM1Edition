package cn.nukkit.level.generator.biome;

import cn.nukkit.level.generator.populator.PopulatorWell;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {

    public DesertBiome() {
        super();
        this.setElevation(63, 74);
        this.temperature = 2;
        this.rainfall = 0;
        
        PopulatorWell well = new PopulatorWell();
        this.addPopulator(well);
    }

    @Override
    public String getName() {
        return "Desert";
    }
}
