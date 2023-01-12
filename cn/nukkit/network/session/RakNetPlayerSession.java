/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.RakNetInterface;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.session.NetworkPlayerSession;
import cn.nukkit.utils.BinaryStream;
import com.google.common.base.Preconditions;
import com.nukkitx.network.raknet.EncapsulatedPacket;
import com.nukkitx.network.raknet.RakNetServerSession;
import com.nukkitx.network.raknet.RakNetSessionListener;
import com.nukkitx.network.raknet.RakNetState;
import com.nukkitx.network.util.DisconnectReason;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;

public class RakNetPlayerSession
implements NetworkPlayerSession,
RakNetSessionListener {
    private static final Logger h = LogManager.getLogger(RakNetPlayerSession.class);
    private final RakNetInterface i;
    private final RakNetServerSession c;
    private final Queue<DataPacket> e = PlatformDependent.newSpscQueue();
    private final Queue<DataPacket> a = PlatformDependent.newMpscQueue();
    private final ScheduledFuture<?> g;
    private Player b;
    private String f = null;
    private CompressionProvider d;

    public RakNetPlayerSession(RakNetInterface rakNetInterface, RakNetServerSession rakNetServerSession) {
        this.i = rakNetInterface;
        this.c = rakNetServerSession;
        this.g = rakNetServerSession.getEventLoop().scheduleAtFixedRate(this::a, 0L, 50L, TimeUnit.MILLISECONDS);
        this.d = rakNetServerSession.getProtocolVersion() >= 11 ? CompressionProvider.NONE : (rakNetServerSession.getProtocolVersion() < 10 ? CompressionProvider.ZLIB : CompressionProvider.ZLIB_RAW);
    }

    @Override
    public void onEncapsulated(EncapsulatedPacket encapsulatedPacket) {
        ByteBuf byteBuf = encapsulatedPacket.getBuffer();
        short s2 = byteBuf.readUnsignedByte();
        if (s2 == 254) {
            byte[] byArray = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(byArray);
            try {
                int n = this.b == null ? ProtocolInfo.CURRENT_PROTOCOL : this.b.protocol;
                this.i.getNetwork().processBatch(byArray, this.e, this.getCompression(), n, this.c.getProtocolVersion());
            }
            catch (Exception exception) {
                this.disconnect("Sent malformed packet");
                h.error("[{}] Unable to process batch packet", this.b == null ? this.c.getAddress() : this.b.getName(), (Object)exception);
            }
        }
    }

    @Override
    public void onDirect(ByteBuf byteBuf) {
    }

    @Override
    public void onSessionChangeState(RakNetState rakNetState) {
    }

    @Override
    public void onDisconnect(DisconnectReason disconnectReason) {
        if (disconnectReason == DisconnectReason.TIMED_OUT) {
            this.disconnect("Timed out");
        } else {
            this.disconnect("Disconnected from Server");
        }
    }

    @Override
    public void disconnect(String string) {
        if (this.f != null) {
            return;
        }
        this.f = string;
        if (this.g != null) {
            this.g.cancel(false);
        }
        this.c.getEventLoop().schedule(() -> this.c.close(), 10L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendPacket(DataPacket dataPacket) {
        if (this.c.isClosed()) {
            return;
        }
        if (dataPacket.protocol != this.b.protocol) {
            h.warn("Wrong protocol used for {}! expected {} got{}", (Object)dataPacket.getClass().getSimpleName(), (Object)this.b.protocol, (Object)dataPacket.protocol);
        }
        if (dataPacket.pid() != -1) {
            dataPacket.tryEncode();
        }
        this.a.offer(dataPacket);
    }

    @Override
    public void sendImmediatePacket(DataPacket dataPacket, Runnable runnable) {
        if (this.c.isClosed()) {
            return;
        }
        this.sendPacket(dataPacket);
        this.c.getEventLoop().execute(() -> {
            this.a();
            runnable.run();
        });
    }

    @Override
    public void flush() {
        this.c.getEventLoop().execute(this::a);
    }

    private void a() {
        if (this.c.isClosed()) {
            return;
        }
        try {
            DataPacket dataPacket;
            ObjectArrayList<DataPacket> objectArrayList = new ObjectArrayList<DataPacket>();
            while ((dataPacket = this.a.poll()) != null) {
                if (dataPacket.pid() == -1) {
                    if (!objectArrayList.isEmpty()) {
                        this.a(objectArrayList);
                        objectArrayList.clear();
                    }
                    this.a(((BatchPacket)dataPacket).payload);
                    continue;
                }
                objectArrayList.add(dataPacket);
            }
            if (!objectArrayList.isEmpty()) {
                this.a(objectArrayList);
            }
        }
        catch (Exception exception) {
            h.error("[{}] Failed to tick RakNetPlayerSession", (Object)this.c.getAddress(), (Object)exception);
        }
    }

    public void serverTick() {
        DataPacket dataPacket;
        while ((dataPacket = this.e.poll()) != null) {
            try {
                this.b.handleDataPacket(dataPacket);
            }
            catch (Exception exception) {
                h.error(new FormattedMessage("An error occurred whilst handling {} for {}", new Object[]{dataPacket.getClass().getSimpleName(), this.b.getName()}, exception));
            }
        }
    }

    private void a(Collection<DataPacket> collection) {
        BinaryStream binaryStream = new BinaryStream();
        for (DataPacket dataPacket : collection) {
            Preconditions.checkArgument(!(dataPacket instanceof BatchPacket), "Cannot batch BatchPacket");
            Preconditions.checkState(dataPacket.isEncoded, "Packet should have already been encoded");
            byte[] byArray = dataPacket.getBuffer();
            binaryStream.putUnsignedVarInt(byArray.length);
            binaryStream.put(byArray);
        }
        try {
            this.a(this.d.compress(binaryStream, Server.getInstance().networkCompressionLevel));
        }
        catch (Exception exception) {
            h.error("Unable to compress batched packets", (Throwable)exception);
        }
    }

    private void a(byte[] byArray) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer(1 + byArray.length);
        byteBuf.writeByte(254);
        byteBuf.writeBytes(byArray);
        this.c.send(byteBuf);
    }

    @Override
    public void setCompression(CompressionProvider compressionProvider) {
        Preconditions.checkNotNull(compressionProvider);
        this.d = compressionProvider;
    }

    @Override
    public CompressionProvider getCompression() {
        return this.d;
    }

    public void setPlayer(Player player) {
        Preconditions.checkArgument(this.b == null && player != null);
        this.b = player;
    }

    @Override
    public Player getPlayer() {
        return this.b;
    }

    public RakNetServerSession getRakNetSession() {
        return this.c;
    }

    public String getDisconnectReason() {
        return this.f;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

