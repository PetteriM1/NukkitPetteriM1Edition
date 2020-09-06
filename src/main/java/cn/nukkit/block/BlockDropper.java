package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityDispenser;
import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDropper extends BlockSolidMeta implements Faceable {

    protected boolean triggered = false;

    public BlockDropper() {
        this(0);
    }

    public BlockDropper(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DROPPER;
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 17.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (player != null) {
            if (Math.abs(player.x - this.x) < 2 && Math.abs(player.z - this.z) < 2) {
                double y = player.y + player.getEyeHeight();

                if (y - this.y > 2) {
                    this.setDamage(BlockFace.UP.getIndex());
                } else if (this.y - y > 0) {
                    this.setDamage(BlockFace.DOWN.getIndex());
                } else {
                    this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
                }
            } else {
                this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
            }
        }

        this.getLevel().setBlock(block, this, true);

        BlockEntity.createBlockEntity(BlockEntity.DROPPER, this.getChunk(), BlockEntity.getDefaultCompound(this, BlockEntity.DROPPER));
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player == null) {
            return false;
        }

        BlockEntity blockEntity = this.level.getBlockEntity(this);

        if (!(blockEntity instanceof BlockEntityDropper)) {
            return false;
        }

        player.addWindow(((BlockEntityDropper) blockEntity).getInventory());
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    public Vector3 getDispensePosition() {
        BlockFace facing = getBlockFace();
        return this.add(
                0.5 + 0.7 * facing.getXOffset(),
                0.5 + 0.7 * facing.getYOffset(),
                0.5 + 0.7 * facing.getZOffset()
        );
    }

    public void dispense() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);

        if (!(blockEntity instanceof BlockEntityDropper)) {
            return;
        }

        int r = 1;
        int slot = -1;
        Item target = null;

        Inventory inv = ((BlockEntityDropper) blockEntity).getInventory();
        for (Map.Entry<Integer, Item> entry : inv.getContents().entrySet()) {
            Item item = entry.getValue();

            if (!item.isNull() && Utils.random.nextInt(r++) == 0) {
                target = item;
                slot = entry.getKey();
            }
        }

        if (target != null) {
            target = target.clone();
            drop(target);

            target.count--;
            inv.setItem(slot, target);
        }
    }

    public void drop(Item item) {
        BlockFace face = this.getBlockFace();
        Vector3 dispensePos = this.getDispensePosition();

        if (face.getAxis() == BlockFace.Axis.Y) {
            dispensePos.y -= 0.125;
        } else {
            dispensePos.y -= 0.15625;
        }

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        Vector3 motion = new Vector3();

        double offset = rand.nextDouble() * 0.1 + 0.2;

        motion.x = face.getXOffset() * offset;
        motion.y = 0.1;
        motion.z = face.getZOffset() * offset;

        motion.x += rand.nextGaussian() * 0.007499999832361937 * 6;
        motion.y += rand.nextGaussian() * 0.007499999832361937 * 6;
        motion.z += rand.nextGaussian() * 0.007499999832361937 * 6;

        Item i = item.clone();
        i.setCount(1);
        this.level.dropItem(dispensePos, i, motion);
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);

        if (blockEntity instanceof BlockEntityDropper) {
            return ContainerInventory.calculateRedstone(((BlockEntityDropper) blockEntity).getInventory());
        }

        return 0;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            triggered = false;
            dispense();

            return type;
        } else if (type == Level.BLOCK_UPDATE_REDSTONE) {
            if ((level.isBlockPowered(this) || level.isBlockPowered(this.up())) && !triggered) {
                triggered = true;
                level.scheduleUpdate(this, this, 4);
            }

            return type;
        }

        return 0;
    }
}
