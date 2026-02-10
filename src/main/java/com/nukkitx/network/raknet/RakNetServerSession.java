package com.nukkitx.network.raknet;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import com.nukkitx.network.NetworkUtils;
import com.nukkitx.network.util.DisconnectReason;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

import static com.nukkitx.network.raknet.RakNetConstants.*;

@ParametersAreNonnullByDefault
public class RakNetServerSession extends RakNetSession {

    private static final boolean security = !Server.getInstance().getPropertyBoolean("disable-raknet-security", false);

    private final RakNetServer rakNet;

    private final int cookie = ThreadLocalRandom.current().nextInt();

    RakNetServerSession(RakNetServer rakNet, InetSocketAddress remoteAddress, Channel channel, EventLoop eventLoop, int mtu,
                        int protocolVersion) {
        super(remoteAddress, channel, eventLoop, mtu, protocolVersion);
        this.rakNet = rakNet;
    }
    private int sentConnection1Replies = 0;
    private int sentConnection2Replies = 0;
    private int sentConnectionAcceptedReplies = 0;

    @Override
    public RakNet getRakNet() {
        return this.rakNet;
    }

    @Override
    protected void onClose() {
        if (!this.rakNet.sessionsByAddress.remove(this.address, this)) {
            throw new IllegalStateException("Session was not found in session map");
        }
    }

    private void onConnectionRequest(ByteBuf buffer) {
        if (this.getState().ordinal() > RakNetState.CONNECTING.ordinal()) { // Allow at 'CONNECTING' if unreliable reply was lost
            return;
        }

        long guid = buffer.readLong();
        long time = buffer.readLong();
        boolean security = buffer.readBoolean();

        if (this.guid != guid || security) {
            if (Nukkit.DEBUG > 1) {
                Server.getInstance().getLogger().debug("CONNECTION_REQUEST_FAILED " + this.address + " " + (this.guid != guid) + " " + security);
            }
            this.close(DisconnectReason.CONNECTION_REQUEST_FAILED);
            return;
        }

        this.setState(RakNetState.CONNECTING);

        this.sendConnectionRequestAccepted(time);
    }

    private void onNewIncomingConnection() {
        if (this.getState() != RakNetState.CONNECTING) {
            return;
        }

        this.setState(RakNetState.CONNECTED);
    }

    private void onOpenConnectionRequest2(ByteBuf buffer) {
        if (this.getState() != RakNetState.INITIALIZING && this.getState() != RakNetState.INITIALIZED) { // Already INITIALIZED == probably a packet loss occurred
            return;
        }

        if (!RakNetUtils.verifyUnconnectedMagic(buffer)) {
            return;
        }

        if (security) {
            if (buffer.readInt() != this.cookie) {
                if (Nukkit.DEBUG > 1) {
                    Server.getInstance().getLogger().debug("CONNECTION_REQUEST_FAILED " + this.address + " cookie mismatch");
                }
                return;
            }

            buffer.readBoolean(); // Client wrote challenge
        }

        NetworkUtils.readAddress(buffer);

        int mtu = buffer.readUnsignedShort();
        this.setMtu(mtu);
        this.guid = buffer.readLong();

        if (this.getState() == RakNetState.INITIALIZING) {
            // We can now accept RakNet datagrams.
            this.initialize();
        }

        sendOpenConnectionReply2();
        this.setState(RakNetState.INITIALIZED);
    }

    @Override
    protected void onPacket(ByteBuf buffer) {
        short packetId = buffer.readUnsignedByte();

        switch (packetId) {
            case ID_OPEN_CONNECTION_REQUEST_2:
                this.onOpenConnectionRequest2(buffer);
                break;
            case ID_CONNECTION_REQUEST:
                this.onConnectionRequest(buffer);
                break;
            case ID_NEW_INCOMING_CONNECTION:
                this.onNewIncomingConnection();
                break;
        }
    }

    private void sendConnectionRequestAccepted(long time) {
        if (++sentConnectionAcceptedReplies > 50) {
            if (sentConnectionAcceptedReplies == 51) {
                Server.getInstance().getLogger().warning("Too many connection accepted replies for " + this.address);
            }
            return;
        }

        boolean ipv6 = this.isIpv6Session();
        ByteBuf buffer = this.allocateBuffer(ipv6 ? 628 : 166);

        buffer.writeByte(ID_CONNECTION_REQUEST_ACCEPTED);
        NetworkUtils.writeAddress(buffer, this.address);
        buffer.writeShort(0); // System index

        for (InetSocketAddress socketAddress : ipv6 ? LOCAL_IP_ADDRESSES_V6 : LOCAL_IP_ADDRESSES_V4) {
            NetworkUtils.writeAddress(buffer, socketAddress);
        }

        buffer.writeLong(time);
        buffer.writeLong(System.currentTimeMillis());

        this.send(buffer, RakNetPriority.IMMEDIATE, RakNetReliability.UNRELIABLE);
    }

    void sendOpenConnectionReply1() {
        if (++sentConnection1Replies > 50) {
            if (sentConnection1Replies == 51) {
                Server.getInstance().getLogger().warning("Too many connection 1 replies for " + this.address);
            }
            return;
        }

        ByteBuf buffer = this.allocateBuffer(security ? 32 : 28);

        buffer.writeByte(ID_OPEN_CONNECTION_REPLY_1);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.rakNet.guid);
        buffer.writeBoolean(security);
        if (security) {
            buffer.writeInt(this.cookie);
        }
        buffer.writeShort(this.getMtu());

        this.sendDirect(buffer);
    }

    private void sendOpenConnectionReply2() {
        if (++sentConnection2Replies > 50) {
            if (sentConnection2Replies == 51) {
                Server.getInstance().getLogger().warning("Too many connection 2 replies for " + this.address);
            }
            return;
        }

        ByteBuf buffer = this.allocateBuffer(31);

        buffer.writeByte(ID_OPEN_CONNECTION_REPLY_2);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.rakNet.guid);
        NetworkUtils.writeAddress(buffer, this.address);
        buffer.writeShort(this.getMtu());
        buffer.writeBoolean(false); // Security

        this.sendDirect(buffer);
    }
}
