/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBed;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerBedEnterEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;

public class BlockBed
extends BlockTransparentMeta
implements Faceable {
    public boolean canDropItem = true;

    public BlockBed() {
        this(0);
    }

    public BlockBed(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 26;
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
    public String getName() {
        return this.getDyeColor().getName() + " Bed Block";
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5625;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (this.level.getDimension() != 0) {
            if (this.getLevel().setBlock(this, Block.get(0), true, true)) {
                this.level.addParticle(new DestroyBlockParticle(this.add(0.5), this));
            }
            CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", this.x)).add(new DoubleTag("", this.y)).add(new DoubleTag("", this.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putShort("Fuse", 0);
            new EntityPrimedTNT(this.level.getChunk((int)this.x >> 4, (int)this.z >> 4), compoundTag);
            return true;
        }
        Block block2 = this.north();
        Block block3 = this.south();
        Block block4 = this.east();
        Block block5 = this.west();
        if ((this.getDamage() & 8) == 8) {
            block = this;
        } else if (block2.getId() == 26 && (block2.getDamage() & 8) == 8) {
            block = block2;
        } else if (block3.getId() == 26 && (block3.getDamage() & 8) == 8) {
            block = block3;
        } else if (block4.getId() == 26 && (block4.getDamage() & 8) == 8) {
            block = block4;
        } else if (block5.getId() == 26 && (block5.getDamage() & 8) == 8) {
            block = block5;
        } else {
            if (player != null) {
                player.sendMessage("\u00a77%tile.bed.notValid", true);
            }
            return true;
        }
        if (player != null) {
            int n;
            boolean bl;
            if (player.distanceSquared(this) > 36.0) {
                player.sendMessage("\u00a77%tile.bed.tooFar", true);
                return true;
            }
            if (!player.isCreative()) {
                BlockFace blockFace = this.getBlockFace().getOpposite();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(block.x - 8.0, block.y - 6.5, block.z - 8.0, block.x + 9.0, block.y + 5.5, block.z + 9.0).addCoord(blockFace.getXOffset(), 0.0, blockFace.getZOffset());
                for (Entity entity : this.getLevel().getCollidingEntities(axisAlignedBB)) {
                    if (entity.isClosed() || !Utils.monstersList.contains(entity.getNetworkId())) continue;
                    player.sendMessage("\u00a77%tile.bed.notSafe", true);
                    return true;
                }
            }
            boolean bl2 = bl = (n = this.getLevel().getTime() % 24000) >= 14000 && n < 23000;
            if (!bl) {
                if ((player.getServer().bedSpawnpoints || player.getServer().suomiCraftPEMode()) && !player.getSpawn().equals(block)) {
                    Entity[] entityArray = new PlayerBedEnterEvent(player, this, true);
                    player.getServer().getPluginManager().callEvent((Event)entityArray);
                    if (!entityArray.isCancelled()) {
                        player.setSpawn(block);
                        player.sendMessage("\u00a77%tile.bed.respawnSet", true);
                    }
                }
                player.sendMessage("\u00a77%tile.bed.noSleep", true);
            } else if (!player.sleepOn(block)) {
                player.sendMessage("\u00a77%tile.bed.occupied", true);
            }
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3;
        if (BlockBed.canStayOnFullNonSolid(this.down()) && (block3 = this.getSide(player.getHorizontalFacing())).canBeReplaced() && BlockBed.canStayOnFullNonSolid(block3.down())) {
            int n = player.getDirection().getHorizontalIndex();
            this.getLevel().setBlock(block, Block.get(26, n), true, true);
            this.getLevel().setBlock(block3, Block.get(26, n | 8), true, true);
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                this.a(this, item.getDamage());
                this.a(block3, item.getDamage());
            }, 2);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        Block block = this.north();
        Block block2 = this.south();
        Block block3 = this.east();
        Block block4 = this.west();
        Block block5 = null;
        if ((this.getDamage() & 8) == 8) {
            if (block.getId() == 26 && (block.getDamage() & 8) != 8) {
                block5 = block;
            } else if (block2.getId() == 26 && (block2.getDamage() & 8) != 8) {
                block5 = block2;
            } else if (block3.getId() == 26 && (block3.getDamage() & 8) != 8) {
                block5 = block3;
            } else if (block4.getId() == 26 && (block4.getDamage() & 8) != 8) {
                block5 = block4;
            }
        } else if (block.getId() == 26 && (block.getDamage() & 8) == 8) {
            block5 = block;
        } else if (block2.getId() == 26 && (block2.getDamage() & 8) == 8) {
            block5 = block2;
        } else if (block3.getId() == 26 && (block3.getDamage() & 8) == 8) {
            block5 = block3;
        } else if (block4.getId() == 26 && (block4.getDamage() & 8) == 8) {
            block5 = block4;
        }
        if (block5 != null) {
            Entity[] entityArray;
            Entity[] entityArray2 = entityArray = (block5.getDamage() & 8) == 8 ? block5.toItem() : null;
            if (this.getLevel().setBlock(block5, Block.get(0), true, true) && entityArray != null && this.canDropItem && this.getLevel().gameRules.getBoolean(GameRule.DO_TILE_DROPS)) {
                this.getLevel().dropItem(this.add(0.5, 0.5, 0.5), (Item)entityArray);
            }
        }
        this.getLevel().setBlock(this, Block.get(0), true, block5 == null);
        for (Entity entity : this.level.getNearbyEntities(new AxisAlignedBB(this, this).grow(2.0, 1.0, 2.0))) {
            Player player;
            if (!(entity instanceof Player) || (player = (Player)entity).getSleepingPos() == null || !player.getSleepingPos().equals(this) && !player.getSleepingPos().equals(block5)) continue;
            player.stopSleep();
        }
        return true;
    }

    private void a(Vector3 vector3, int n) {
        CompoundTag compoundTag = BlockEntity.getDefaultCompound(vector3, "Bed");
        compoundTag.putByte("color", n);
        BlockEntity.createBlockEntity("Bed", this.getChunk(), compoundTag, new Object[0]);
    }

    @Override
    public Item toItem() {
        return Item.get(355, this.getDyeColor().getWoolData());
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        BlockEntity blockEntity;
        if (this.level != null && (blockEntity = this.level.getBlockEntity(this)) instanceof BlockEntityBed) {
            return ((BlockEntityBed)blockEntity).getDyeColor();
        }
        return DyeColor.WHITE;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}

