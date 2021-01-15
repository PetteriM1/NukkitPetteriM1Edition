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
    public volatile boolean isEncoded = false;
    private int channel = Network.CHANNEL_NONE;

    public RakNetReliability reliability = RakNetReliability.RELIABLE_ORDERED;

    public abstract byte pid();

    public void decode() {
        this.decode(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public void decode(int protocolId) {
        this.decodePayload(protocolId);
    }

    public final void tryEncode(int protocolId) {
        if (!this.isEncoded) {
            this.isEncoded = true;
            this.encode(protocolId);
        }
    }

    public void encode(int protocolId) {
        this.reset(protocolId);
        this.encodePayload(protocolId);
    }

    public void decodePayload(int protocolId) {
        // TODO: consider making this abstract
    }

    public void encodePayload(int protocolId) {
        // TODO: consider making this abstract
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
