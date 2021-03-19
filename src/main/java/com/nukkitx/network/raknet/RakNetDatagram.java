package com.nukkitx.network.raknet;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.nukkitx.network.raknet.RakNetConstants.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString()
public class RakNetDatagram extends AbstractReferenceCounted {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(RakNetDatagram.class);

    final List<EncapsulatedPacket> packets = new ArrayList<>();
    byte flags = FLAG_VALID;
    final long sendTime;
    long nextSend;
    int sequenceIndex = -1;

    @Override
    public RakNetDatagram retain() {
        super.retain();
        return this;
    }

    @Override
    public RakNetDatagram retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override
    public RakNetDatagram touch(Object hint) {
        for (EncapsulatedPacket packet : packets) {
            packet.touch(hint);
        }
        return this;
    }

    void decode(ByteBuf buf) {
        flags = buf.readByte();
        sequenceIndex = buf.readUnsignedMediumLE();
        while (buf.isReadable()) {
            EncapsulatedPacket packet = new EncapsulatedPacket();
            packet.decode(buf);
            packets.add(packet);
        }
    }

    public void encode(ByteBuf buf) {
        buf.writeByte(flags);
        buf.writeMediumLE(sequenceIndex);
        for (EncapsulatedPacket packet : packets) {
            packet.encode(buf);
        }
    }

    boolean tryAddPacket(EncapsulatedPacket packet, int mtu) {
        if (this.getSize() + packet.getSize() > mtu - RAKNET_DATAGRAM_HEADER_SIZE) {
            return false;
        }

        packets.add(packet);
        if (packet.split) {
            flags |= FLAG_CONTINUOUS_SEND;
        }
        return true;
    }

    @Override
    public boolean release() {
        return super.release();
    }

    @Override
    protected void deallocate() {
        for (EncapsulatedPacket packet : packets) {
            packet.release();
        }
    }

    public int getSize() {
        int size = RAKNET_DATAGRAM_HEADER_SIZE;
        for (EncapsulatedPacket packet : packets) {
            size += packet.getSize();
        }
        return size;
    }
}