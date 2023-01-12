/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTorch;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockRedstoneTorch
extends BlockTorch
implements Faceable {
    public BlockRedstoneTorch() {
        this(0);
    }

    public BlockRedstoneTorch(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Redstone Torch";
    }

    @Override
    public int getId() {
        return 76;
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!super.place(item, block, block2, blockFace, d2, d3, d4, player)) {
            return false;
        }
        this.checkState();
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.getBlockFace() != blockFace ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return blockFace == BlockFace.DOWN ? this.getWeakPower(blockFace) : 0;
    }

    @Override
    public boolean onBreak(Item item) {
        super.onBreak(item);
        Location location = this.getLocation();
        BlockFace blockFace = this.getBlockFace().getOpposite();
        for (BlockFace blockFace2 : BlockFace.values()) {
            if (blockFace2 == blockFace) continue;
            this.level.updateAroundRedstone(location.getSideVec(blockFace2), null);
        }
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (super.onUpdate(n) == 0) {
            if (n == 1 || n == 6) {
                this.level.scheduleUpdate(this, this.tickRate());
            } else if (n == 3) {
                RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
                this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
                if (redstoneUpdateEvent.isCancelled()) {
                    return 0;
                }
                if (this.checkState()) {
                    return 1;
                }
            }
        }
        return 0;
    }

    protected boolean checkState() {
        if (this.isPoweredFromSide()) {
            BlockFace blockFace = this.getBlockFace().getOpposite();
            Location location = this.getLocation();
            this.level.setBlock(location, Block.get(75, this.getDamage()), false, true);
            for (BlockFace blockFace2 : BlockFace.values()) {
                if (blockFace2 == blockFace) continue;
                this.level.updateAroundRedstone(location.getSideVec(blockFace2), null);
            }
            return true;
        }
        return false;
    }

    protected boolean isPoweredFromSide() {
        BlockFace blockFace = this.getBlockFace().getOpposite();
        return this.level.isSidePowered(this.getLocation().getSide(blockFace), blockFace);
    }

    @Override
    public int tickRate() {
        return 2;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}

