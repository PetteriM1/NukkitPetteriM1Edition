package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.Collection;

public interface EntitySpawner {

    void spawn(Collection<Player> onlinePlayers);

    SpawnResult spawn(Player player, Position pos, Level level);

    int getEntityNetworkId();
}
