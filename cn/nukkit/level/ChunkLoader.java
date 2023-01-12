/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;

public interface ChunkLoader {
    public int getLoaderId();

    public boolean isLoaderActive();

    public Position getPosition();

    public double getX();

    public double getZ();

    public Level getLevel();

    public void onChunkChanged(FullChunk var1);

    public void onChunkLoaded(FullChunk var1);

    public void onChunkUnloaded(FullChunk var1);

    public void onChunkPopulated(FullChunk var1);

    public void onBlockChanged(Vector3 var1);
}

