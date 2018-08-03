package cn.nukkit.level.generator.biome;

import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorMelon;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.tree.JungleTreePopulator;

public class JungleBiome extends GrassyBiome {

    public JungleBiome() {
        super();
        JungleTreePopulator trees = new JungleTreePopulator();
        trees.setBaseAmount(10);
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(6);
        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(2);
        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(20);
        PopulatorMelon melons = new PopulatorMelon();
        melons.setBaseAmount(1);
        this.addPopulator(grass);
        this.addPopulator(tallGrass);
        this.addPopulator(bigTrees);
        this.addPopulator(trees);
        this.setElevation(62, 63);
        this.temperature = 1.2;
        this.rainfall = 0.9;
    }

    @Override
    public String getName() {
        return "Jungle";
    }
}
