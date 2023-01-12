/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;

public class FireworksDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        BlockFace blockFace2 = blockFace.getOpposite();
        Position position = blockDispenser.getSide(blockFace).add(0.5 + (double)blockFace2.getXOffset() * 0.2, 0.5 + (double)blockFace2.getYOffset() * 0.2, 0.5 + (double)blockFace2.getZOffset() * 0.2);
        CompoundTag compoundTag = Entity.getDefaultNBT(position);
        compoundTag.putCompound("FireworkItem", NBTIO.putItemHelper(item));
        new EntityFirework(blockDispenser.level.getChunk(position.getChunkX(), position.getChunkZ()), compoundTag).spawnToAll();
        return null;
    }
}

