package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityPig;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.SpawnerTask;

public class PigSpawner extends AbstractEntitySpawner {

    public PigSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) != Block.GRASS) {
        } else if (pos.y > 255 || pos.y < 1) {
        } else if (level.isAnimalSpawningAllowedByTime()) {
            BaseEntity entity = this.spawnTask.createEntity("Pig", pos.add(0, 1, 0));
            if (Utils.rand(1, 20) == 1) {
                entity.setBaby(true);
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityPig.NETWORK_ID;
    }
}
