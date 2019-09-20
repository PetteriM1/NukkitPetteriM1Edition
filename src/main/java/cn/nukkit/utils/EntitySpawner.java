package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public interface EntitySpawner {

    void spawn();

    SpawnResult spawn(Player player, Position pos, Level level);

    int getEntityNetworkId();
}
