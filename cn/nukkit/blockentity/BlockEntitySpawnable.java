/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import java.io.IOException;
import java.nio.ByteOrder;

public abstract class BlockEntitySpawnable
extends BlockEntity {
    public BlockEntitySpawnable(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        super.initBlockEntity();
        this.spawnToAll();
    }

    public abstract CompoundTag getSpawnCompound();

    public BlockEntityDataPacket createSpawnPacket() {
        CompoundTag compoundTag = this.getSpawnCompound();
        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.x = (int)this.x;
        blockEntityDataPacket.y = (int)this.y;
        blockEntityDataPacket.z = (int)this.z;
        try {
            blockEntityDataPacket.namedTag = NBTIO.write(compoundTag, ByteOrder.LITTLE_ENDIAN, true);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return blockEntityDataPacket;
    }

    public void spawnTo(Player player) {
        block0: {
            if (this.closed) break block0;
            player.dataPacket(this.createSpawnPacket());
        }
    }

    public void spawnToAll() {
        block0: {
            if (this.closed) break block0;
            this.level.addChunkPacket(this.chunk.getX(), this.chunk.getZ(), this.createSpawnPacket());
        }
    }

    public boolean updateCompoundTag(CompoundTag compoundTag, Player player) {
        return false;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

