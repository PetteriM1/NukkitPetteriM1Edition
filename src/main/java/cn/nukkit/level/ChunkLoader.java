package cn.nukkit.level;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface ChunkLoader {

    Level getLevel();

    int getLoaderId();

    Position getPosition();

    double getX();

    double getZ();

    boolean isLoaderActive();

    void onBlockChanged(Vector3 block);

    void onChunkChanged(FullChunk chunk);

    void onChunkLoaded(FullChunk chunk);

    void onChunkPopulated(FullChunk chunk);

    void onChunkUnloaded(FullChunk chunk);
}
