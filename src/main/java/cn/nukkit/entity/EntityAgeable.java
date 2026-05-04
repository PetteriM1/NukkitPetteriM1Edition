package cn.nukkit.entity;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface EntityAgeable {

    default void setBaby(boolean baby) {

    }

    boolean isBaby();
}
