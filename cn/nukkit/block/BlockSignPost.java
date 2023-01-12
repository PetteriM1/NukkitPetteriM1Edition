/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.block.SignColorChangeEvent;
import cn.nukkit.event.block.SignGlowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;

public class BlockSignPost
extends BlockTransparentMeta
implements Faceable {
    public BlockSignPost() {
        this(0);
    }

    public BlockSignPost(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 63;
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getName() {
        return "Sign Post";
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    protected int getPostId() {
        return 63;
    }

    protected int getWallId() {
        return 68;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (blockFace != BlockFace.DOWN) {
            CompoundTag compoundTag = new CompoundTag().putString("id", "Sign").putInt("x", (int)block.x).putInt("y", (int)block.y).putInt("z", (int)block.z).putString("Text1", "").putString("Text2", "").putString("Text3", "").putString("Text4", "");
            if (blockFace == BlockFace.UP) {
                this.setDamage((int)Math.floor((player.yaw + 180.0) * 16.0 / 360.0 + 0.5) & 0xF);
                this.getLevel().setBlock(block, Block.get(this.getPostId(), this.getDamage()), true);
            } else {
                this.setDamage(blockFace.getIndex());
                this.getLevel().setBlock(block, Block.get(this.getWallId(), this.getDamage()), true);
            }
            if (player != null) {
                compoundTag.putString("Creator", player.getUniqueId().toString());
            }
            if (item.hasCustomBlockData()) {
                for (Tag tag : item.getCustomBlockData().getAllTags()) {
                    compoundTag.put(tag.getName(), tag);
                }
            }
            BlockEntity.createBlockEntity("Sign", this.getChunk(), compoundTag, new Object[0]);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().getId() == 0) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return Item.get(323);
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351) {
            BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this);
            if (!(blockEntity instanceof BlockEntitySign)) {
                return false;
            }
            BlockEntitySign blockEntitySign = (BlockEntitySign)blockEntity;
            int n = item.getDamage();
            if (n == 0 || n == 20) {
                boolean bl;
                boolean bl2 = bl = n == 20;
                if (blockEntitySign.isGlowing() == bl) {
                    if (player != null) {
                        blockEntitySign.spawnTo(player);
                    }
                    return false;
                }
                SignGlowEvent signGlowEvent = new SignGlowEvent(this, player, bl);
                this.level.getServer().getPluginManager().callEvent(signGlowEvent);
                if (signGlowEvent.isCancelled()) {
                    return false;
                }
                blockEntitySign.setGlowing(bl);
                blockEntitySign.spawnToAll();
                this.level.addLevelEvent(this, 1066);
                if (player != null && (player.getGamemode() & 1) == 0) {
                    --item.count;
                }
                return true;
            }
            BlockColor blockColor = DyeColor.getByDyeData(n).getSignColor();
            if (blockColor.equals(blockEntitySign.getColor())) {
                return false;
            }
            SignColorChangeEvent signColorChangeEvent = new SignColorChangeEvent(this, player, blockColor);
            this.level.getServer().getPluginManager().callEvent(signColorChangeEvent);
            if (signColorChangeEvent.isCancelled()) {
                if (player != null) {
                    blockEntitySign.spawnTo(player);
                }
                return false;
            }
            blockEntitySign.setColor(blockColor);
            blockEntitySign.spawnToAll();
            this.level.addLevelEvent(this, 1065);
            if (player != null && (player.getGamemode() & 1) == 0) {
                --item.count;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

