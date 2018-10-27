package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class ZombieSpawner extends AbstractEntitySpawner {

    public ZombieSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int time = level.getTime() % Level.TIME_FULL;
        int light = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (light > 7) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (Block.transparent[blockId]) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (biomeId == Biome.DESERT) {
            if (blockId != Block.SAND) {
                result = SpawnResult.WRONG_BLOCK;
            } else if (time > 13184 && time < 22800) {
                this.spawnTask.createEntity("Husk", pos.add(0, 2.8, 0));
            }
        } else if (biomeId == Biome.HELL || level.getName().equals("end")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (time > 13184 && time < 22800) {
            if (EntityUtils.rand(1, 40) == 30) {
                this.spawnTask.createEntity("ZombieVillager", pos.add(0, 2.8, 0));
            } else {
                this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.8, 0));
            }
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityZombie.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Zombie";
    }
}
