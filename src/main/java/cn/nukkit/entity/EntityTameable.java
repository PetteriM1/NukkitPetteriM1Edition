package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityTameable {

    String NAMED_TAG_OWNER_UUID = "OwnerUUID";

    String NAMED_TAG_SITTING = "Sitting";

    void setOwner(Player player);

    void setOwnerUUID(String uuid);

    void setSitting(boolean sitting);

    Player getOwner();

    String getOwnerUUID();

    boolean isSitting();

    boolean hasOwner();

    default boolean isOwner(Entity entity) {
        return entity instanceof Player && ((Player) entity).getUniqueId().toString().equals(this.getOwnerUUID());
    }
}
