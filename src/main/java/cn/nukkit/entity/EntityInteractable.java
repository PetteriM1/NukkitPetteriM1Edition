package cn.nukkit.entity;

import cn.nukkit.Player;

/**
 * @author Adam Matthew
 */
public interface EntityInteractable {

    boolean canDoInteraction();

    default String getInteractButtonText(Player player) {
        return this.getInteractButtonText();
    }

    String getInteractButtonText();
}
