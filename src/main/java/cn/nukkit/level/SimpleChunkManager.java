package cn.nukkit.level;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class SimpleChunkManager implements ChunkManager {

    protected Long2ObjectOpenHashMap<FullChunk> chunks = new Long2ObjectOpenHashMap<>();

    protected long seed;

    public SimpleChunkManager(long seed) {
        this.seed = seed;
    }

    @Override
    public int getBlockIdAt(int x, int y, int z) {
        return this.getBlockIdAt(x, y, z, 0);
    }

    @Override
    public int getBlockIdAt(int x, int y, int z, int layer) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockId(x & 0xf, y & 0xff, z & 0xf, layer);
        }
        return 0;
    }

    @Override
    public void setBlockFullIdAt(int x, int y, int z, int layer, int fullId) {
        //noop
    }

    @Override
    public void setBlockIdAt(int x, int y, int z, int id) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockId(x & 0xf, y & 0xff, z & 0xf, id);
        }
    }

    @Override
    public boolean setBlockAtLayer(int x, int y, int z, int layer, int id, int data) {
        return false; //TODO: layers
    }

    @Override
    public void setBlockAt(int x, int y, int z, int id, int data) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlock(x & 0xf, y & 0xff, z & 0xf, id, data);
        }
    }
    
    @Override
    public void setBlockFullIdAt(int x, int y, int z, int fullId) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setFullBlockId(x & 0xf, y & 0xff, z & 0xf, fullId);
        }
    }

    @Override
    public void setBlockIdAt(int x, int y, int z, int layer, int id) {

    }

    @Override
    public int getBlockDataAt(int x, int y, int z) {
       return this.getBlockDataAt(x, y, z, 0);
    }

    @Override
    public int getBlockDataAt(int x, int y, int z, int layer) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x & 0xf, y & 0xff, z & 0xf, layer);
        }
        return 0;
    }

    @Override
    public void setBlockDataAt(int x, int y, int z, int data) {
        this.setBlockDataAt(x, y, z, 0, data);
    }

    @Override
    public void setBlockDataAt(int x, int y, int z, int layer, int data) {
        FullChunk chunk = this.getChunk(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setBlockData(x & 0xf, y & 0xff, z & 0xf, data, layer);
        }
    }

    @Override
    public BaseFullChunk getChunk(int chunkX, int chunkZ) {
        long index = Level.chunkHash(chunkX, chunkZ);
        return this.chunks.containsKey(index) ? (BaseFullChunk) this.chunks.get(index) : null;
    }

    @Override
    public void setChunk(int chunkX, int chunkZ) {
        this.setChunk(chunkX, chunkZ, null);
    }

    @Override
    public void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk) {
        if (chunk == null) {
            this.chunks.remove(Level.chunkHash(chunkX, chunkZ));
            return;
        }
        this.chunks.put(Level.chunkHash(chunkX, chunkZ), chunk);
    }

    public void cleanChunks() {
        this.chunks.clear();
    }

    @Override
    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void cleanChunks(long seed) {
        this.seed = seed;
    }
}
