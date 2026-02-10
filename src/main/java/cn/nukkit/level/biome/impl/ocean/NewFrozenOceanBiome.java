package cn.nukkit.level.biome.impl.ocean;

public class NewFrozenOceanBiome extends OceanBiome {

    public NewFrozenOceanBiome() {
        super();

        //TODO: ice mountains
    }

    @Override
    public boolean canRain() {
        return false;
    }

    @Override
    public String getName() {
        return "Frozen Ocean";
    }

    @Override
    public boolean isFreezing() {
        return true;
    }
}
