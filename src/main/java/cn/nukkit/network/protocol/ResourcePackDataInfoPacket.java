package cn.nukkit.network.protocol;

import lombok.ToString;

import java.util.UUID;

@ToString(exclude = "sha256")
public class ResourcePackDataInfoPacket extends DataPacket {

    public static final int TYPE_INVALID = 0;
    public static final int TYPE_ADDON = 1;
    public static final int TYPE_CACHED = 2;
    public static final int TYPE_COPY_PROTECTED = 3;
    public static final int TYPE_BEHAVIOR = 4;
    public static final int TYPE_PERSONA_PIECE = 5;
    public static final int TYPE_RESOURCE = 6;
    public static final int TYPE_SKINS = 7;
    public static final int TYPE_WORLD_TEMPLATE = 8;
    public static final int TYPE_COUNT = 9;

    public UUID packId;
    public int maxChunkSize;
    public int chunkCount;
    public long compressedPackSize;
    public byte[] sha256;
    public boolean premium;
    public int type = -1;

    @Override
    public void decode(int protocolId) {
        this.packId = UUID.fromString(this.getString());
        this.maxChunkSize = this.getLInt();
        this.chunkCount = this.getLInt();
        this.compressedPackSize = this.getLLong();
        this.sha256 = this.getByteArray();
        try {
            this.premium = this.getBoolean();
            this.type = this.getByte();
        } catch (Exception ignored) {}
    }

    @Override
    public void encode(int protocolId) {
        this.putString(this.packId.toString());
        this.putLInt(this.maxChunkSize);
        this.putLInt(this.chunkCount);
        this.putLLong(this.compressedPackSize);
        this.putByteArray(this.sha256);
        if (protocolId >= 361) {
            this.putBoolean(this.premium);
            this.putByte((byte) (this.type == -1? (protocolId < ProtocolInfo.v1_13_0? 1 : TYPE_RESOURCE) : type));
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET;
    }
}
