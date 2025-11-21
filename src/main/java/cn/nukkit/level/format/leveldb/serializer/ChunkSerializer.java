package cn.nukkit.level.format.leveldb.serializer;

import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.leveldb.structure.ChunkBuilder;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.WriteBatch;

public interface ChunkSerializer {

    void deserialize(DB db, ChunkBuilder chunkBuilder);

    void serialize(WriteBatch db, Chunk chunk);
}
