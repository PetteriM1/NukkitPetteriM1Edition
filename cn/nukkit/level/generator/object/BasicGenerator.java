/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public abstract class BasicGenerator {
    public abstract boolean generate(ChunkManager var1, NukkitRandom var2, Vector3 var3);

    protected void setBlockAndNotifyAdequately(ChunkManager chunkManager, BlockVector3 blockVector3, Block block) {
        this.setBlock(chunkManager, new Vector3(blockVector3.x, blockVector3.y, blockVector3.z), block);
    }

    protected void setBlockAndNotifyAdequately(ChunkManager chunkManager, Vector3 vector3, Block block) {
        this.setBlock(chunkManager, vector3, block);
    }

    protected void setBlock(ChunkManager chunkManager, Vector3 vector3, Block block) {
        chunkManager.setBlockAt((int)vector3.x, (int)vector3.y, (int)vector3.z, block.getId(), block.getDamage());
    }
}

