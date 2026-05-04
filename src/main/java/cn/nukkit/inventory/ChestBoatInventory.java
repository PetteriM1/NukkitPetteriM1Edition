package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ChestBoatInventory extends ContainerInventory {

    public ChestBoatInventory(EntityChestBoat holder) {
        super(holder, InventoryType.CHEST_BOAT);
    }

    @Override
    public EntityChestBoat getHolder() {
        return (EntityChestBoat) super.getHolder();
    }

    @Override
    public void onOpen(Player who) {
        this.viewers.add(who);

        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.type = who.protocol >= ProtocolInfo.v1_19_0_29 ? InventoryType.CHEST_BOAT.getNetworkType() : InventoryType.CHEST.getNetworkType(); // Use chest inventory window for old versions
        InventoryHolder holder = this.getHolder();
        if (holder != null) {
            pk.x = (int) ((Vector3) holder).getX();
            pk.y = (int) ((Vector3) holder).getY();
            pk.z = (int) ((Vector3) holder).getZ();
        } else {
            pk.x = pk.y = pk.z = 0;
        }

        if (holder != null) {
            pk.entityId = ((Entity) holder).getId();
        }

        who.dataPacket(pk);

        this.sendContents(who);
    }
}
