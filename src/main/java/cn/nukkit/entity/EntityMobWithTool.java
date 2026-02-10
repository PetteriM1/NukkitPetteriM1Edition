package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;

import javax.annotation.Nullable;

/**
 * A mob that can hold tools in its hands
 */
public interface EntityMobWithTool {

    @Nullable
    Item getOffhand();

    default int getOffhandSlot() {
        return 1;
    }

    @Nullable
    Item getTool();

    default int getToolSlot() {
        return 0;
    }

    default void sendHandItems(Player p) {
        sendHandItems(p, false);
    }

    /**
     * Update mob's held items to player
     *
     * @param player  player
     * @param sendAir whether empty (air) slots should be sent
     */
    default void sendHandItems(Player player, boolean sendAir) {
        Item tool = this.getTool();

        if (tool != null && (sendAir || !tool.isNull())) {
            Item clean = null;

            if (Server.getInstance().reduceTraffic) {
                clean = Item.get(tool.getId(), tool.getDamage(), 1);

                CompoundTag oldTag = tool.getNamedTag();

                if (oldTag != null) {
                    clean.setNamedTag(CompoundTag.sanitize(oldTag));
                }
            }

            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = ((Entity) this).getId();
            pk.hotbarSlot = this.getToolSlot();
            pk.item = clean != null ? clean : tool;
            player.dataPacket(pk);
        }

        Item offhand = this.getOffhand();

        if (offhand != null && (sendAir || !offhand.isNull())) {
            Item clean = null;

            if (Server.getInstance().reduceTraffic) {
                clean = Item.get(offhand.getId(), offhand.getDamage(), 1);

                CompoundTag oldTag = offhand.getNamedTag();

                if (oldTag != null) {
                    clean.setNamedTag(CompoundTag.sanitize(oldTag));
                }
            }

            MobEquipmentPacket pk = new MobEquipmentPacket();
            pk.eid = ((Entity) this).getId();
            pk.hotbarSlot = this.getOffhandSlot();
            pk.item = clean != null ? clean : offhand;
            player.dataPacket(pk);
        }
    }

    @SuppressWarnings("unused")
    void setOffhand(@Nullable Item offhand);

    @SuppressWarnings("unused")
    void setTool(@Nullable Item tool);
}
