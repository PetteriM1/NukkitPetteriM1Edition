package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityWolf;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class WolfSpawner extends AbstractEntitySpawner {

    public WolfSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (level.getName().equals("nether") || level.getName().equals("end")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (biomeId != Biome.FOREST && biomeId != Biome.TAIGA && biomeId != Biome.ICE_PLAINS) {
            result = SpawnResult.WRONG_BIOME;
        } else if (pos.y > 127 || pos.y < 1 || blockId == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (Block.transparent[blockId]) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.8, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWolf.NETWORK_ID;
    }

    @Override
    public final String getEntityName() {
        return "Wolf";
    }
}
