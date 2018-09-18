package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.List;

public interface EntitySpawner {

    public void spawn(List<Player> onlinePlayers);

    public SpawnResult spawn(Player player, Position pos, Level level);

    public int getEntityNetworkId();

    public String getEntityName();
}
