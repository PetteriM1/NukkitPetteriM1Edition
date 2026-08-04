package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class PiglinSpawner extends AbstractEntitySpawner {

    public PiglinSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) <= 7) {
            int biome = level.getBiomeId((int) pos.x, (int) pos.z);
            if (biome != EnumBiome.SOULSAND_VALLEY.id && biome != EnumBiome.BASALT_DELTAS.id && biome != EnumBiome.WARPED_FOREST.id) {
                int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
                if (blockId != Block.BLOCK_NETHER_WART_BLOCK) {
                    for (int i = 0; i < Utils.rand(2, 4); i++) {
                        BaseEntity entity = this.spawnTask.createEntity("Piglin", pos.add(0.5, 1, 0.5));
                        if (entity == null) return;
                        if (Utils.rand(1, 20) == 1) {
                            entity.setBaby(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityPiglin.NETWORK_ID;
    }
}
