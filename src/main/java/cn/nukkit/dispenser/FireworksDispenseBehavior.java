package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class FireworksDispenseBehavior extends DefaultDispenseBehavior {

    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        EntityFirework firework = new EntityFirework(block.level.getChunk(block.getChunkX(), block.getChunkZ()),
                Entity.getDefaultNBT(block.getSide(face).add(0, 0.2, 0)));
        firework.spawnToAll();

        return null;
    }
}
