package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class PhantomSpawner extends AbstractEntitySpawner {

    public PhantomSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (level.isMobSpawningAllowedByTime()) {
            if (pos.y < 130 && pos.y > 0 && biomeId != 14 && biomeId != 15 && level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == 0 && level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) < 8) { // "Phantoms spawn if the player's Y-coordinate is between 1 and 129" - Minecraft Wiki
                EntityPhantom phantom = (EntityPhantom) this.spawnTask.createEntity("Phantom", pos);
                phantom.setTarget(player);
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityPhantom.NETWORK_ID;
    }
}
