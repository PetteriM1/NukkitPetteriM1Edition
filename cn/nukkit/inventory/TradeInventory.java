/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.passive.EntityVillagerV1;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.network.protocol.UpdateTradePacket;
import java.io.IOException;
import java.nio.ByteOrder;

public class TradeInventory
extends BaseInventory {
    public TradeInventory(InventoryHolder inventoryHolder) {
        super(inventoryHolder, InventoryType.TRADING);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        UpdateTradePacket updateTradePacket = new UpdateTradePacket();
        updateTradePacket.windowId = (byte)player.getWindowId(this);
        updateTradePacket.windowType = (byte)InventoryType.TRADING.getNetworkType();
        updateTradePacket.isWilling = this.getHolder().isWilling();
        updateTradePacket.screen2 = true;
        updateTradePacket.trader = this.getHolder().getId();
        updateTradePacket.tradeTier = this.getHolder().getTradeTier();
        updateTradePacket.player = player.getId();
        try {
            updateTradePacket.offers = NBTIO.write(this.getHolder().getOffers(), ByteOrder.LITTLE_ENDIAN);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        player.dataPacket(updateTradePacket);
    }

    @Override
    public void onClose(Player player) {
        for (int k = 0; k <= 1; ++k) {
            Item item = this.getItem(k);
            this.clear(k);
            if (player.getInventory().canAddItem(item)) {
                player.getInventory().addItem(item);
                continue;
            }
            player.level.dropItem(player, item);
        }
        super.onClose(player);
    }

    @Override
    public EntityVillagerV1 getHolder() {
        return (EntityVillagerV1)this.holder;
    }
}

