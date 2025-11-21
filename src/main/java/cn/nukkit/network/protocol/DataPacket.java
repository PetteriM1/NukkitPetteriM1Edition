package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.SnappyCompression;
import cn.nukkit.utils.Zlib;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class DataPacket extends BinaryStream implements Cloneable {

    public int protocol = Integer.MAX_VALUE;
    public volatile boolean isEncoded = false;

    static int CONST(@SuppressWarnings("SameParameterValue") int i) {
        return i;
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
            DataPacket packet = (DataPacket) super.clone();
            packet.setBuffer(this.count < 0 ? null : this.getBuffer()); // prevent reflecting same buffer instance
            packet.offset = this.offset;
            packet.count = this.count;
            return packet;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public BatchPacket compress() {
        return this.compress(Server.getInstance().networkCompressionLevel);
    }

    public BatchPacket compress(int level) {
        byte[] buf = this.getBuffer();
        BinaryStream stream = new BinaryStream(new byte[5 + buf.length]).reset();
        stream.putUnsignedVarInt(buf.length);
        stream.put(buf);
        try {
            byte[] bytes = stream.getBuffer();
            BatchPacket batched = new BatchPacket();
            if (Server.getInstance().useSnappy && protocol >= ProtocolInfo.v1_19_30_23) {
                batched.payload = SnappyCompression.compress(bytes);
            } else if (protocol >= ProtocolInfo.v1_16_0) {
                batched.payload = Zlib.deflateRaw(bytes, level);
            } else {
                batched.payload = Zlib.deflatePre16Packet(bytes, level);
            }
            return batched;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void decode();

    void decodeUnsupported() {
        if (Nukkit.DEBUG > 1) {
            Server.getInstance().getLogger().debug("Warning: decode() not implemented for " + this.getClass().getName());
        }
    }

    public abstract void encode();

    void encodeUnsupported() {
        if (Nukkit.DEBUG > 1) {
            Server.getInstance().getLogger().debug("Warning: encode() not implemented for " + this.getClass().getName());
            Thread.dumpStack();
        }
    }

    public abstract byte pid();

    @Override
    public DataPacket reset() {
        super.reset();
        if (protocol == Integer.MAX_VALUE && Server.getInstance().minimumProtocol != ProtocolInfo.CURRENT_PROTOCOL) {
            Server.getInstance().getLogger().warning("DataPacket#reset() called before protocol was set. This can crash multiversion clients.", new Throwable(""));
        }
        byte packetId = this.pid();
        if (protocol <= 274) {
            this.putByte(packetId);
            this.putShort(0);
        } else if (packetId < 0 && packetId >= -56) { // Hack: (byte) 200+ --> (int) 300+
            this.putUnsignedVarInt(packetId + 356);
        } else {
            this.putUnsignedVarInt(packetId & 0xff);
        }
        return this;
    }

    public final void tryEncode() {
        if (!this.isEncoded) {
            this.isEncoded = true;
            this.encode();
        }
    }
}
