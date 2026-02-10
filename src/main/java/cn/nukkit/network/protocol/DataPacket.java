package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRules;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.types.EntityLink;
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

    @Deprecated
    @Override
    public BlockVector3 getBlockVector3() {
        return protocol != Integer.MAX_VALUE ? getBlockVector3(protocol) : super.getBlockVector3();
    }

    @Deprecated
    @Override
    public Skin getSkin() {
        return protocol != Integer.MAX_VALUE ? getSkin(protocol) : super.getSkin();
    }

    @Deprecated
    @Override
    public Item getSlot() {
        return protocol != Integer.MAX_VALUE ? getSlot(protocol) : super.getSlot();
    }

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
            Server.getInstance().getLogger().debug("Warning: decode() not implemented for " + this.getClass().getName(), new Throwable(""));
        }
    }

    public abstract void encode();

    void encodeUnsupported() {
        if (Nukkit.DEBUG > 1) {
            Server.getInstance().getLogger().debug("Warning: encode() not implemented for " + this.getClass().getName(), new Throwable(""));
        }
    }

    // PM1E: attempt to use packet protocol automatically

    public abstract byte pid();

    @Deprecated
    @Override
    public void putBlockVector3(BlockVector3 v) {
        if (protocol != Integer.MAX_VALUE) putBlockVector3(protocol, v.x, v.y, v.z);
        else super.putBlockVector3(v);
    }

    @Deprecated
    @Override
    public void putBlockVector3(int x, int y, int z) {
        if (protocol != Integer.MAX_VALUE) putBlockVector3(protocol, x, y, z);
        else super.putBlockVector3(x, y, z);
    }

    @Deprecated
    @Override
    public void putEntityLink(EntityLink link) {
        if (protocol != Integer.MAX_VALUE) putEntityLink(protocol, link);
        else super.putEntityLink(link);
    }

    @Deprecated
    @Override
    public void putGameRules(GameRules gameRules, boolean startGame) {
        if (protocol != Integer.MAX_VALUE) putGameRules(protocol, gameRules, startGame);
        else super.putGameRules(gameRules, startGame);
    }

    @Deprecated
    @Override
    public void putSkin(Skin skin) {
        if (protocol != Integer.MAX_VALUE) putSkin(protocol, skin);
        else super.putSkin(skin);
    }

    @Deprecated
    @Override
    public void putSlot(Item item) {
        if (protocol != Integer.MAX_VALUE) putSlot(protocol, item);
        else super.putSlot(item);
    }

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
