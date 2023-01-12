/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Utils;
import java.util.Map;

public class BlockTrappedChest
extends BlockChest {
    public BlockTrappedChest() {
        this(0);
    }

    public BlockTrappedChest(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 146;
    }

    @Override
    public String getName() {
        return "Trapped Chest";
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Object object22;
        BlockEntityChest blockEntityChest = null;
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        for (Object object22 : BlockFace.Plane.HORIZONTAL) {
            BlockEntity object3;
            Object object4;
            if ((this.getDamage() == 4 || this.getDamage() == 5) && (object22 == BlockFace.WEST || object22 == BlockFace.EAST) || (this.getDamage() == 3 || this.getDamage() == 2) && (object22 == BlockFace.NORTH || object22 == BlockFace.SOUTH) || !((object4 = this.getSide((BlockFace)((Object)object22))) instanceof BlockTrappedChest) || ((Block)object4).getDamage() != this.getDamage() || !((object3 = this.getLevel().getBlockEntity((Vector3)object4)) instanceof BlockEntityChest) || ((BlockEntityChest)object3).isPaired()) continue;
            blockEntityChest = (BlockEntityChest)object3;
            break;
        }
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag("").putList(new ListTag("Items")).putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            object22 = item.getCustomBlockData().getTags();
            for (Map.Entry entry : object22.entrySet()) {
                compoundTag.put((String)entry.getKey(), (Tag)entry.getValue());
            }
        }
        object22 = (BlockEntityChest)BlockEntity.createBlockEntity("Chest", this.getChunk(), compoundTag, new Object[0]);
        if (blockEntityChest != null) {
            blockEntityChest.pairWith((BlockEntityChest)object22);
            ((BlockEntityChest)object22).pairWith(blockEntityChest);
        }
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        int n = 0;
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityChest) {
            n = ((BlockEntityChest)blockEntity).getInventory().getViewers().size();
        }
        return Math.min(n, 15);
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return blockFace == BlockFace.UP ? this.getWeakPower(blockFace) : 0;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }
}

