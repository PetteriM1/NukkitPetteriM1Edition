package cn.nukkit.entity;

import cn.nukkit.Player;

/**
 * @author BeYkeRYkt
 * Nukkit Project
 */
public interface EntityOwnable {

    void setOwnerName(String playerName);

    Player getOwner();

    String getOwnerName();
}
