package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityTameable {

    String NAMED_TAG_OWNER_UUID = "OwnerUUID";

    String NAMED_TAG_SITTING = "Sitting";

    Player getOwner();

    void setOwner(Player player);

    String getOwnerUUID();

    void setOwnerUUID(String uuid);

    boolean hasOwner();

    default boolean isOwner(Entity entity) {
        return entity instanceof Player && ((Player) entity).getUniqueId().toString().equals(this.getOwnerUUID());
    }

    boolean isSitting();

    void setSitting(boolean sitting);
}
