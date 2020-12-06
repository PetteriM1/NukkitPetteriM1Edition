package cn.nukkit.network.protocol;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;

import java.io.IOException;
import java.nio.ByteOrder;

public class LevelEventGenericPacket extends DataPacket {

    public int eventId;
    public CompoundTag tag;

    @Override
    public byte pid() {
        return ProtocolInfo.LEVEL_EVENT_GENERIC_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(eventId);
        try {
            this.put(NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
