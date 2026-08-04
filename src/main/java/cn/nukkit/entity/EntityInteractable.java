package cn.nukkit.entity;

import cn.nukkit.Player;

/**
 * @author Adam Matthew
 */
public interface EntityInteractable {

    String getInteractButtonText();

    boolean canDoInteraction();

    default String getInteractButtonText(Player player) {
        return this.getInteractButtonText();
    }
}
