/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTorch;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;

public class BlockRedstoneTorchUnlit
extends BlockTorch {
    public BlockRedstoneTorchUnlit() {
        this(0);
    }

    public BlockRedstoneTorchUnlit(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Unlit Redstone Torch";
    }

    @Override
    public int getId() {
        return 75;
    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(76));
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
        BlockFace blockFace = this.getBlockFace().getOpposite();
        Location location = this.getLocation();
        if (!this.level.isSidePowered(location.getSideVec(blockFace), blockFace)) {
            this.level.setBlock(location, Block.get(76, this.getDamage()), false, true);
            for (BlockFace blockFace2 : BlockFace.values()) {
                if (blockFace2 == blockFace) continue;
                this.level.updateAroundRedstone(location.getSideVec(blockFace2), null);
            }
            return true;
        }
        return false;
    }

    @Override
    public int tickRate() {
        return 2;
    }
}

