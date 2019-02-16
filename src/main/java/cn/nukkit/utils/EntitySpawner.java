package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.Collection;

public interface EntitySpawner {

    public void spawn(Collection<Player> onlinePlayers);

    public SpawnResult spawn(Player player, Position pos, Level level);

    public int getEntityNetworkId();
}
