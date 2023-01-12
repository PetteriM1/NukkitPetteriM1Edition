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
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Utils;

public class ItemFlintSteel
extends ItemTool {
    public ItemFlintSteel() {
        this((Integer)0, 1);
    }

    public ItemFlintSteel(Integer n) {
        this(n, 1);
    }

    public ItemFlintSteel(Integer n, int n2) {
        super(259, n, n2, "Flint and Steel");
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
        if ((block.getId() == 0 || block.getId() == 51 || block.getId() == 492) && (block2 instanceof BlockSolid || block2 instanceof BlockSolidMeta || block2 instanceof BlockLeaves)) {
            if (block2.getId() == 49 && level.createPortal(block2, false)) {
                return true;
            }
            BlockFire blockFire = (BlockFire)Block.get(block.down().getId() == 88 ? 492 : 51);
            blockFire.x = block.x;
            blockFire.y = block.y;
            blockFire.z = block.z;
            blockFire.level = level;
            if (blockFire.isBlockTopFacingSurfaceSolid(blockFire.down()) || blockFire.canNeighborBurn()) {
                BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, null, player, BlockIgniteEvent.BlockIgniteCause.FLINT_AND_STEEL);
                block.getLevel().getServer().getPluginManager().callEvent(blockIgniteEvent);
                if (!blockIgniteEvent.isCancelled()) {
                    level.setBlock(blockFire, blockFire, true);
                    level.scheduleUpdate(blockFire, Server.getInstance().suomiCraftPEMode() ? Utils.rand(200, 400) : blockFire.tickRate() + Utils.random.nextInt(10));
                    level.addLevelSoundEvent(block, 50);
                    if (!player.isCreative()) {
                        this.useOn(block);
                        if (this.getDamage() >= 65) {
                            this.count = 0;
                        }
                        player.getInventory().setItemInHand(this);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getMaxDurability() {
        return 65;
    }
}

