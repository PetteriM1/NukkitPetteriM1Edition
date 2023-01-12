/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCauldron;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockBell
extends BlockTransparentMeta
implements Faceable {
    public static final int TYPE_ATTACHMENT_STANDING = 0;
    public static final int TYPE_ATTACHMENT_HANGING = 1;
    public static final int TYPE_ATTACHMENT_SIDE = 2;
    public static final int TYPE_ATTACHMENT_MULTIPLE = 3;

    public BlockBell() {
        this(0);
    }

    public BlockBell(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Bell";
    }

    @Override
    public int getId() {
        return 461;
    }

    private boolean a(BlockFace blockFace, int n, BlockFace blockFace2) {
        BlockFace.Axis axis = blockFace.getAxis();
        switch (n) {
            case 0: {
                if (axis == BlockFace.Axis.Y) {
                    return blockFace == BlockFace.DOWN;
                }
                return blockFace2.getAxis() != axis;
            }
            case 1: {
                return blockFace == BlockFace.UP;
            }
            case 2: {
                return blockFace == blockFace2.getOpposite();
            }
            case 3: {
                return blockFace == blockFace2 || blockFace == blockFace2.getOpposite();
            }
        }
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        int n = this.getAttachmentType();
        BlockFace blockFace = this.getBlockFace();
        boolean bl = this.a(BlockFace.NORTH, n, blockFace);
        boolean bl2 = this.a(BlockFace.SOUTH, n, blockFace);
        boolean bl3 = this.a(BlockFace.WEST, n, blockFace);
        boolean bl4 = this.a(BlockFace.EAST, n, blockFace);
        boolean bl5 = this.a(BlockFace.UP, n, blockFace);
        boolean bl6 = this.a(BlockFace.DOWN, n, blockFace);
        double d2 = bl ? 0.0 : 0.25;
        double d3 = bl2 ? 1.0 : 0.75;
        double d4 = bl3 ? 0.0 : 0.25;
        double d5 = bl4 ? 1.0 : 0.75;
        double d6 = bl6 ? 0.0 : 0.25;
        double d7 = bl5 ? 1.0 : 0.75;
        return new AxisAlignedBB(this.x + d4, this.y + d6, this.z + d2, this.x + d5, this.y + d7, this.z + d3);
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return this.recalculateBoundingBox().expand(1.0E-6, 1.0E-6, 1.0E-6);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return this.ring(player);
    }

    public boolean ring(Entity entity) {
        BlockFace blockFace;
        BlockFace blockFace2 = this.getBlockFace();
        if (entity != null) {
            if (entity instanceof EntityItem) {
                int n;
                int n2;
                Position position = this.add(0.5, 0.5, 0.5);
                Vector3 vector3 = entity.subtract(position).normalize();
                int n3 = vector3.x < 0.0 ? -1 : (n2 = vector3.x > 0.0 ? 1 : 0);
                int n4 = vector3.z < 0.0 ? -1 : (n = vector3.z > 0.0 ? 1 : 0);
                if (n2 != 0 && n != 0) {
                    if (Math.abs(vector3.x) < Math.abs(vector3.z)) {
                        n2 = 0;
                    } else {
                        n = 0;
                    }
                }
                blockFace = blockFace2;
                for (BlockFace blockFace3 : BlockFace.values()) {
                    if (blockFace3.getXOffset() != n2 || blockFace3.getZOffset() != n) continue;
                    blockFace = blockFace3;
                    break;
                }
            } else {
                blockFace = entity.getDirection();
            }
        } else {
            blockFace = blockFace2;
        }
        switch (this.getAttachmentType()) {
            case 0: {
                if (blockFace.getAxis() == blockFace2.getAxis()) break;
                return false;
            }
            case 3: {
                if (blockFace.getAxis() != blockFace2.getAxis()) break;
                return false;
            }
            case 2: {
                if (blockFace.getAxis() != blockFace2.getAxis()) break;
            }
        }
        this.getLevel().addSound((Vector3)this, Sound.BLOCK_BELL_HIT);
        return true;
    }

    private boolean a() {
        switch (this.getAttachmentType()) {
            case 0: {
                if (!this.a(this.down(), BlockFace.UP)) break;
                return true;
            }
            case 1: {
                if (!this.a(this.up(), BlockFace.DOWN)) break;
                return true;
            }
            case 3: {
                BlockFace blockFace = this.getBlockFace();
                if (!this.a(this.getSide(blockFace), blockFace.getOpposite()) || !this.a(this.getSide(blockFace.getOpposite()), blockFace)) break;
                return true;
            }
            case 2: {
                BlockFace blockFace = this.getBlockFace();
                if (!this.a(this.getSide(blockFace.getOpposite()), blockFace)) break;
                return true;
            }
        }
        return false;
    }

    private boolean a(Block block, BlockFace blockFace) {
        if (!block.isTransparent()) {
            return true;
        }
        if (blockFace == BlockFace.DOWN) {
            switch (block.getId()) {
                case 101: 
                case 154: {
                    return true;
                }
            }
            return block instanceof BlockFence;
        }
        if (block instanceof BlockCauldron) {
            return blockFace == BlockFace.UP;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (!this.a()) {
                this.level.useBreakOn(this);
            }
            return n;
        }
        if (n == 6) {
            this.ring(null);
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block.canBeReplaced() && block.getId() != 0 && !(block instanceof BlockLiquid)) {
            blockFace = BlockFace.UP;
        }
        switch (blockFace) {
            case UP: {
                this.setAttachmentType(0);
                this.setBlockFace(player.getDirection().getOpposite());
                break;
            }
            case DOWN: {
                this.setAttachmentType(1);
                this.setBlockFace(player.getDirection().getOpposite());
                break;
            }
            default: {
                this.setBlockFace(blockFace);
                if (this.a(block.getSide(blockFace), blockFace.getOpposite())) {
                    this.setAttachmentType(3);
                    break;
                }
                this.setAttachmentType(2);
            }
        }
        if (!this.a()) {
            return false;
        }
        this.level.setBlock(this, this, true, true);
        BlockEntity.createBlockEntity("Bell", this.getChunk(), BlockEntity.getDefaultCompound(this, "Bell"), new Object[0]);
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    public void setBlockFace(BlockFace blockFace) {
        if (blockFace.getHorizontalIndex() == -1) {
            return;
        }
        this.setDamage(this.getDamage() & 0xC | blockFace.getHorizontalIndex());
    }

    public int getAttachmentType() {
        return (this.getDamage() & 0xC) >> 2 & 3;
    }

    public void setAttachmentType(int n) {
        this.setDamage(this.getDamage() & 3 | (n &= 3) << 2);
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public double getResistance() {
        return 25.0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GOLD_BLOCK_COLOR;
    }
}

