/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;

public class ShulkerBoxDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Block block = blockDispenser.getSide(blockFace);
        if (block.getId() == 0) {
            CompoundTag compoundTag;
            CompoundTag compoundTag2 = BlockEntity.getDefaultCompound(block, "ShulkerBox");
            compoundTag2.putByte("facing", BlockFace.UP.getIndex());
            if (item.hasCustomName()) {
                compoundTag2.putString("CustomName", item.getCustomName());
            }
            if ((compoundTag = item.getNamedTag()) != null && compoundTag.contains("Items")) {
                compoundTag2.putList(compoundTag.getList("Items"));
            }
            blockDispenser.level.setBlock(block, Block.get(218, item.getDamage()), true);
            BlockEntity.createBlockEntity("ShulkerBox", blockDispenser.level.getChunk(block.getChunkX(), block.getChunkZ()), compoundTag2, new Object[0]);
            return null;
        }
        return item;
    }
}

