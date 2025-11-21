package cn.nukkit.entity;

import cn.nukkit.Player;

/**
 * @author BeYkeRYkt
 * Nukkit Project
 */
public interface EntityOwnable {

    Player getOwner();

    String getOwnerName();

    void setOwnerName(String playerName);
}
