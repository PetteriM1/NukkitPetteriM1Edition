package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnResult;
import cn.nukkit.utils.Spawner;

public class GhastSpawner extends AbstractEntitySpawner {

    public GhastSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (!level.getName().equals("nether")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (Block.transparent[blockId]) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1 || blockId == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity("Ghast", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityGhast.NETWORK_ID;
    }
}
