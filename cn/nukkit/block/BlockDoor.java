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
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Faceable;

public abstract class BlockDoor
extends BlockTransparentMeta
implements Faceable {
    public static int DOOR_OPEN_BIT = 4;
    public static int DOOR_TOP_BIT = 8;
    public static int DOOR_HINGE_BIT = 1;
    public static int DOOR_POWERED_BIT = 2;
    private static final int[] d = new int[]{1, 2, 3, 0};

    protected BlockDoor(int n) {
        super(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    private int a() {
        int n;
        int n2;
        if (this.isTop()) {
            n2 = this.down().getDamage();
            n = this.getDamage();
        } else {
            n2 = this.getDamage();
            n = this.up().getDamage();
        }
        boolean bl = (n & DOOR_HINGE_BIT) > 0;
        return n2 & 7 | (this.isTop() ? 8 : 0) | (bl ? 16 : 0);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        boolean bl;
        double d2 = 0.1875;
        int n = this.a();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 2.0, this.z + 1.0);
        int n2 = n & 3;
        boolean bl2 = (n & 4) > 0;
        boolean bl3 = bl = (n & 0x10) > 0;
        if (n2 == 0) {
            if (bl2) {
                if (!bl) {
                    axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + d2);
                } else {
                    axisAlignedBB.setBounds(this.x, this.y, this.z + 1.0 - d2, this.x + 1.0, this.y + 1.0, this.z + 1.0);
                }
            } else {
                axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + d2, this.y + 1.0, this.z + 1.0);
            }
        } else if (n2 == 1) {
            if (bl2) {
                if (!bl) {
                    axisAlignedBB.setBounds(this.x + 1.0 - d2, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
                } else {
                    axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + d2, this.y + 1.0, this.z + 1.0);
                }
            } else {
                axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + d2);
            }
        } else if (n2 == 2) {
            if (bl2) {
                if (!bl) {
                    axisAlignedBB.setBounds(this.x, this.y, this.z + 1.0 - d2, this.x + 1.0, this.y + 1.0, this.z + 1.0);
                } else {
                    axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + d2);
                }
            } else {
                axisAlignedBB.setBounds(this.x + 1.0 - d2, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
            }
        } else if (n2 == 3) {
            if (bl2) {
                if (!bl) {
                    axisAlignedBB.setBounds(this.x, this.y, this.z, this.x + d2, this.y + 1.0, this.z + 1.0);
                } else {
                    axisAlignedBB.setBounds(this.x + 1.0 - d2, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
                }
            } else {
                axisAlignedBB.setBounds(this.x, this.y, this.z + 1.0 - d2, this.x + 1.0, this.y + 1.0, this.z + 1.0);
            }
        }
        return axisAlignedBB;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().getId() == 0) {
            Block block = this.up();
            if (block instanceof BlockDoor) {
                this.getLevel().setBlock(block, Block.get(0), false);
                this.getLevel().useBreakOn(this, Item.get(270));
            }
            return 1;
        }
        if (n == 6 && (!this.isOpen() && this.level.isBlockPowered(this.getLocation()) || this.isOpen() && !this.level.isBlockPowered(this.getLocation()))) {
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, this.isOpen() ? 15 : 0, this.isOpen() ? 0 : 15));
            this.toggle(null);
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.y > 254.0) {
            return false;
        }
        if (blockFace == BlockFace.UP) {
            Block block3 = this.up();
            if (!block3.canBeReplaced() || !BlockDoor.canStayOnFullNonSolid(this.down())) {
                return false;
            }
            int n = d[player != null ? player.getDirection().getHorizontalIndex() : 0];
            Block block4 = this.getSide(player.getDirection().rotateYCCW());
            Block block5 = this.getSide(player.getDirection().rotateY());
            int n2 = DOOR_TOP_BIT;
            if (block4.getId() == this.getId() || !block5.isTransparent() && block4.isTransparent()) {
                n2 |= DOOR_HINGE_BIT;
            }
            this.setDamage(n);
            this.getLevel().setBlock(block, this, true, true);
            this.getLevel().setBlock(block3, Block.get(this.getId(), n2), true);
            if (!this.isOpen() && this.level.isBlockPowered(this.getLocation())) {
                this.toggle(null);
                this.getLevel().setBlockDataAt(block3.getFloorX(), block3.getFloorY(), block3.getFloorZ(), n2 |= DOOR_POWERED_BIT);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        if (this.isTop(this.getDamage())) {
            Block block = this.down();
            if (block.getId() == this.getId()) {
                this.getLevel().setBlock(block, Block.get(0), true);
            }
        } else {
            Block block = this.up();
            if (block.getId() == this.getId()) {
                this.getLevel().setBlock(block, Block.get(0), true);
            }
        }
        this.getLevel().setBlock(this, Block.get(0), true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return this.toggle(player);
    }

    public boolean toggle(Player player) {
        Block block;
        Block block2;
        DoorToggleEvent doorToggleEvent = new DoorToggleEvent(this, player);
        this.getLevel().getServer().getPluginManager().callEvent(doorToggleEvent);
        if (doorToggleEvent.isCancelled()) {
            return false;
        }
        if (this.isTop(this.getDamage())) {
            block2 = this.down();
            block = this;
        } else {
            block2 = this;
            block = this.up();
        }
        if (block.getId() != block2.getId()) {
            return false;
        }
        int n = block2.getDamage() ^ 4;
        this.level.setBlockDataAt(block2.getFloorX(), block2.getFloorY(), block2.getFloorZ(), n);
        if (this.a(n)) {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_OPEN);
        } else {
            this.level.addSound((Vector3)this, Sound.RANDOM_DOOR_CLOSE);
        }
        return true;
    }

    private boolean a(int n) {
        if (this.isTop(n)) {
            return (this.down().getDamage() & DOOR_OPEN_BIT) > 0;
        }
        return (n & DOOR_OPEN_BIT) > 0;
    }

    public boolean isOpen() {
        if (this.isTop(this.getDamage())) {
            return (this.down().getDamage() & DOOR_OPEN_BIT) > 0;
        }
        return (this.getDamage() & DOOR_OPEN_BIT) > 0;
    }

    public boolean isTop() {
        return this.isTop(this.getDamage());
    }

    public boolean isTop(int n) {
        return (n & DOOR_TOP_BIT) != 0;
    }

    public boolean isRightHinged() {
        if (this.isTop()) {
            return (this.getDamage() & DOOR_HINGE_BIT) > 0;
        }
        return (this.up().getDamage() & DOOR_HINGE_BIT) > 0;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

