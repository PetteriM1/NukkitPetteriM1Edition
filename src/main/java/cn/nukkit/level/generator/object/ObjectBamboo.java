package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBamboo;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class ObjectBamboo {

    public static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 15;

    public static void growBamboo(ChunkManager level, Vector3 pos, NukkitRandom random) {
        if (!canPlaceObject(level, pos)) return;

        BlockBamboo bamboo = new BlockBamboo();
        bamboo.setLeafSize(BlockBamboo.LEAF_SIZE_NONE);
        int height = random.nextRange(MIN_HEIGHT, MAX_HEIGHT);

        if (height > 4){
            bamboo.setThick(true);
        }

        for (int i = 0; i < height; i++){
            if (level.getBlockIdAt((int) pos.getX(), (int) pos.getY() + i, (int) pos.getZ()) != Block.AIR){
                break;
            }

            if (i >= height*0.7 || (height == 3 && i == 2)){
                bamboo.setLeafSize(bamboo.isThick()? BlockBamboo.LEAF_SIZE_LARGE : BlockBamboo.LEAF_SIZE_SMALL);
            }
            level.setBlockAt((int) pos.getX(), (int) pos.getY() + i, (int) pos.getZ(), bamboo.getId(), bamboo.getDamage());
        }
    }

    private static boolean canPlaceObject(ChunkManager level, Vector3 pos) {
        return level.getBlockIdAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) == Block.AIR;
    }

}
