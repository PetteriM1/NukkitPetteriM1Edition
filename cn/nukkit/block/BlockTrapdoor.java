/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.event.block.DoorToggleEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockTrapdoor
extends BlockTransparentMeta
implements Faceable {
    public static int TRAPDOOR_OPEN_BIT = 8;
    public static int TRAPDOOR_TOP_BIT = 4;
    private static final int[] d = new int[]{2, 1, 3, 0};

    public BlockTrapdoor() {
        this(0);
    }

    public BlockTrapdoor(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 96;
    }

    @Override
    public String getName() {
        return "Wooden Trapdoor";
    }

    @Override
    public double getHardness() {
        return 3.0;
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
        int n = this.getDamage();
        double d2 = 0.1875;
        AxisAlignedBB axisAlignedBB = (n & TRAPDOOR_TOP_BIT) > 0 ? new AxisAlignedBB(this.x, this.y + 1.0 - d2, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0) : new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + d2, this.z + 1.0);
        if ((n & TRAPDOOR_OPEN_BIT) > 0) {
            if ((n & 3) == 0) {
                axisAlignedBB.setBounds(this.x, this.y, this.z + 1.0 - d2, this.x + 1.0, this.y + 1.0, this.z + 1.0);
            } else if ((n & 3) == 1) {
                axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + d2);
            }
            if ((n & 3) == 2) {
                axisAlignedBB.setBounds(this.x + 1.0 - d2, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
            }
            if ((n & 3) == 3) {
                axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + d2, this.y + 1.0, this.z + 1.0);
            }
        }
        return axisAlignedBB;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 6 && (!this.isOpen() && this.level.isBlockPowered(this.getLocation()) || this.isOpen() && !this.level.isBlockPowered(this.getLocation()))) {
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, this.isOpen() ? 15 : 0, this.isOpen() ? 0 : 15));
            this.setDamage(this.getDamage() ^ TRAPDOOR_OPEN_BIT);
            this.level.setBlock(this, this, true);
            if (this.isOpen()) {
                this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_OPEN);
            } else {
                this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_CLOSE);
            }
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl;
        BlockFace blockFace2;
        int n = 0;
        if (blockFace.getAxis().isHorizontal() || player == null) {
            blockFace2 = blockFace;
            bl = d3 > 0.5;
        } else {
            blockFace2 = player.getDirection().getOpposite();
            boolean bl2 = bl = blockFace != BlockFace.UP;
        }
        n = bl ? (n |= TRAPDOOR_TOP_BIT) : (n |= d[blockFace2.getHorizontalIndex()]);
        this.setDamage(n);
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return this.toggle(player);
    }

    public boolean toggle(Player player) {
        DoorToggleEvent doorToggleEvent = new DoorToggleEvent(this, player);
        this.level.getServer().getPluginManager().callEvent(doorToggleEvent);
        if (doorToggleEvent.isCancelled()) {
            return false;
        }
        this.setDamage(this.getDamage() ^ TRAPDOOR_OPEN_BIT);
        this.level.setBlock(this, this, true, true);
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

    public boolean isOpen() {
        return (this.getDamage() & TRAPDOOR_OPEN_BIT) != 0;
    }

    public boolean isTop() {
        return (this.getDamage() & TRAPDOOR_TOP_BIT) != 0;
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

