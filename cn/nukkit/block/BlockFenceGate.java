/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.event.block.DoorToggleEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockFenceGate
extends BlockTransparentMeta
implements Faceable {
    public BlockFenceGate() {
        this(0);
    }

    public BlockFenceGate(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 107;
    }

    @Override
    public String getName() {
        return "Oak Fence Gate";
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.getDamage() & 4) > 0) {
            return null;
        }
        int n = this.getDamage() & 3;
        if (n == 2 || n == 0) {
            return new AxisAlignedBB(this.x, this.y, this.z + 0.375, this.x + 1.0, this.y + 1.5, this.z + 0.625);
        }
        return new AxisAlignedBB(this.x + 0.375, this.y, this.z, this.x + 0.625, this.y + 1.5, this.z + 1.0);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(player != null ? player.getDirection().getHorizontalIndex() : 0);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player == null) {
            return false;
        }
        if (!this.toggle(player)) {
            return false;
        }
        this.getLevel().setBlock(this, this, true);
        if (this.isOpen()) {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_OPEN);
        } else {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_CLOSE);
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    public boolean toggle(Player player) {
        int n;
        DoorToggleEvent doorToggleEvent = new DoorToggleEvent(this, player);
        this.getLevel().getServer().getPluginManager().callEvent(doorToggleEvent);
        if (doorToggleEvent.isCancelled()) {
            return false;
        }
        player = doorToggleEvent.getPlayer();
        if (player != null) {
            int n2;
            double d2 = player.yaw;
            double d3 = (d2 - 90.0) % 360.0;
            if (d3 < 0.0) {
                d3 += 360.0;
            }
            n = (n2 = this.getDamage() & 1) == 0 ? (d3 >= 0.0 && d3 < 180.0 ? 2 : 0) : (d3 >= 90.0 && d3 < 270.0 ? 3 : 1);
        } else {
            int n3 = this.getDamage() & 1;
            n = n3 == 0 ? 0 : 1;
        }
        this.setDamage(n | ~this.getDamage() & 4);
        this.level.setBlock(this, this, true, false);
        if (this.isOpen()) {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_OPEN);
        } else {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_CLOSE);
        }
        return true;
    }

    public boolean isOpen() {
        return (this.getDamage() & 4) > 0;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 6 && (!this.isOpen() && this.level.isBlockPowered(this.getLocation()) || this.isOpen() && !this.level.isBlockPowered(this.getLocation()))) {
            this.toggle(null);
            return n;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return Item.get(107, 0, 1);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean canPassThrough() {
        return this.isOpen();
    }
}

