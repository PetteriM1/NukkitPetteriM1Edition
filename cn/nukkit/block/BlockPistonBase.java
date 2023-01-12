/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockPistonHead;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityPistonArm;
import cn.nukkit.event.block.BlockPistonChangeEvent;
import cn.nukkit.event.block.BlockPistonEvent;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Faceable;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockPistonBase
extends BlockSolidMeta
implements Faceable {
    public boolean sticky;

    public BlockPistonBase() {
        this(0);
    }

    public BlockPistonBase(int n) {
        super(n);
    }

    public abstract int getPistonHeadBlockId();

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (Math.abs((double)player.getFloorX() - this.x) < 2.0 && Math.abs((double)player.getFloorZ() - this.z) < 2.0) {
            double d5 = player.y + (double)player.getEyeHeight();
            if (d5 - this.y > 2.0) {
                this.setDamage(BlockFace.UP.getIndex());
            } else if (this.y - d5 > 0.0) {
                this.setDamage(BlockFace.DOWN.getIndex());
            } else {
                this.setDamage(player.getHorizontalFacing().getIndex());
            }
        } else {
            this.setDamage(player.getHorizontalFacing().getIndex());
        }
        this.level.setBlock(block, this, true, false);
        CompoundTag compoundTag = new CompoundTag("").putString("id", "PistonArm").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putBoolean("Sticky", this.sticky);
        BlockEntityPistonArm blockEntityPistonArm = (BlockEntityPistonArm)BlockEntity.createBlockEntity("PistonArm", this.getChunk(), compoundTag, new Object[0]);
        blockEntityPistonArm.sticky = this.sticky;
        blockEntityPistonArm.spawnToAll();
        this.b();
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(0), true, true);
        Block block = this.getSide(this.getFacing());
        if (block instanceof BlockPistonHead && ((BlockPistonHead)block).getBlockFace() == this.getFacing()) {
            block.onBreak(item);
        }
        return true;
    }

    public boolean isExtended() {
        BlockFace blockFace = this.getFacing();
        Block block = this.getSide(blockFace);
        return block instanceof BlockPistonHead && ((BlockPistonHead)block).getBlockFace() == blockFace;
    }

    @Override
    public int onUpdate(int n) {
        if (n != 6 && n != 1) {
            return 0;
        }
        if (n == 6) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
        }
        this.b();
        return n;
    }

    private void b() {
        BlockFace blockFace = this.getFacing();
        boolean bl = this.a();
        boolean bl2 = this.isExtended();
        if (bl && !bl2) {
            BlocksCalculator blocksCalculator = new BlocksCalculator(this, blockFace, true);
            if (blocksCalculator.canMove()) {
                if (!this.a(true, blocksCalculator)) {
                    return;
                }
                this.a(true);
                this.getLevel().addLevelSoundEvent(this, 84);
            }
            return;
        }
        if (!bl && bl2) {
            if (this.sticky) {
                Position position = this.add(blockFace.getXOffset() << 1, blockFace.getYOffset() << 1, blockFace.getZOffset() << 1);
                Block block = this.level.getBlock(position);
                if (block.getId() == 0) {
                    this.level.setBlock(this.getLocation().getSide(blockFace), Block.get(0), true, true);
                }
                if (BlockPistonBase.canPush(block, blockFace.getOpposite(), false) && (!(block instanceof BlockFlowable) || block.getId() == 33 || block.getId() == 29) && this.a(false, null)) {
                    this.a(false);
                }
            } else {
                this.a(false);
                this.level.setBlock(this.getLocation().getSide(blockFace), Block.get(0), true, false);
            }
            this.getLevel().addLevelSoundEvent(this, 83);
        }
    }

    public BlockFace getFacing() {
        BlockFace blockFace = BlockFace.fromIndex(this.getDamage()).getOpposite();
        if (blockFace == BlockFace.UP) {
            return BlockFace.DOWN;
        }
        if (blockFace == BlockFace.DOWN) {
            return BlockFace.UP;
        }
        return blockFace;
    }

    private boolean a() {
        BlockFace blockFace = this.getFacing();
        if (blockFace == BlockFace.UP) {
            blockFace = BlockFace.DOWN;
        }
        if (blockFace == BlockFace.DOWN) {
            blockFace = BlockFace.UP;
        }
        for (BlockFace blockFace2 : BlockFace.values()) {
            if (blockFace2 == blockFace || !this.level.isSidePowered(this.getLocation().getSide(blockFace2), blockFace2)) continue;
            return true;
        }
        if (this.level.isSidePowered(this, BlockFace.DOWN)) {
            return true;
        }
        Vector3 vector3 = this.getLocation().up();
        for (BlockFace blockFace3 : BlockFace.values()) {
            if (blockFace3 == BlockFace.DOWN || !this.level.isSidePowered(vector3.getSideVec(blockFace3), blockFace3)) continue;
            return true;
        }
        return false;
    }

    private void a(boolean bl) {
        BlockEntityPistonArm blockEntityPistonArm;
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityPistonArm && (blockEntityPistonArm = (BlockEntityPistonArm)blockEntity).isExtended() != bl) {
            this.level.getServer().getPluginManager().callEvent(new BlockPistonChangeEvent(this, bl ? 0 : 15, bl ? 15 : 0));
            blockEntityPistonArm.setExtended(bl);
            blockEntityPistonArm.broadcastMove();
            if (blockEntityPistonArm.chunk != null) {
                blockEntityPistonArm.chunk.setChanged();
            }
        }
    }

    private boolean a(boolean bl, BlocksCalculator blocksCalculator) {
        Location location = this.getLocation();
        BlockFace blockFace = this.getFacing();
        if (!bl) {
            this.level.setBlock(location.getSideVec(blockFace), Block.get(0), true, false);
        }
        if (blocksCalculator == null) {
            blocksCalculator = new BlocksCalculator(this, blockFace, bl);
        }
        if (blocksCalculator.canMove()) {
            Block block;
            int n;
            BlockPistonEvent blockPistonEvent = new BlockPistonEvent(this, blockFace, blocksCalculator.getBlocksToMove(), blocksCalculator.getBlocksToDestroy(), bl);
            this.level.getServer().getPluginManager().callEvent(blockPistonEvent);
            if (blockPistonEvent.isCancelled()) {
                return false;
            }
            List<Block> list = blocksCalculator.getBlocksToMove();
            if (!bl && list.isEmpty()) {
                this.level.setBlock(location.getSideVec(blockFace), Block.get(0), false, true);
                return true;
            }
            ArrayList<Block> arrayList = new ArrayList<Block>(list);
            List<Block> list2 = blocksCalculator.getBlocksToDestroy();
            BlockFace blockFace2 = bl ? blockFace : blockFace.getOpposite();
            for (n = list2.size() - 1; n >= 0; --n) {
                block = list2.get(n);
                this.level.useBreakOn(block);
            }
            for (n = list.size() - 1; n >= 0; --n) {
                block = list.get(n);
                this.level.setBlock(block, Block.get(0), true, false);
                Position position = block.getLocation().getSide(blockFace2);
                this.level.setBlock(position, (Block)arrayList.get(n), true, false);
            }
            if (bl) {
                Vector3 vector3 = location.getSideVec(blockFace);
                this.level.setBlock(vector3, Block.get(this.getPistonHeadBlockId(), this.getDamage()), true, false);
            }
            return true;
        }
        return false;
    }

    public static boolean canPush(Block block, BlockFace blockFace, boolean bl) {
        if (block.canBePushed() && block.getY() >= 0.0 && (blockFace != BlockFace.DOWN || block.getY() != 0.0) && block.getY() <= 255.0 && (blockFace != BlockFace.UP || block.getY() != 255.0)) {
            if (!(block instanceof BlockPistonBase)) {
                if (block instanceof BlockFlowable || block.breakWhenPushed()) {
                    return bl;
                }
            } else {
                return !((BlockPistonBase)block).isExtended();
            }
            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    public static class BlocksCalculator {
        private final Vector3 e;
        private final Block c;
        private final BlockFace a;
        private final List<Block> b = new ArrayList<Block>();
        private final List<Block> d = new ArrayList<Block>();
        protected Boolean canMove;

        public BlocksCalculator(Block block, BlockFace blockFace, boolean bl) {
            this.e = block.getLocation();
            if (bl) {
                this.a = blockFace;
                this.c = block.getSide(blockFace);
            } else {
                this.a = blockFace.getOpposite();
                this.c = block.getSide(blockFace, 2);
            }
        }

        public boolean canMove() {
            return this.canMove == null ? (this.canMove = Boolean.valueOf(this.a())) : this.canMove;
        }

        private boolean a() {
            this.b.clear();
            this.d.clear();
            Block block = this.c;
            if (!BlockPistonBase.canPush(block, this.a, false)) {
                if (block instanceof BlockFlowable || block.breakWhenPushed()) {
                    boolean bl = false;
                    for (Block block2 : this.d) {
                        if (block2.x != this.c.x || block2.y != this.c.y || block2.z != this.c.z) continue;
                        bl = true;
                        break;
                    }
                    if (!bl) {
                        this.d.add(this.c);
                    }
                    return true;
                }
                return false;
            }
            return this.a(this.c);
        }

        private boolean a(Block block) {
            int n;
            Block block2 = block.clone();
            if (block2.getId() == 0) {
                return true;
            }
            if (!BlockPistonBase.canPush(block, this.a, false)) {
                return true;
            }
            if (block.equals(this.e)) {
                return true;
            }
            if (this.b.contains(block)) {
                return true;
            }
            int n2 = 1;
            if (n2 + this.b.size() > 12) {
                return false;
            }
            int n3 = 0;
            for (n = n2 - 1; n >= 0; --n) {
                this.b.add(block2.getSide(this.a.getOpposite(), n));
                ++n3;
            }
            n = 1;
            while (true) {
                Block block3;
                int n4;
                if ((n4 = this.b.indexOf(block3 = block2.getSide(this.a, n))) > -1) {
                    this.a(n3, n4);
                    for (int k = 0; k <= n4 + n3; ++k) {
                        Block block4 = this.b.get(k);
                    }
                    return true;
                }
                if (block3.getId() == 0) {
                    return true;
                }
                if (!BlockPistonBase.canPush(block3, this.a, true) || block3.equals(this.e)) {
                    return false;
                }
                if (block3 instanceof BlockFlowable || block2.breakWhenPushed()) {
                    boolean bl = false;
                    for (Block block5 : this.d) {
                        if (block5.x != block3.x || block5.y != block3.y || block5.z != block3.z) continue;
                        bl = true;
                        break;
                    }
                    if (!bl) {
                        this.d.add(block3);
                    }
                    return true;
                }
                if (this.b.size() >= 12) {
                    return false;
                }
                this.b.add(block3);
                ++n3;
                ++n;
            }
        }

        private void a(int n, int n2) {
            ArrayList<Block> arrayList = new ArrayList<Block>(this.b.subList(0, n2));
            ArrayList<Block> arrayList2 = new ArrayList<Block>(this.b.subList(this.b.size() - n, this.b.size()));
            ArrayList<Block> arrayList3 = new ArrayList<Block>(this.b.subList(n2, this.b.size() - n));
            this.b.clear();
            this.b.addAll(arrayList);
            this.b.addAll(arrayList2);
            this.b.addAll(arrayList3);
        }

        private boolean b(Block block) {
            for (BlockFace blockFace : BlockFace.values()) {
                if (blockFace.getAxis() == this.a.getAxis() || this.a(block.getSide(blockFace))) continue;
                return false;
            }
            return true;
        }

        public List<Block> getBlocksToMove() {
            return this.b;
        }

        public List<Block> getBlocksToDestroy() {
            return this.d;
        }
    }
}

