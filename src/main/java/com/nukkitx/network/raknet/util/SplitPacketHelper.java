package com.nukkitx.network.raknet.util;

import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetSession;
import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import javax.annotation.Nullable;

public class SplitPacketHelper extends AbstractReferenceCounted {

    private final EncapsulatedPacket[] packets;

    public SplitPacketHelper(long expectedLength) {
        if (expectedLength < 1 || expectedLength > 2048) {
            throw new IllegalArgumentException("expectedLength is less than 1 or greater than 2048 " + expectedLength);
        }

        this.packets = new EncapsulatedPacket[(int) expectedLength];
    }

    @Nullable
    public EncapsulatedPacket add(EncapsulatedPacket packet, RakNetSession session) {
        if (!packet.isSplit()) throw new IllegalArgumentException("Packet is not split");
        if (this.refCnt() <= 0) throw new IllegalStateException("Packet has been released");
        if (packet.getPartIndex() < 0 || packet.getPartIndex() >= this.packets.length) {
            throw new IndexOutOfBoundsException();
        }

        int partIndex = packet.getPartIndex();
        if (this.packets[partIndex] != null) {
            // Duplicate
            return null;
        }
        this.packets[partIndex] = packet;
        // Retain the packet so it can be reassembled later.
        packet.retain();

        int sz = 0;
        for (EncapsulatedPacket netPacket : this.packets) {
            if (netPacket == null) {
                return null;
            }
            sz += netPacket.getBuffer().readableBytes();
        }

        // We can't use a composite buffer as the native code will choke on it
        ByteBuf reassembled = session.allocateBuffer(sz);
        for (EncapsulatedPacket netPacket : this.packets) {
            ByteBuf buf = netPacket.getBuffer();
            reassembled.writeBytes(buf, buf.readerIndex(), buf.readableBytes());
        }

        return packet.fromSplit(reassembled);
    }

    @Override
    protected void deallocate() {
        for (EncapsulatedPacket packet : this.packets) {
            ReferenceCountUtil.release(packet);
        }
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        throw new UnsupportedOperationException();
    }
}
