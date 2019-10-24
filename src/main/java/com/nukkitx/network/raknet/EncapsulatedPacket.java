package com.nukkitx.network.raknet;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class EncapsulatedPacket implements ReferenceCounted {

    RakNetReliability reliability;
    int reliabilityIndex;
    int sequenceIndex;
    int orderingIndex;
    short orderingChannel;
    boolean split;
    int partCount;
    int partId;
    int partIndex;
    ByteBuf buffer;

    public void encode(ByteBuf buf) {
        int flags = reliability.ordinal() << 5;
        if (split) {
            flags |= 0b00010000;
        }
        buf.writeByte(flags); // flags
        buf.writeShort(buffer.readableBytes() << 3); // size

        if (reliability.isReliable()) {
            buf.writeMediumLE(reliabilityIndex);
        }

        if (reliability.isSequenced()) {
            buf.writeMediumLE(sequenceIndex);
        }

        if (reliability.isOrdered() || reliability.isSequenced()) {
            buf.writeMediumLE(orderingIndex);
            buf.writeByte(orderingChannel);
        }

        if (split) {
            buf.writeInt(partCount);
            buf.writeShort(partId);
            buf.writeInt(partIndex);
        }

        buf.writeBytes(this.buffer, this.buffer.readerIndex(), this.buffer.readableBytes());
        // If we need to resend, we don't want the buffer's reader index changing.
    }

    public void decode(ByteBuf buf) {
        byte flags = buf.readByte();
        reliability = RakNetReliability.fromId((flags & 0b11100000) >> 5);
        split = (flags & 0b00010000) != 0;
        int size = (buf.readUnsignedShort() + 7) >> 3;

        if (reliability.isReliable()) {
            reliabilityIndex = buf.readUnsignedMediumLE();
        }

        if (reliability.isSequenced()) {
            sequenceIndex = buf.readUnsignedMediumLE();
        }

        if (reliability.isOrdered() || reliability.isSequenced()) {
            orderingIndex = buf.readUnsignedMediumLE();
            orderingChannel = buf.readUnsignedByte();
        }

        if (split) {
            partCount = buf.readInt();
            partId = buf.readUnsignedShort();
            partIndex = buf.readInt();
        }

        // Slice the buffer to use less memory
        buffer = buf.readSlice(size);
    }

    public int getSize() {
        // Include back of the envelope calculation
        return 3 + this.reliability.getSize() + (this.split ? 10 : 0) + this.buffer.readableBytes();
    }

    public EncapsulatedPacket fromSplit(ByteBuf reassembled) {
        EncapsulatedPacket packet = new EncapsulatedPacket();
        packet.reliability = this.reliability;
        packet.reliabilityIndex = this.reliabilityIndex;
        packet.sequenceIndex = this.sequenceIndex;
        packet.orderingIndex = this.orderingIndex;
        packet.orderingChannel = this.orderingChannel;
        packet.buffer = reassembled;
        return packet;
    }

    @Override
    public int refCnt() {
        return buffer.refCnt();
    }

    @Override
    public EncapsulatedPacket retain() {
        this.buffer.retain();
        return this;
    }

    @Override
    public EncapsulatedPacket retain(int i) {
        this.buffer.retain(i);
        return this;
    }

    @Override
    public EncapsulatedPacket touch() {
        this.buffer.touch();
        return this;
    }

    @Override
    public EncapsulatedPacket touch(Object o) {
        this.buffer.touch(o);
        return this;
    }

    @Override
    public boolean release() {
        return buffer.release();
    }

    @Override
    public boolean release(int i) {
        return buffer.release(i);
    }
}

