/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.event.player.CraftingTableOpenEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.utils.BlockColor;

public class BlockCraftingTable
extends BlockSolid {
    @Override
    public String getName() {
        return "Crafting Table";
    }

    @Override
    public int getId() {
        return 58;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            CraftingTableOpenEvent craftingTableOpenEvent = new CraftingTableOpenEvent(player, this);
            player.getServer().getPluginManager().callEvent(craftingTableOpenEvent);
            if (!craftingTableOpenEvent.isCancelled()) {
                if (player.craftingType == 1) {
                    player.getServer().getLogger().debug(player.getName() + " tried to activate crafting table but craftingType is already CRAFTING_BIG");
                    return true;
                }
                player.craftingType = 1;
                player.setCraftingGrid(player.getUIInventory().getBigCraftingGrid());
                if (player.protocol >= 407) {
                    ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
                    containerOpenPacket.windowId = -1;
                    containerOpenPacket.type = 1;
                    containerOpenPacket.x = (int)this.x;
                    containerOpenPacket.y = (int)this.y;
                    containerOpenPacket.z = (int)this.z;
                    containerOpenPacket.entityId = player.getId();
                    player.dataPacket(containerOpenPacket);
                }
            }
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}

