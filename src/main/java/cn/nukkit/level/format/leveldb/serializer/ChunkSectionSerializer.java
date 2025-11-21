package cn.nukkit.level.format.leveldb.serializer;

import cn.nukkit.level.format.leveldb.structure.ChunkBuilder;
import cn.nukkit.level.format.leveldb.structure.StateBlockStorage;
import io.netty.buffer.ByteBuf;

interface ChunkSectionSerializer {

    StateBlockStorage[] deserialize(ByteBuf buf, ChunkBuilder builder);

    void serialize(ByteBuf buf, StateBlockStorage[] storage, int ySection);
}
