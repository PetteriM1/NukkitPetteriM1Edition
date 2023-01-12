/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockLeaves;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;

public class ItemFireCharge
extends Item {
    public ItemFireCharge() {
        this((Integer)0, 1);
    }

    public ItemFireCharge(Integer n) {
        this(n, 1);
    }

    public ItemFireCharge(Integer n, int n2) {
        super(385, 0, n2, "Fire Charge");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (player.isAdventure() && !player.getServer().suomiCraftPEMode()) {
            return false;
        }
        if (block.getId() == 0 && (block2 instanceof BlockSolid || block2 instanceof BlockSolidMeta || block2 instanceof BlockLeaves)) {
            if (block2.getId() == 49 && level.createPortal(block2, true)) {
                return true;
            }
            BlockFire blockFire = (BlockFire)Block.get(block.down().getId() == 88 ? 492 : 51);
            blockFire.x = block.x;
            blockFire.y = block.y;
            blockFire.z = block.z;
            blockFire.level = level;
            if (blockFire.isBlockTopFacingSurfaceSolid(blockFire.down()) || blockFire.canNeighborBurn()) {
                BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, null, player, BlockIgniteEvent.BlockIgniteCause.FIREBALL);
                block.getLevel().getServer().getPluginManager().callEvent(blockIgniteEvent);
                if (!blockIgniteEvent.isCancelled()) {
                    level.setBlock(blockFire, blockFire, true);
                    level.scheduleUpdate(blockFire, Server.getInstance().suomiCraftPEMode() ? Utils.rand(200, 400) : blockFire.tickRate() + Utils.random.nextInt(10));
                    level.addSound((Vector3)block, Sound.MOB_GHAST_FIREBALL);
                    if (!player.isCreative()) {
                        player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                    }
                }
                return true;
            }
        }
        return false;
    }
}

