/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;
import com.nukkitx.network.raknet.RakNetReliability;

public abstract class DataPacket
extends BinaryStream
implements Cloneable {
    public int protocol = Integer.MAX_VALUE;
    public volatile boolean isEncoded = false;
    private int d = 0;
    public RakNetReliability reliability = RakNetReliability.RELIABLE_ORDERED;

    public abstract byte pid();

    public abstract void decode();

    public abstract void encode();

    @Override
    public DataPacket reset() {
        super.reset();
        if (Nukkit.DEBUG > 1 && this.protocol == Integer.MAX_VALUE) {
            Server.getInstance().getLogger().debug("Warning: DataPacket#reset() was called before setting the protocol. To ensure multiversion compatibility make sure the protocol is set before trying to encode a packet.", new Throwable());
        }
        if (this.protocol <= 274) {
            this.putByte(this.pid());
            this.putShort(0);
        } else {
            this.putUnsignedVarInt(this.pid() & 0xFF);
        }
        return this;
    }

    public void setChannel(int n) {
        this.d = n;
    }

    public int getChannel() {
        return this.d;
    }

    public DataPacket clean() {
        this.setBuffer(null);
        this.setOffset(0);
        this.isEncoded = false;
        return this;
    }

    public DataPacket clone() {
        try {
            DataPacket dataPacket = (DataPacket)super.clone();
            dataPacket.setBuffer(this.count < 0 ? null : this.getBuffer());
            dataPacket.offset = this.offset;
            dataPacket.count = this.count;
            return dataPacket;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public BatchPacket compress() {
        return this.compress(Server.getInstance().networkCompressionLevel);
    }

    public BatchPacket compress(int n) {
        BatchPacket batchPacket = new BatchPacket();
        byte[][] byArrayArray = new byte[2][];
        byte[] byArray = this.getBuffer();
        byArrayArray[0] = Binary.writeUnsignedVarInt(byArray.length);
        byArrayArray[1] = byArray;
        byte[] byArray2 = Binary.appendBytes(byArrayArray);
        try {
            batchPacket.payload = this.protocol >= 407 ? Zlib.deflateRaw(byArray2, n) : Zlib.deflate(byArray2, n);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return batchPacket;
    }

    public final void tryEncode() {
        block0: {
            if (this.isEncoded) break block0;
            this.isEncoded = true;
            this.encode();
        }
    }

    static int e(int n) {
        return n;
    }

    void b() {
        Server.getInstance().getLogger().debug("Warning: decode() not implemented for " + this.getClass().getName());
    }

    void a() {
        block0: {
            Server.getInstance().getLogger().debug("Warning: encode() not implemented for " + this.getClass().getName());
            if (Nukkit.DEBUG <= 1) break block0;
            Thread.dumpStack();
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

