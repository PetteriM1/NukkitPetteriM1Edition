package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityMagmaCube;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class MagmaCubeSpawner extends AbstractEntitySpawner {

    public MagmaCubeSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (!level.getName().equals("nether")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) != Block.NETHERRACK) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1) {
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity("MagmaCube", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityMagmaCube.NETWORK_ID;
    }
}
