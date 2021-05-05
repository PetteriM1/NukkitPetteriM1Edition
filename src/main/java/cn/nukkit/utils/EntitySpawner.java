package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

/**
 * Interface of a mob spawner
 */
public interface EntitySpawner {

    /**
     * Find safe coordinates and attempt to spawn a mob
     */
    void spawn();

    /**
     * Run the spawner
     *
     * @param player player
     * @param pos safe position
     * @param level world
     */
    void spawn(Player player, Position pos, Level level);

    /**
     * Get entity network id of this mob spawner
     *
     * @return entity network id
     */
    int getEntityNetworkId();
}
