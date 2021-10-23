package com.nukkitx.network.raknet;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import com.nukkitx.network.SessionConnection;
import com.nukkitx.network.raknet.util.*;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.nukkitx.network.raknet.RakNetConstants.*;

@ParametersAreNonnullByDefault
public abstract class RakNetSession implements SessionConnection<ByteBuf> {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(RakNetSession.class);

    final InetSocketAddress address;
    final Channel channel;
    final EventLoop eventLoop;
    final int protocolVersion;
    private int mtu;
    private int adjustedMtu; // Used in datagram calculations
    long guid;
    private volatile RakNetState state = RakNetState.UNCONNECTED;
    private volatile long lastTouched = System.currentTimeMillis();
    private volatile boolean closed = false;

    // Reliability, Ordering, Sequencing and datagram indexes
    private RakNetSlidingWindow slidingWindow;
    private int splitIndex;
    private int datagramReadIndex;
    private int datagramWriteIndex;
    private int reliabilityReadIndex;
    private int reliabilityWriteIndex;
    private int[] orderReadIndex;
    private int[] orderWriteIndex;

    private RoundRobinArray<SplitPacketHelper> splitPackets;
    private BitQueue reliableDatagramQueue;

    private FastBinaryMinHeap<EncapsulatedPacket> outgoingPackets;
    private long[] outgoingPacketNextWeights;
    private FastBinaryMinHeap<EncapsulatedPacket>[] orderingHeaps;
    @Getter
    @Setter
    private volatile RakNetSessionListener listener = null;
    private volatile long currentPingTime = -1;
    private volatile long lastPingTime = -1;
    private volatile long lastPongTime = -1;
    private ConcurrentMap<Integer, RakNetDatagram> sentDatagrams;
    private Queue<IntRange> incomingAcks;
    private Queue<IntRange> incomingNaks;
    private Queue<IntRange> outgoingAcks;
    private Queue<IntRange> outgoingNaks;
    private int unackedBytes;
    private long lastMinWeight;

    RakNetSession(InetSocketAddress address, Channel channel, EventLoop eventLoop, int mtu, int protocolVersion) {
        this.address = address;
        this.channel = channel;
        this.eventLoop = eventLoop;
        this.setMtu(mtu);
        this.protocolVersion = protocolVersion;
    }

    final void initialize() {
        Preconditions.checkState(this.state == RakNetState.INITIALIZING);

        this.slidingWindow = new RakNetSlidingWindow(this.mtu);

        this.reliableDatagramQueue = new BitQueue(512);
        this.orderReadIndex = new int[MAXIMUM_ORDERING_CHANNELS];
        this.orderWriteIndex = new int[MAXIMUM_ORDERING_CHANNELS];

        //noinspection unchecked
        this.orderingHeaps = new FastBinaryMinHeap[MAXIMUM_ORDERING_CHANNELS];
        this.splitPackets = new RoundRobinArray<>(256);
        this.sentDatagrams = new ConcurrentSkipListMap<>();
        for (int i = 0; i < MAXIMUM_ORDERING_CHANNELS; i++) {
            orderingHeaps[i] = new FastBinaryMinHeap<>(64);
        }

        this.outgoingPackets = new FastBinaryMinHeap<>(8);

        this.incomingAcks = PlatformDependent.newMpscQueue();
        this.incomingNaks = PlatformDependent.newMpscQueue();
        this.outgoingAcks = PlatformDependent.newMpscQueue();
        this.outgoingNaks = PlatformDependent.newMpscQueue();

        this.outgoingPacketNextWeights = new long[4];
        this.initHeapWeights();
    }

