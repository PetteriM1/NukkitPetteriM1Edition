package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityWitherSkeleton;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class WitherSkeletonSpawner extends AbstractEntitySpawner {

    public WitherSkeletonSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (!level.getName().equals("nether")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (pos.y > 127 || pos.y < 1) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) != Block.NETHERRACK) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity("WitherSkeleton", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWitherSkeleton.NETWORK_ID;
    }
}
