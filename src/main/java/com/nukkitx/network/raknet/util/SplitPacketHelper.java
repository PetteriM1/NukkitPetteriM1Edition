package com.nukkitx.network.raknet.util;

import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.network.util.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import javax.annotation.Nullable;

public class SplitPacketHelper extends AbstractReferenceCounted {

    private final EncapsulatedPacket[] packets;
    private final long created = System.currentTimeMillis();

    public SplitPacketHelper(long expectedLength) {
        Preconditions.checkArgument(expectedLength >= 1, "expectedLength is less than 1 (%s)", expectedLength);
        this.packets = new EncapsulatedPacket[(int) expectedLength];
    }

    @Nullable
    public EncapsulatedPacket add(EncapsulatedPacket packet, RakNetSession session) {
        Preconditions.checkNotNull(packet, "packet");
        Preconditions.checkArgument(packet.isSplit(), "packet is not split");
        Preconditions.checkState(this.refCnt() > 0, "packet has been released");
        Preconditions.checkElementIndex((int) packet.getPartIndex(), this.packets.length);

        int partIndex = (int) packet.getPartIndex();
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

    public boolean expired() {
        // If we're waiting on a split packet for more than 30 seconds, the client on the other end is either severely
        // lagging, or has died.
        Preconditions.checkState(this.refCnt() > 0, "packet has been released");
        return System.currentTimeMillis() - created >= 30000;
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
