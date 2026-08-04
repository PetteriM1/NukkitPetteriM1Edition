package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityControllable {

    default void onJump(Player player, int duration) {
    }

    void onPlayerInput(Player player, double strafe, double forward);
}
