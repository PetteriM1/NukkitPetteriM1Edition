/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import java.util.Random;

public abstract class TreeGenerator
extends BasicGenerator {
    protected boolean canGrowInto(int n) {
        return n == 0 || n == 18 || n == 2 || n == 3 || n == 17 || n == 162 || n == 6 || n == 106;
    }

    public void generateSaplings(Level level, Random random, Vector3 vector3) {
    }

    protected void setDirtAt(ChunkManager chunkManager, BlockVector3 blockVector3) {
        this.setDirtAt(chunkManager, new Vector3(blockVector3.x, blockVector3.y, blockVector3.z));
    }

    protected void setDirtAt(ChunkManager chunkManager, Vector3 vector3) {
        if (chunkManager.getBlockIdAt((int)vector3.x, (int)vector3.y, (int)vector3.z) != 3) {
            this.setBlockAndNotifyAdequately(chunkManager, vector3, Block.get(3));
        }
    }
}

