package cn.nukkit.level.format;
public interface FullChunk {
    void setBlockId(int x, int y, int z, int id);
    int getHighestBlockAt(int x, int z);
}
