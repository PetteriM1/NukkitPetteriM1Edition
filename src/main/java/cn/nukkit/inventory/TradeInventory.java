package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.network.protocol.UpdateTradePacket;

import java.io.IOException;
import java.nio.ByteOrder;

public class TradeInventory extends BaseInventory {

    public TradeInventory(InventoryHolder holder) {
        super(holder, InventoryType.TRADING);
    }

    public void onOpen(Player who) {
        super.onOpen(who);

        UpdateTradePacket pk = new UpdateTradePacket();
        pk.protocol = who.protocol;
        pk.windowId = (byte) who.getWindowId(this);
        pk.windowType = (byte) InventoryType.TRADING.getNetworkType();
        pk.unknownVarInt1 = 0;
        pk.isWilling = this.getHolder().isWilling();
        pk.screen2 = true;
        pk.trader = this.getHolder().getId();
        pk.tradeTier = this.getHolder().getTradeTier();
        pk.player = who.getId();
        pk.displayName = this.getHolder().getNameTag();
        try {
            pk.offers = NBTIO.write(this.getHolder().getOffers(), ByteOrder.LITTLE_ENDIAN, true);
        } catch(IOException ignored) {}

        who.dataPacket(pk);
    }

    public void onClose(Player who) {
        for (int i = 0; i <= 1; i++) {
            Item item = getItem(i);
            if (who.getInventory().canAddItem(item)) {
                who.getInventory().addItem(item);
            } else {
                who.dropItem(item);
            }
            this.clear(i);
        }

        super.onClose(who);
    }

    public EntityVillager getHolder() {
        return (EntityVillager) this.holder;
    }
}
