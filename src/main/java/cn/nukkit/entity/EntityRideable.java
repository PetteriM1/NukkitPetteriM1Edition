package cn.nukkit.entity;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface EntityRideable {

    boolean dismountEntity(Entity entity);

    /**
     * Mount or Dismounts an Entity from a rideable entity
     *
     * @param entity The target Entity
     * @return {@code true} if the mounting successful
     */
    boolean mountEntity(Entity entity);
}
