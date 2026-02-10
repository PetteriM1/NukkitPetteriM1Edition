package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public abstract class BasicGenerator {

    public abstract boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position);

    protected void setBlock(ChunkManager level, Vector3 v, Block b) {
        level.setBlockAt((int) v.x, (int) v.y, (int) v.z, b.getId(), b.getDamage());
    }

    protected void setBlockAndNotifyAdequately(ChunkManager level, Vector3 pos, Block state) {
        level.setBlockAt((int) pos.x, (int) pos.y, (int) pos.z, state.getId(), state.getDamage());
    }

    protected void setBlockAndNotifyAdequately(ChunkManager level, BlockVector3 pos, Block state) {
        level.setBlockAt(pos.x, pos.y, pos.z, state.getId(), state.getDamage());
    }
}
