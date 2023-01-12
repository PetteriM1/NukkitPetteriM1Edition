/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityJukebox;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemRecord;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.utils.BlockColor;

public class BlockJukebox
extends BlockSolid {
    @Override
    public String getName() {
        return "Jukebox";
    }

    @Override
    public int getId() {
        return 84;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityJukebox)) {
            return false;
        }
        BlockEntityJukebox blockEntityJukebox = (BlockEntityJukebox)blockEntity;
        if (blockEntityJukebox.getRecordItem().getId() != 0) {
            blockEntityJukebox.dropItem();
        } else if (item instanceof ItemRecord) {
            blockEntityJukebox.setRecordItem(item);
            blockEntityJukebox.play();
            if (player != null) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                TextPacket textPacket = new TextPacket();
                textPacket.type = (byte)4;
                textPacket.message = "%record.nowPlaying";
                textPacket.parameters = new String[]{((ItemRecord)item).getDiscName()};
                textPacket.isLocalized = true;
                player.dataPacket(textPacket);
            }
        }
        return false;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (super.place(item, block, block2, blockFace, d2, d3, d4, player)) {
            this.a();
            return true;
        }
        return false;
    }

    private BlockEntity a() {
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "Jukebox").putInt("x", this.getFloorX()).putInt("y", this.getFloorY()).putInt("z", this.getFloorZ());
        return BlockEntity.createBlockEntity("Jukebox", this.getChunk(), compoundTag, new Object[0]);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}

