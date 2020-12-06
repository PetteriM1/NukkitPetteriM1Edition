package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.network.Network;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;
import com.nukkitx.network.raknet.RakNetReliability;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class DataPacket extends BinaryStream implements Cloneable {
    public boolean isEncoded = false;
    private int channel = Network.CHANNEL_NONE;

    public RakNetReliability reliability = RakNetReliability.RELIABLE_ORDERED;

    public abstract byte pid();

    public abstract void decode(int protocolId);

    public void decodePacket(int protocolId) {
        this.decode(protocolId);
    }

    public abstract void encode(int protocolId);

    public void encodePacket(int protocolId) {
        this.reset(protocolId);
        this.encode(protocolId);
    }

    public DataPacket reset(int protocolId) {
        this.reset();
        if (protocolId <= ProtocolInfo.v1_5_0) {
            this.putByte(this.pid());
            this.putShort(0);
        } else {
            this.putUnsignedVarInt(this.pid() & 0xff);
        }
        return this;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public DataPacket clean() {
        this.setBuffer(null);
        this.setOffset(0);
        this.isEncoded = false;
        return this;
    }

    @Override
    public DataPacket clone() {
        try {
            return (DataPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public BatchPacket compress() {
        return compress(Server.getInstance().networkCompressionLevel, ProtocolInfo.CURRENT_PROTOCOL);
    }

    public BatchPacket compress(int level, int protocolId) {
        BatchPacket batch = new BatchPacket();
        byte[][] batchPayload = new byte[2][];
        byte[] buf = getBuffer();
        batchPayload[0] = Binary.writeUnsignedVarInt(buf.length);
        batchPayload[1] = buf;
        byte[] data = Binary.appendBytes(batchPayload);
        try {
            if (protocolId >= ProtocolInfo.v1_16_0) {
                batch.payload = Zlib.deflateRaw(data, level);
            } else {
                batch.payload = Zlib.deflate(data, level);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return batch;
    }
}
