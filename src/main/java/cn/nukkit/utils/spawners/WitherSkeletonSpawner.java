package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityWitherSkeleton;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class WitherSkeletonSpawner extends AbstractEntitySpawner {

    public WitherSkeletonSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 3) == 1) {
            return;
        }

        if (pos.y > 127 || pos.y < 1) {
        } else if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) != Block.NETHERRACK) {
        } else {
            this.spawnTask.createEntity("WitherSkeleton", pos.add(0, 1, 0));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWitherSkeleton.NETWORK_ID;
    }
}