    private void deinitialize() {
        // Perform resource clean up.
        if (this.splitPackets != null) {
            this.splitPackets.forEach(ReferenceCountUtil::release);
        }
        if (this.sentDatagrams != null) {
            this.sentDatagrams.values().forEach(ReferenceCountUtil::release);
        }

        FastBinaryMinHeap<EncapsulatedPacket>[] orderingHeaps = this.orderingHeaps;
        this.orderingHeaps = null;
        if (orderingHeaps != null) {
            for (FastBinaryMinHeap<EncapsulatedPacket> orderingHeap : orderingHeaps) {
                EncapsulatedPacket packet;
                while ((packet = orderingHeap.poll()) != null) {
                    packet.release();
                }
            }
        }

        FastBinaryMinHeap<EncapsulatedPacket> outgoingPackets = this.outgoingPackets;
        this.outgoingPackets = null;
        if (outgoingPackets != null) {
            EncapsulatedPacket packet;
            while ((packet = outgoingPackets.poll()) != null) {
                packet.release();
            }
        }
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public int getMtu() {
        return this.mtu;
    }

    void setMtu(int mtu) {
        this.mtu = RakNetUtils.clamp(mtu, MINIMUM_MTU_SIZE, MAXIMUM_MTU_SIZE);
        this.adjustedMtu = (this.mtu - UDP_HEADER_SIZE) - (this.address.getAddress() instanceof Inet6Address ? 40 : 20);
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public long getPing() {
        return this.lastPongTime - this.lastPingTime;
    }

    public ByteBuf allocateBuffer(int capacity) {
        return this.channel.alloc().ioBuffer(capacity);
    }

    private void initHeapWeights() {
        for (int priorityLevel = 0; priorityLevel < 4; priorityLevel++) {
            this.outgoingPacketNextWeights[priorityLevel] = (1 << priorityLevel) * priorityLevel + priorityLevel;
        }
    }

    private long getNextWeight(RakNetPriority priority) {
        int priorityLevel = priority.ordinal();
        long next = this.outgoingPacketNextWeights[priorityLevel];

        if (!this.outgoingPackets.isEmpty()) {
            if (next >= this.lastMinWeight) {
                next = this.lastMinWeight + (1 << priorityLevel) * priorityLevel + priorityLevel;
                this.outgoingPacketNextWeights[priorityLevel] = next + (1 << priorityLevel) * (priorityLevel + 1) + priorityLevel;
            }
        } else {
            this.initHeapWeights();
        }
        this.lastMinWeight = next - (1 << priorityLevel) * priorityLevel + priorityLevel;
        return next;
    }

    private EncapsulatedPacket getReassembledPacket(EncapsulatedPacket splitPacket) {
        this.checkForClosed();

        SplitPacketHelper helper = this.splitPackets.get(splitPacket.getPartId());
        if (helper == null) {
            this.splitPackets.set(splitPacket.getPartId(), helper = new SplitPacketHelper(splitPacket.getPartCount()));
        }

        // Try reassembling the packet.
        EncapsulatedPacket result = helper.add(splitPacket, this);
        if (result != null) {
            // Packet reassembled. Remove the helper
            if (this.splitPackets.remove(splitPacket.getPartId(), helper)) {
                helper.release();
            }
        }

        return result;
    }

    public void onDatagram(ByteBuf buffer) {
        try {
            if (this.isClosed()) {
                return;
            }
            this.touch();

            byte potentialFlags = buffer.readByte();
            boolean rakNetDatagram = (potentialFlags & FLAG_VALID) != 0;
            if (!rakNetDatagram) {
                // Received non-datagram packet
                buffer.readerIndex(0);
                this.onPacketInternal(buffer);
                return;
            }

            if (this.state == null || this.state.ordinal() < RakNetState.INITIALIZED.ordinal()) {
                // Block RakNet datagrams if we haven't initialized the session yet.
                return;
            }

            // Check if we have received acknowledge datagram
            if ((potentialFlags & FLAG_ACK) != 0) {
                this.onAcknowledge(buffer, this.incomingAcks, false);
            } else if ((potentialFlags & FLAG_NACK) != 0) {
                this.onAcknowledge(buffer, this.incomingNaks, true);
            } else {
                buffer.readerIndex(0);
                this.onRakNetDatagram(buffer);
            }
        } catch (Exception ex) {
            Server.getInstance().getLogger().error("Received bad datagram from " + this.address, ex);
            this.disconnect(DisconnectReason.BAD_PACKET);
        } finally {
            buffer.release();
        }
    }

    private void onEncapsulatedInternal(EncapsulatedPacket packet) {
        ByteBuf buffer = packet.buffer;
        short packetId = buffer.readUnsignedByte();
        switch (packetId) {
            case ID_CONNECTED_PING:
                this.onConnectedPing(buffer);
                break;
            case ID_CONNECTED_PONG:
                this.onConnectedPong(buffer);
                break;
            case ID_DISCONNECTION_NOTIFICATION:
                this.onDisconnectionNotification();
                break;
            default:
                buffer.readerIndex(0);
                if (packetId >= ID_USER_PACKET_ENUM) {
                    // Forward to user
                    if (this.listener != null) {
                        this.listener.onEncapsulated(packet);
                    }
                } else {
                    this.onPacket(buffer);
                }
                break;
        }
    }

    private void onPacketInternal(ByteBuf buffer) {
        short packetId = buffer.getUnsignedByte(buffer.readerIndex());
        buffer.readerIndex(0);
        if (packetId >= ID_USER_PACKET_ENUM) {
            // Forward to user
            if (this.listener != null) {
                this.listener.onDirect(buffer);
            }
        } else {
            this.onPacket(buffer);
        }
    }

    protected abstract void onPacket(ByteBuf buffer);

    private void onRakNetDatagram(ByteBuf buffer) {
        if (this.state == null || RakNetState.INITIALIZED.compareTo(this.state) > 0) {
            return;
        }
        RakNetDatagram datagram = new RakNetDatagram(System.currentTimeMillis());
        datagram.decode(buffer);

        this.slidingWindow.onPacketReceived(datagram.sendTime);

        int prevSequenceIndex = this.datagramReadIndex;
        if (prevSequenceIndex < datagram.sequenceIndex) {
            this.datagramReadIndex = datagram.sequenceIndex + 1;
        }

        int missedDatagrams = datagram.sequenceIndex - prevSequenceIndex;

        if (missedDatagrams > 0) {
            this.outgoingNaks.offer(new IntRange(datagram.sequenceIndex - missedDatagrams, datagram.sequenceIndex));
        }

        this.outgoingAcks.offer(new IntRange(datagram.sequenceIndex, datagram.sequenceIndex));

        for (final EncapsulatedPacket encapsulated : datagram.packets) {
            if (encapsulated.reliability.isReliable()) {
                int missed = encapsulated.reliabilityIndex - this.reliabilityReadIndex;

                if (missed > 0) {
                    if (missed < this.reliableDatagramQueue.size()) {
                        if (this.reliableDatagramQueue.get(missed)) {
                            this.reliableDatagramQueue.set(missed, false);
                        } else {
                            // Duplicate packet
                            continue;
                        }
                    } else {
                        int count = (missed - this.reliableDatagramQueue.size());
                        for (int i = 0; i < count; i++) {
                            this.reliableDatagramQueue.add(true);
                        }

                        this.reliableDatagramQueue.add(false);
                    }
                } else if (missed == 0) {
                    this.reliabilityReadIndex++;
                    if (!this.reliableDatagramQueue.isEmpty()) {
                        this.reliableDatagramQueue.poll();
                    }
                } else {
                    // Duplicate packet
                    continue;
                }

                while (!this.reliableDatagramQueue.isEmpty() && !this.reliableDatagramQueue.peek()) {
                    this.reliableDatagramQueue.poll();
                    ++this.reliabilityReadIndex;
                }
            }


            if (encapsulated.split) {
                final EncapsulatedPacket reassembled = this.getReassembledPacket(encapsulated);
                if (reassembled == null) {
                    // Not reassembled
                    continue;
                }
                try {
                    this.checkForOrdered(reassembled);
                } finally {
                    reassembled.release();
                }
            } else {
                this.checkForOrdered(encapsulated);
            }
        }
    }

    private void checkForOrdered(EncapsulatedPacket packet) {
        if (packet.getReliability().isOrdered()) {
            this.onOrderedReceived(packet);
        } else {
            this.onEncapsulatedInternal(packet);
        }
    }

    private void onOrderedReceived(EncapsulatedPacket packet) {
        FastBinaryMinHeap<EncapsulatedPacket> binaryHeap = this.orderingHeaps[packet.orderingChannel];

        if (this.orderReadIndex[packet.orderingChannel] < packet.orderingIndex) {
            // Not next in line so add to queue.
            binaryHeap.insert(packet.orderingIndex, packet.retain());
            return;
        } else if (this.orderReadIndex[packet.orderingChannel] > packet.orderingIndex) {
            // We already have this
            return;
        }
        this.orderReadIndex[packet.orderingChannel]++;

        // Can be handled
        this.onEncapsulatedInternal(packet);

        EncapsulatedPacket queuedPacket;
        while ((queuedPacket = binaryHeap.peek()) != null) {
            if (queuedPacket.orderingIndex == this.orderReadIndex[packet.orderingChannel]) {
                try {
                    // We got the expected packet
                    binaryHeap.remove();
                    this.orderReadIndex[packet.orderingChannel]++;

                    this.onEncapsulatedInternal(queuedPacket);
                } finally {
                    queuedPacket.release();
                }
            } else {
                // Found a gap. Wait till we start receive another ordered packet.
                break;
            }
        }
    }

    final void onTick(long curTime) {
        if (this.isClosed()) {
            return;
        }
        this.tick(curTime);
    }

    protected void tick(long curTime) {
        if (this.isTimedOut(curTime)) {
            this.close(DisconnectReason.TIMED_OUT);
            return;
        }

        if (this.state == null || this.state.ordinal() < RakNetState.INITIALIZED.ordinal()) {
            return;
        }

        if (this.currentPingTime + 2000L < curTime) {
            this.sendConnectedPing(curTime);
        }

        this.handleIncomingAcknowledge(curTime, this.incomingAcks, false);
        this.handleIncomingAcknowledge(curTime, this.incomingNaks, true);

        // Send known outgoing acknowledge packets
        final int mtu = this.adjustedMtu - RAKNET_DATAGRAM_HEADER_SIZE;
        while (!this.outgoingNaks.isEmpty()) {
            ByteBuf buffer = this.allocateBuffer(mtu);
            buffer.writeByte(FLAG_VALID | FLAG_NACK);
            RakNetUtils.writeIntRanges(buffer, this.outgoingNaks, mtu - 1);
            this.sendDirect(buffer);
        }

        if (this.slidingWindow.shouldSendAcks(curTime)) {
            while (!this.outgoingAcks.isEmpty()) {
                ByteBuf buffer = this.allocateBuffer(mtu);
                buffer.writeByte(FLAG_VALID | FLAG_ACK);
                RakNetUtils.writeIntRanges(buffer, this.outgoingAcks, mtu - 1);
                this.sendDirect(buffer);
                this.slidingWindow.onSendAck();
            }
        }

        // Send packets that are stale first. This function returns whether to continue
        // to send rest of the datagrams, as it might close the session due to too many stale datagrams
        if (this.sendStaleDatagrams(curTime)) {
            // Now send usual packets
            this.sendDatagrams(curTime);
            // Finally, flush channel
            this.channel.flush();
        }
    }

    private void handleIncomingAcknowledge(long curTime, Queue<IntRange> queue, boolean nack) {
        if (queue.isEmpty()) {
            return;
        }

        if (nack) {
            this.slidingWindow.onNak();
        }

        IntRange range;
        while ((range = queue.poll()) != null) {
            for (int i = range.start; i <= range.end; i++) {
                RakNetDatagram datagram = this.sentDatagrams.remove(i);
                if (datagram != null) {
                    if (nack) {
                        this.onIncomingNack(datagram, curTime);
                    } else {
                        this.onIncomingAck(datagram, curTime);
                    }
                }
            }
        }
    }

    private void onIncomingAck(RakNetDatagram datagram, long curTime) {
        try {
            this.unackedBytes -= datagram.getSize();
            this.slidingWindow.onAck(curTime - datagram.sendTime, datagram.sequenceIndex, this.datagramReadIndex);
        } finally {
            datagram.release();
        }
    }

    private void onIncomingNack(RakNetDatagram datagram, long curTime) {
        if (Nukkit.DEBUG > 2) {
            log.trace("NAK'ed datagram {} from {}", datagram.sequenceIndex, this.address);
        }
        this.sendDatagram(datagram, curTime);
    }

    private boolean sendStaleDatagrams(long curTime) {
        if (this.sentDatagrams.isEmpty()) {
            return true;
        }

        int resendCount = 0;
        int transmissionBandwidth = this.slidingWindow.getRetransmissionBandwidth(this.unackedBytes);

        for (RakNetDatagram datagram : this.sentDatagrams.values()) {
            if (datagram.getNextSend() <= curTime) {
                int size = datagram.getSize();
                if (transmissionBandwidth < size) {
                    break;
                }
                transmissionBandwidth -= size;
                resendCount++;
                this.sendDatagram(datagram, curTime);
            }
        }

        if (resendCount > MAXIMUM_STALE_DATAGRAMS) {
            if (Nukkit.DEBUG > 1) {
                log.debug("Too many stale datagrams from " + this.address);
            }
            this.close(DisconnectReason.TIMED_OUT);
            return false;
        }

        if (resendCount > 0) {
            this.slidingWindow.onResend(curTime);
        }

        return true;
    }

    private void sendDatagrams(long curTime) {
        if (this.outgoingPackets.isEmpty()) {
            return;
        }

        int transmissionBandwidth = this.slidingWindow.getTransmissionBandwidth(this.unackedBytes);
        RakNetDatagram datagram = new RakNetDatagram(curTime);
        EncapsulatedPacket packet;

        while ((packet = this.outgoingPackets.peek()) != null) {
            int size = packet.getSize();
            if (transmissionBandwidth < size) {
                break;
            }

            transmissionBandwidth -= size;
            this.outgoingPackets.remove();

            // Send full datagram
            if (!datagram.tryAddPacket(packet, this.adjustedMtu)) {
                this.sendDatagram(datagram, curTime);

                datagram = new RakNetDatagram(curTime);
                if (!datagram.tryAddPacket(packet, this.adjustedMtu)) {
                    throw new IllegalArgumentException("Packet too large to fit in MTU (size: " + packet.getSize() + ", MTU: " + this.adjustedMtu + ')');
                }
            }
        }

        if (!datagram.getPackets().isEmpty()) {
            this.sendDatagram(datagram, curTime);
        }
    }

    @Override
    public void disconnect() {
        this.disconnect(DisconnectReason.DISCONNECTED);
    }

    @Override
    public void disconnect(DisconnectReason reason) {
        if (!this.isClosed()) {
            this.eventLoop.execute(() -> this.disconnect0(reason));
        }
    }

    private void disconnect0(DisconnectReason reason) {
        if (!this.isClosed()) {
            this.sendDisconnectionNotification();
            this.close0(reason);
        }
    }

    @Override
    public void close() {
        this.close(DisconnectReason.DISCONNECTED);
    }

    @Override
    public void close(DisconnectReason reason) {
        if (!this.isClosed()) {
            this.eventLoop.execute(() -> this.close0(reason));
        }
    }

    private void close0(DisconnectReason reason) {
        if (this.isClosed()) {
            return;
        }

        this.closed = true;
        this.state = RakNetState.UNCONNECTED;
        this.onClose();
        if (Nukkit.DEBUG > 2) {
            log.trace("RakNet Session ({} => {}) closed: {}", this.getRakNet().getBindAddress(), this.address, reason);
        }

        this.deinitialize();
        if (this.listener != null) {
            this.listener.onDisconnect(reason);
        }
    }

    protected void onClose() {
    }

    @Override
    public void sendImmediate(ByteBuf buf) {
        this.send(buf, RakNetPriority.IMMEDIATE);
    }

    @Override
    public void send(ByteBuf buf) {
        this.send(buf, RakNetPriority.MEDIUM);
    }

    public void send(ByteBuf buf, RakNetPriority priority) {
        this.send(buf, priority, RakNetReliability.RELIABLE_ORDERED);
    }

    public void send(ByteBuf buf, RakNetReliability reliability) {
        this.send(buf, RakNetPriority.MEDIUM, reliability);
    }

    public void send(ByteBuf buf, RakNetPriority priority, RakNetReliability reliability) {
        this.send(buf, priority, reliability, 0);
    }

    public void send(ByteBuf buf, RakNetPriority priority, RakNetReliability reliability, @Nonnegative int orderingChannel) {
        if (this.eventLoop.inEventLoop()) {
            this.send0(buf, priority, reliability, orderingChannel);
        } else {
            this.eventLoop.execute(() -> this.send0(buf, priority, reliability, orderingChannel));
        }
    }

    private void send0(ByteBuf buf, RakNetPriority priority, RakNetReliability reliability, @Nonnegative int orderingChannel) {
        try {
            if (this.isClosed() || state == null || state.ordinal() < RakNetState.INITIALIZED.ordinal()) {
                // Session is not ready for RakNet datagrams.
                return;
            }
            EncapsulatedPacket[] packets = this.createEncapsulated(buf, priority, reliability, orderingChannel);

            if (priority == RakNetPriority.IMMEDIATE) {
                this.sendImmediate(packets);
                return;
            }

            long weight = this.getNextWeight(priority);
            if (packets.length == 1) {
                this.outgoingPackets.insert(weight, packets[0]);
            } else {
                this.outgoingPackets.insertSeries(weight, packets);
            }
        } finally {
            buf.release();
        }
    }

    private void sendImmediate(EncapsulatedPacket[] packets) {
        long curTime = System.currentTimeMillis();

        for (EncapsulatedPacket packet : packets) {
            RakNetDatagram datagram = new RakNetDatagram(curTime);

            if (!datagram.tryAddPacket(packet, this.adjustedMtu)) {
                throw new IllegalArgumentException("Packet too large to fit in MTU (size: " + packet.getSize() +
                        ", MTU: " + this.adjustedMtu + ')');
            }
            this.sendDatagram(datagram, curTime);
        }
        this.channel.flush();
    }

    private EncapsulatedPacket[] createEncapsulated(ByteBuf buffer, RakNetPriority priority, RakNetReliability reliability,
                                                    int orderingChannel) {
        int maxLength = this.adjustedMtu - MAXIMUM_ENCAPSULATED_HEADER_SIZE - RAKNET_DATAGRAM_HEADER_SIZE;

        ByteBuf[] buffers;
        int splitId = 0;

        if (buffer.readableBytes() > maxLength) {
            // Packet requires splitting
            // Adjust reliability
            switch (reliability) {
                case UNRELIABLE:
                    reliability = RakNetReliability.RELIABLE;
                    break;
                case UNRELIABLE_SEQUENCED:
                    reliability = RakNetReliability.RELIABLE_SEQUENCED;
                    break;
                case UNRELIABLE_WITH_ACK_RECEIPT:
                    reliability = RakNetReliability.RELIABLE_WITH_ACK_RECEIPT;
                    break;
            }

            int split = ((buffer.readableBytes() - 1) / maxLength) + 1;
            buffer.retain(split);

            buffers = new ByteBuf[split];
            for (int i = 0; i < split; i++) {
                buffers[i] = buffer.readSlice(Math.min(maxLength, buffer.readableBytes()));
            }
            if (buffer.isReadable()) {
                throw new IllegalStateException("Buffer still has bytes to read!");
            }

            // Allocate split ID
            splitId = this.splitIndex++;
        } else {
            buffers = new ByteBuf[]{buffer.readRetainedSlice(buffer.readableBytes())};
        }

        // Set meta
        int orderingIndex = 0;
        /*int sequencingIndex = 0;
        if (reliability.isSequenced()) {
            sequencingIndex = this.sequenceWriteIndex.getAndIncrement(orderingChannel);
        } todo: sequencing */
        if (reliability.isOrdered()) {
            orderingIndex = this.orderWriteIndex[orderingChannel]++;
        }

        // Now create the packets.
        EncapsulatedPacket[] packets = new EncapsulatedPacket[buffers.length];
        for (int i = 0, parts = buffers.length; i < parts; i++) {
            EncapsulatedPacket packet = new EncapsulatedPacket();
            packet.buffer = buffers[i];
            packet.orderingChannel = (short) orderingChannel;
            packet.orderingIndex = orderingIndex;
            //packet.setSequenceIndex(sequencingIndex);
            packet.reliability = reliability;
            if (reliability.isReliable()) {
                packet.reliabilityIndex = this.reliabilityWriteIndex++;
            }

            if (parts > 1) {
                packet.split = true;
                packet.partIndex = i;
                packet.partCount = parts;
                packet.partId = splitId;
            }

            packets[i] = packet;
        }
        return packets;
    }

    private void sendDatagram(RakNetDatagram datagram, long time) {
        Preconditions.checkArgument(!datagram.packets.isEmpty(), "RakNetDatagram with no packets");
        try {
            int oldIndex = datagram.sequenceIndex;
            datagram.sequenceIndex = this.datagramWriteIndex++;

            for (EncapsulatedPacket packet : datagram.packets) {
                // check if packet is reliable so it can be resent later if a NAK is received.
                if (packet.reliability != RakNetReliability.UNRELIABLE &&
                        packet.reliability != RakNetReliability.UNRELIABLE_SEQUENCED) {
                    datagram.nextSend = time + this.slidingWindow.getRtoForRetransmission();
                    if (oldIndex == -1) {
                        this.unackedBytes += datagram.getSize();
                    } else {
                        this.sentDatagrams.remove(oldIndex, datagram);
                    }
                    this.sentDatagrams.put(datagram.sequenceIndex, datagram.retain()); // Keep for resending
                    break;
                }
            }
            ByteBuf buf = this.allocateBuffer(datagram.getSize());
            Preconditions.checkArgument(buf.writerIndex() < this.adjustedMtu, "Packet length was %s but expected %s", buf.writerIndex(), this.adjustedMtu);
            datagram.encode(buf);
            this.channel.write(new DatagramPacket(buf, this.address));
        } finally {
            datagram.release();
        }
    }

    void sendDirect(ByteBuf buffer) {
        this.channel.writeAndFlush(new DatagramPacket(buffer, this.address));
    }

    /*
     * Packet Handlers
     */

    private void onAcknowledge(ByteBuf buffer, Queue<IntRange> queue, boolean nack) {
        this.checkForClosed();
        int size = buffer.readUnsignedShort();
        for (int i = 0; i < size; i++) {
            boolean singleton = buffer.readBoolean();
            int start = buffer.readUnsignedMediumLE();
            // We don't need the upper limit if it's a singleton
            int end = singleton ? start : buffer.readMediumLE();
            if (start > end) {
                if (Nukkit.DEBUG > 2) {
                    log.trace("{} sent an IntRange with a start value {} greater than an end value of {}", this.address,
                            start, end);
                }
                this.disconnect(DisconnectReason.BAD_PACKET);
                return;
            }
            queue.offer(new IntRange(start, end));
        }
    }

    private void onConnectedPing(ByteBuf buffer) {
        long pingTime = buffer.readLong();
        this.sendConnectedPong(pingTime);
    }

    private void onConnectedPong(ByteBuf buffer) {
        long pingTime = buffer.readLong();
        if (this.currentPingTime == pingTime) {
            this.lastPingTime = this.currentPingTime;
            this.lastPongTime = System.currentTimeMillis();
        }
    }

    private void onDisconnectionNotification() {
        this.close(DisconnectReason.CLOSED_BY_REMOTE_PEER);
    }

    /*
     * Packet Dispatchers
     */

    private void sendConnectedPing(long pingTime) {
        ByteBuf buffer = this.allocateBuffer(9);
        buffer.writeByte(ID_CONNECTED_PING);
        buffer.writeLong(pingTime);
        this.send(buffer, RakNetPriority.IMMEDIATE, RakNetReliability.RELIABLE);
        this.currentPingTime = pingTime;
    }

    private void sendConnectedPong(long pingTime) {
        ByteBuf buffer = this.allocateBuffer(17);
        buffer.writeByte(ID_CONNECTED_PONG);
        buffer.writeLong(pingTime);
        buffer.writeLong(System.currentTimeMillis());
        this.send(buffer, RakNetPriority.IMMEDIATE, RakNetReliability.RELIABLE);
    }

    private void sendDisconnectionNotification() {
        ByteBuf buffer = this.allocateBuffer(1);
        buffer.writeByte(ID_DISCONNECTION_NOTIFICATION);
        this.send(buffer, RakNetPriority.IMMEDIATE, RakNetReliability.RELIABLE_ORDERED);
    }

    private void touch() {
        this.checkForClosed();
        this.lastTouched = System.currentTimeMillis();
    }

    public boolean isTimedOut(long curTime) {
        return curTime - this.lastTouched >= SESSION_TIMEOUT_MS;
    }

    private void checkForClosed() {
        Preconditions.checkState(!this.isClosed(), "Session already closed");
    }

    public boolean isClosed() {
        return this.closed;
    }

    public abstract RakNet getRakNet();

    boolean isIpv6Session() {
        return this.address.getAddress() instanceof Inet6Address;
    }

    public RakNetState getState() {
        return state;
    }

    void setState(@Nullable RakNetState state) {
        if (this.state != state) {
            this.state = state;
            if (this.listener != null) {
                this.listener.onSessionChangeState(this.state);
            }
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public EventLoop getEventLoop() {
        return eventLoop;
    }
}
