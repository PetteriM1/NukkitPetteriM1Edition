package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.generator.populator.impl.PopulatorBambooForest;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.impl.tree.JungleTreePopulator;

/**
 * @author Alemiz112
 */
public class JungleBambooBiome extends JungleBiome {

    public JungleBambooBiome() {
        JungleTreePopulator trees = new JungleTreePopulator();
        trees.setBaseAmount(3);
        this.addPopulator(trees);

        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(7);
        this.addPopulator(bigTrees);

        PopulatorMelon melon = new PopulatorMelon();
        melon.setRandomAmount(2);
        this.addPopulator(melon);

        PopulatorBambooForest bamboo = new PopulatorBambooForest();
        this.addPopulator(bamboo);
    }

    @Override
    public String getName() {
        return "Jungle Bamboo";
    }
}
