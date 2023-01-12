/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public abstract class BlockRedstoneDiode
extends BlockFlowable
implements Faceable {
    protected boolean isPowered = false;

    public BlockRedstoneDiode() {
        this(0);
    }

    public BlockRedstoneDiode(int n) {
        super(n);
    }

    @Override
    public boolean onBreak(Item item) {
        Location location = this.getLocation();
        this.level.setBlock(this, Block.get(0), true, true);
        for (BlockFace blockFace : BlockFace.values()) {
            this.level.updateAroundRedstone(location.getSideVec(blockFace), null);
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!BlockRedstoneDiode.canStayOnFullSolid(this.down())) {
            return false;
        }
        this.setDamage(player != null ? player.getDirection().getOpposite().getHorizontalIndex() : 0);
        this.level.setBlock(block, this, true, true);
        if (this.shouldBePowered()) {
            this.level.scheduleUpdate(this, 1);
        }
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            if (!this.isLocked()) {
                Location location = this.getLocation();
                boolean bl = this.shouldBePowered();
                if (this.isPowered && !bl) {
                    this.level.setBlock(location, this.getUnpowered(), true, true);
                    this.level.updateAroundRedstone(this.getLocation().getSide(this.getFacing().getOpposite()), null);
                } else if (!this.isPowered) {
                    this.level.setBlock(location, this.getPowered(), true, true);
                    this.level.updateAroundRedstone(this.getLocation().getSide(this.getFacing().getOpposite()), null);
                    if (!bl) {
                        this.level.scheduleUpdate(this.getPowered(), this, this.getDelay());
                    }
                }
            }
        } else if (n == 1 || n == 6) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
            if (n == 1 && !BlockRedstoneDiode.canStayOnFullSolid(this.down())) {
                this.level.useBreakOn(this);
            } else {
                this.updateState();
            }
            return 1;
        }
        return 0;
    }

    public void updateState() {
        if (!this.isLocked()) {
            boolean bl = this.shouldBePowered();
            if ((this.isPowered && !bl || !this.isPowered && bl) && !this.level.isBlockTickPending(this, this)) {
                this.level.scheduleUpdate(this, this, this.getDelay());
            }
        }
    }

    public boolean isLocked() {
        return false;
    }

    protected int calculateInputStrength() {
        BlockFace blockFace = this.getFacing();
        Position position = this.getLocation().getSide(blockFace);
        int n = this.level.getRedstonePower(position, blockFace);
        if (n >= 15) {
            return n;
        }
        Block block = this.level.getBlock(position);
        return Math.max(n, block.getId() == 55 ? block.getDamage() : 0);
    }

    protected int getPowerOnSides() {
        Location location = this.getLocation();
        BlockFace blockFace = this.getFacing();
        BlockFace blockFace2 = blockFace.rotateY();
        BlockFace blockFace3 = blockFace.rotateYCCW();
        return Math.max(this.getPowerOnSide(location.getSideVec(blockFace2), blockFace2), this.getPowerOnSide(location.getSideVec(blockFace3), blockFace3));
    }

    protected int getPowerOnSide(Vector3 vector3, BlockFace blockFace) {
        Block block = this.level.getBlock(vector3);
        return this.isAlternateInput(block) ? (block.getId() == 152 ? 15 : (block.getId() == 55 ? block.getDamage() : this.level.getStrongPower(vector3, blockFace))) : 0;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    protected boolean shouldBePowered() {
        return this.calculateInputStrength() > 0;
    }

    public abstract BlockFace getFacing();

    protected abstract int getDelay();

    protected abstract Block getUnpowered();

    protected abstract Block getPowered();

    @Override
    public double getMaxY() {
        return this.y + 0.125;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    protected boolean isAlternateInput(Block block) {
        return block.isPowerSource();
    }

    public static boolean isDiode(Block block) {
        return block instanceof BlockRedstoneDiode;
    }

    protected int getRedstoneSignal() {
        return 15;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return this.getWeakPower(blockFace);
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return !this.isPowered() ? 0 : (this.getFacing() == blockFace ? this.getRedstoneSignal() : 0);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public boolean isPowered() {
        return this.isPowered;
    }

    public boolean isFacingTowardsRepeater() {
        BlockFace blockFace = this.getFacing().getOpposite();
        Block block = this.getSide(blockFace);
        return block instanceof BlockRedstoneDiode && ((BlockRedstoneDiode)block).getFacing() != blockFace;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

