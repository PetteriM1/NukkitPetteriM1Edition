/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;

public class TNTDispenseBehavior
extends DefaultDispenseBehavior {
    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Position position = blockDispenser.getSide(blockFace).add(0.5, 0.0, 0.5);
        EntityPrimedTNT entityPrimedTNT = new EntityPrimedTNT(blockDispenser.level.getChunk(position.getChunkX(), position.getChunkZ()), Entity.getDefaultNBT(position));
        entityPrimedTNT.spawnToAll();
        return null;
    }
}

