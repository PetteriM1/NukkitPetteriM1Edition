package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBed;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBed;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockBed extends BlockTransparentMeta implements Faceable {

    public BlockBed() {
        this(0);
    }

    public BlockBed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BED_BLOCK;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    public boolean sticksToPiston() {
        return false;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Bed Block";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new SimpleAxisAlignedBB(
                this.x,
                this.y,
                this.z,
                this.x + 1,
                this.y + 0.5625,
                this.z + 1
        );
    }

    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (this.level.getDimension() != Level.DIMENSION_OVERWORLD) {
            this.level.useBreakOn(this);
            CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos")
                            .add(new DoubleTag("", x))
                            .add(new DoubleTag("", y))
                            .add(new DoubleTag("", z)))
                    .putList(new ListTag<DoubleTag>("Motion")
                            .add(new DoubleTag("", 0))
                            .add(new DoubleTag("", 0))
                            .add(new DoubleTag("", 0)))
                    .putList(new ListTag<FloatTag>("Rotation")
                            .add(new FloatTag("", 0))
                            .add(new FloatTag("", 0)))
                    .putShort("Fuse", 0); // create an instant explosion
            new EntityPrimedTNT(this.level.getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), nbt); // we don't even have to spawn the tnt entity for players
            return true;
        }

        int time = this.getLevel().getTime() % Level.TIME_FULL;

        boolean isNight = (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE);

        if (player != null && !isNight) {
            player.sendTranslation("§7%tile.bed.noSleep");
            return true;
        }

        Block blockNorth = this.north();
        Block blockSouth = this.south();
        Block blockEast = this.east();
        Block blockWest = this.west();

        Block b;
        if ((this.getDamage() & 0x08) == 0x08) {
            b = this;
        } else {
            if (blockNorth.getId() == BED_BLOCK && (blockNorth.getDamage() & 0x08) == 0x08) {
                b = blockNorth;
            } else if (blockSouth.getId() == BED_BLOCK && (blockSouth.getDamage() & 0x08) == 0x08) {
                b = blockSouth;
            } else if (blockEast.getId() == BED_BLOCK && (blockEast.getDamage() & 0x08) == 0x08) {
                b = blockEast;
            } else if (blockWest.getId() == BED_BLOCK && (blockWest.getDamage() & 0x08) == 0x08) {
                b = blockWest;
            } else {
                if (player != null) {
                    player.sendTranslation("§7%tile.bed.notValid");
                }

                return true;
            }
        }

        if (player != null) {
            if (player.distanceSquared(this) > 36) {
                player.sendTranslation("§7%tile.bed.tooFar");
                return true;
            }

            if (!player.isCreative()) {
                BlockFace secondPart = this.getBlockFace().getOpposite();
                AxisAlignedBB checkArea = new SimpleAxisAlignedBB(b.x - 8, b.y - 6.5, b.z - 8, b.x + 9, b.y + 5.5, b.z + 9).addCoord(secondPart.getXOffset(), 0, secondPart.getZOffset());

                for (Entity entity : this.getLevel().getCollidingEntities(checkArea)) {
                    if (!entity.isClosed() && Utils.monstersList.contains(entity.getNetworkId())) {
                        player.sendTranslation("§7%tile.bed.notSafe");
                        return true;
                    }
                }
            }

            if (!player.sleepOn(b)) {
                player.sendTranslation("§7%tile.bed.occupied");
            }
        }

        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = this.down();
        if (!down.isTransparent() || down instanceof BlockSlab) {
            Block next = this.getSide(player.getHorizontalFacing());
            Block downNext = next.down();

            if (next.canBeReplaced() && (!downNext.isTransparent() || downNext instanceof BlockSlab)) {
                int meta = player.getDirection().getHorizontalIndex();

                this.getLevel().setBlock(block, Block.get(BED_BLOCK, meta), true, true);
                this.getLevel().setBlock(next, Block.get(BED_BLOCK, meta | 0x08), true, true);

                // HACK: Make bed color to update for the player
                Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                    createBlockEntity(this, item.getDamage());
                    createBlockEntity(next, item.getDamage());
                }, 2);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        Block blockNorth = this.north();
        Block blockSouth = this.south();
        Block blockEast = this.east();
        Block blockWest = this.west();

        Block secondPart = null;
        if ((this.getDamage() & 0x08) == 0x08) { // Top part of the bed
            if (blockNorth.getId() == BED_BLOCK && (blockNorth.getDamage() & 0x08) != 0x08) { // Check if the block ID & meta are right
                secondPart = blockNorth;
            } else if (blockSouth.getId() == BED_BLOCK && (blockSouth.getDamage() & 0x08) != 0x08) {
                secondPart = blockSouth;
            } else if (blockEast.getId() == BED_BLOCK && (blockEast.getDamage() & 0x08) != 0x08) {
                secondPart = blockEast;
            } else if (blockWest.getId() == BED_BLOCK && (blockWest.getDamage() & 0x08) != 0x08) {
                secondPart = blockWest;
            }
        } else { // Bottom part of the bed
            if (blockNorth.getId() == BED_BLOCK && (blockNorth.getDamage() & 0x08) == 0x08) {
                secondPart = blockNorth;
            } else if (blockSouth.getId() == BED_BLOCK && (blockSouth.getDamage() & 0x08) == 0x08) {
                secondPart = blockSouth;
            } else if (blockEast.getId() == BED_BLOCK && (blockEast.getDamage() & 0x08) == 0x08) {
                secondPart = blockEast;
            } else if (blockWest.getId() == BED_BLOCK && (blockWest.getDamage() & 0x08) == 0x08) {
                secondPart = blockWest;
            }
        }

        if (secondPart != null) {
            this.getLevel().setBlock(secondPart, Block.get(BlockID.AIR), true, true);
        }

        this.getLevel().setBlock(this, Block.get(BlockID.AIR), true, false); // Don't update both parts to prevent duplication bug if there are two fallable blocks on top of the bed

        for (Entity entity : this.level.getNearbyEntities(new SimpleAxisAlignedBB(this, this).grow(2, 1, 2))) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;

            if (player.getSleepingPos() == null) continue;
            if (!player.getSleepingPos().equals(this) && !player.getSleepingPos().equals(secondPart)) continue;
            player.stopSleep();
        }

        return true;
    }

    private void createBlockEntity(Vector3 pos, int color) {
        CompoundTag nbt = BlockEntity.getDefaultCompound(pos, BlockEntity.BED);
        nbt.putByte("color", color);

        BlockEntity.createBlockEntity(BlockEntity.BED, this.level.getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4), nbt);
    }

    @Override
    public Item toItem() {
        return new ItemBed(this.getDyeColor().getWoolData());
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        if (this.level != null) {
            BlockEntity blockEntity = this.level.getBlockEntity(this);

            if (blockEntity instanceof BlockEntityBed) {
                return ((BlockEntityBed) blockEntity).getDyeColor();
            }
        }

        return DyeColor.WHITE;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
