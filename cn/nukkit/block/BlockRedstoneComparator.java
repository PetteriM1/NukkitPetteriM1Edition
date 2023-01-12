/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityComparator;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;

public abstract class BlockRedstoneComparator
extends BlockRedstoneDiode {
    public BlockRedstoneComparator() {
        this(0);
    }

    public BlockRedstoneComparator(int n) {
        super(n);
    }

    @Override
    protected int getDelay() {
        return 2;
    }

    @Override
    public BlockFace getFacing() {
        return BlockFace.fromHorizontalIndex(this.getDamage());
    }

    public Mode getMode() {
        return (this.getDamage() & 4) > 0 ? Mode.SUBTRACT : Mode.COMPARE;
    }

    @Override
    protected BlockRedstoneComparator getUnpowered() {
        return (BlockRedstoneComparator)Block.get(149, this.getDamage());
    }

    @Override
    protected BlockRedstoneComparator getPowered() {
        return (BlockRedstoneComparator)Block.get(150, this.getDamage());
    }

    @Override
    protected int getRedstoneSignal() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        return blockEntity instanceof BlockEntityComparator ? ((BlockEntityComparator)blockEntity).getOutputSignal() : 0;
    }

    @Override
    public void updateState() {
        if (!this.level.isBlockTickPending(this, this)) {
            int n;
            int n2 = this.b();
            BlockEntity blockEntity = this.level.getBlockEntity(this);
            int n3 = n = blockEntity instanceof BlockEntityComparator ? ((BlockEntityComparator)blockEntity).getOutputSignal() : 0;
            if (n2 != n || this.isPowered() != this.shouldBePowered()) {
                this.level.scheduleUpdate(this, this, 2);
            }
        }
    }

    @Override
    protected int calculateInputStrength() {
        int n = super.calculateInputStrength();
        BlockFace blockFace = this.getFacing();
        Block block = this.getSide(blockFace);
        if (block.hasComparatorInputOverride()) {
            n = block.getComparatorInputOverride();
        } else if (n < 15 && block.isNormalBlock() && (block = block.getSide(blockFace)).hasComparatorInputOverride()) {
            n = block.getComparatorInputOverride();
        }
        return n;
    }

    @Override
    protected boolean shouldBePowered() {
        int n = this.calculateInputStrength();
        if (n >= 15) {
            return true;
        }
        if (n == 0) {
            return false;
        }
        int n2 = this.getPowerOnSides();
        return n2 == 0 || n >= n2;
    }

    private int b() {
        return this.getMode() == Mode.SUBTRACT ? Math.max(this.calculateInputStrength() - this.getPowerOnSides(), 0) : this.calculateInputStrength();
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (this.getMode() == Mode.SUBTRACT) {
            this.setDamage(this.getDamage() - 4);
        } else {
            this.setDamage(this.getDamage() + 4);
        }
        if (this.getMode() == Mode.SUBTRACT) {
            this.level.addLevelSoundEvent(this, 73);
        } else {
            this.level.addLevelSoundEvent(this, 74);
        }
        this.level.setBlock(this, this, true, false);
        this.a();
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            this.a();
            return n;
        }
        return super.onUpdate(n);
    }

    private void a() {
        int n = this.b();
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        int n2 = 0;
        if (blockEntity instanceof BlockEntityComparator) {
            BlockEntityComparator blockEntityComparator = (BlockEntityComparator)blockEntity;
            n2 = blockEntityComparator.getOutputSignal();
            blockEntityComparator.setOutputSignal(n);
        }
        if (n2 != n || this.getMode() == Mode.COMPARE) {
            boolean bl = this.shouldBePowered();
            boolean bl2 = this.isPowered();
            if (bl2 && !bl) {
                this.level.setBlock(this, this.getUnpowered(), true, false);
            } else if (!bl2 && bl) {
                this.level.setBlock(this, this.getPowered(), true, false);
            }
            this.level.updateAroundRedstone(this, null);
        }
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (super.place(item, block, block2, blockFace, d2, d3, d4, player)) {
            CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "Comparator").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
            BlockEntity.createBlockEntity("Comparator", this.getChunk(), compoundTag, new Object[0]);
            this.onUpdate(6);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPowered() {
        return this.isPowered || (this.getDamage() & 8) > 0;
    }

    @Override
    public Item toItem() {
        return Item.get(404);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    public static enum Mode {
        COMPARE,
        SUBTRACT;

    }
}

