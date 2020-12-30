package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityFox;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class FoxSpawner extends AbstractEntitySpawner {

    public FoxSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 3) == 1) {
            return;
        }

        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (biomeId != 5 && biomeId != 160 && biomeId != 31 && biomeId != 19 && biomeId != 30 && biomeId != 133 && biomeId != 158 && biomeId != 32 && biomeId != 33) {
        } else if (blockId != Block.GRASS) {
        } else if (pos.y > 255 || pos.y < 1) {
        } else if (level.isAnimalSpawningAllowedByTime()) {
            int count = Utils.rand(2, 4); // They spawn in groups of 2-4
            for (int i = 0; i < count; i++) {
                BaseEntity entity = this.spawnTask.createEntity("Fox", pos.add(0, 1, 0));
                if (entity == null) return;
                if (Utils.rand(1, 20) == 1) {
                    entity.setBaby(true);
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityFox.NETWORK_ID;
    }
}
