/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFurnaceBurning;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBlastFurnace;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Utils;
import java.util.Map;

public class BlockBlastFurnaceLit
extends BlockFurnaceBurning {
    public BlockBlastFurnaceLit() {
        this(0);
    }

    public BlockBlastFurnaceLit(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Lit Blast Furnace";
    }

    @Override
    public int getId() {
        return 469;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(451));
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Object object;
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "BlastFurnace").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            object = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : object.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        return (object = (BlockEntityBlastFurnace)BlockEntity.createBlockEntity("BlastFurnace", this.getChunk(), compoundTag, new Object[0])) != null;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityBlastFurnace)) {
                return false;
            }
            BlockEntityBlastFurnace blockEntityBlastFurnace = (BlockEntityBlastFurnace)blockEntity;
            if (blockEntityBlastFurnace.namedTag.contains("Lock") && blockEntityBlastFurnace.namedTag.get("Lock") instanceof StringTag && !blockEntityBlastFurnace.namedTag.getString("Lock").equals(item.getCustomName())) {
                return true;
            }
            player.addWindow(blockEntityBlastFurnace.getInventory());
        }
        return true;
    }
}

