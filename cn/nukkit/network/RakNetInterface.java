/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Event;
import cn.nukkit.event.player.PlayerCreationEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.network.AdvancedSourceInterface;
import cn.nukkit.network.Network;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.session.NetworkPlayerSession;
import cn.nukkit.network.session.RakNetPlayerSession;
import cn.nukkit.utils.Utils;
import com.google.common.base.Strings;
import com.nukkitx.network.raknet.RakNetServer;
import com.nukkitx.network.raknet.RakNetServerListener;
import com.nukkitx.network.raknet.RakNetServerSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RakNetInterface
implements RakNetServerListener,
AdvancedSourceInterface {
    private static final Logger b = LogManager.getLogger(RakNetInterface.class);
    private final Server e;
    private final RakNetServer g;
    private Network c;
    private byte[] f;
    private final Map<InetSocketAddress, RakNetPlayerSession> a = new HashMap<InetSocketAddress, RakNetPlayerSession>();
    private final Queue<RakNetPlayerSession> d = PlatformDependent.newMpscQueue();

    public RakNetInterface(Server server) {
        this.e = server;
        this.g = new RakNetServer(new InetSocketAddress(Strings.isNullOrEmpty(this.e.getIp()) ? "0.0.0.0" : this.e.getIp(), this.e.getPort()), Runtime.getRuntime().availableProcessors());
        this.g.bind().join();
        this.g.setListener(this);
    }

    @Override
    public void setNetwork(Network network) {
        this.c = network;
    }

    @Override
    public boolean process() {
        Object object;
        Object object2;
        Object object3;
        RakNetPlayerSession rakNetPlayerSession;
        while ((rakNetPlayerSession = this.d.poll()) != null) {
            object3 = rakNetPlayerSession.getRakNetSession().getAddress();
            try {
                object2 = new PlayerCreationEvent(this, Player.class, Player.class, null, (InetSocketAddress)object3);
                this.e.getPluginManager().callEvent((Event)object2);
                this.a.put(((PlayerCreationEvent)object2).getSocketAddress(), rakNetPlayerSession);
                object = ((PlayerCreationEvent)object2).getPlayerClass().getConstructor(SourceInterface.class, Long.class, InetSocketAddress.class);
                Player player = (Player)((Constructor)object).newInstance(this, ((PlayerCreationEvent)object2).getClientId(), ((PlayerCreationEvent)object2).getSocketAddress());
                player.raknetProtocol = rakNetPlayerSession.getRakNetSession().getProtocolVersion();
                rakNetPlayerSession.setPlayer(player);
                this.e.addPlayer((InetSocketAddress)object3, player);
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().error("Failed to create player", exception);
                rakNetPlayerSession.disconnect("Internal error");
                this.a.remove(object3);
            }
        }
        object3 = this.a.values().iterator();
        while (object3.hasNext()) {
            object2 = (RakNetPlayerSession)object3.next();
            object = ((RakNetPlayerSession)object2).getPlayer();
            if (((RakNetPlayerSession)object2).getDisconnectReason() != null) {
                ((Player)object).close(((Player)object).getLeaveMessage(), ((RakNetPlayerSession)object2).getDisconnectReason(), false);
                object3.remove();
                continue;
            }
            ((RakNetPlayerSession)object2).serverTick();
        }
        return true;
    }

    @Override
    public int getNetworkLatency(Player player) {
        RakNetServerSession rakNetServerSession = this.g.getSession(player.getSocketAddress());
        return rakNetServerSession == null ? -1 : (int)rakNetServerSession.getPing();
    }

    @Override
    public NetworkPlayerSession getSession(InetSocketAddress inetSocketAddress) {
        return this.a.get(inetSocketAddress);
    }

    @Override
    public void close(Player player) {
        this.close(player, "unknown reason");
    }

    @Override
    public void close(Player player, String string) {
        NetworkPlayerSession networkPlayerSession = this.getSession(player.getSocketAddress());
        if (networkPlayerSession != null) {
            networkPlayerSession.disconnect(string);
        }
    }

    @Override
    public void shutdown() {
        this.a.values().forEach(rakNetPlayerSession -> rakNetPlayerSession.disconnect("Shutdown"));
        this.g.close();
    }

    @Override
    public void emergencyShutdown() {
        this.a.values().forEach(rakNetPlayerSession -> rakNetPlayerSession.disconnect("Shutdown"));
        this.g.close();
    }

    @Override
    public void blockAddress(InetAddress inetAddress) {
        this.g.block(inetAddress);
    }

    @Override
    public void blockAddress(InetAddress inetAddress, int n) {
        this.g.block(inetAddress, n, TimeUnit.SECONDS);
    }

    @Override
    public void unblockAddress(InetAddress inetAddress) {
        this.g.unblock(inetAddress);
    }

    @Override
    public void sendRawPacket(InetSocketAddress inetSocketAddress, ByteBuf byteBuf) {
        this.g.send(inetSocketAddress, byteBuf);
    }

    @Override
    public void setName(String string) {
        QueryRegenerateEvent queryRegenerateEvent = this.e.getQueryInformation();
        String[] stringArray = string.split("!@#");
        String string2 = Utils.rtrim(stringArray[0].replace(";", "\\;"), '\\');
        String string3 = stringArray.length > 1 ? Utils.rtrim(stringArray[1].replace(";", "\\;"), '\\') : "";
        StringJoiner stringJoiner = new StringJoiner(";").add("MCPE").add(string2).add(Integer.toString(ProtocolInfo.CURRENT_PROTOCOL)).add("1.19.50").add(Integer.toString(queryRegenerateEvent.getPlayerCount())).add(Integer.toString(queryRegenerateEvent.getMaxPlayerCount())).add(Long.toString(this.g.getGuid())).add(string3).add(Server.getGamemodeString(this.e.getDefaultGamemode(), true)).add("1");
        this.f = stringJoiner.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket) {
        return this.putPacket(player, dataPacket, false);
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket, boolean bl) {
        return this.putPacket(player, dataPacket, bl, false);
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket, boolean bl, boolean bl2) {
        RakNetPlayerSession rakNetPlayerSession = this.a.get(player.getSocketAddress());
        if (rakNetPlayerSession != null) {
            rakNetPlayerSession.sendPacket(dataPacket);
        }
        return null;
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
        return true;
    }

    @Override
    public byte[] onQuery(InetSocketAddress inetSocketAddress) {
        return this.f;
    }

    @Override
    public void onSessionCreation(RakNetServerSession rakNetServerSession) {
        if (rakNetServerSession.getEventLoop().inEventLoop()) {
            this.a(rakNetServerSession);
        } else {
            rakNetServerSession.getEventLoop().execute(() -> this.a(rakNetServerSession));
        }
    }

    private void a(RakNetServerSession rakNetServerSession) {
        RakNetPlayerSession rakNetPlayerSession = new RakNetPlayerSession(this, rakNetServerSession);
        rakNetServerSession.setListener(rakNetPlayerSession);
        this.d.offer(rakNetPlayerSession);
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) {
        this.e.handlePacket((InetSocketAddress)datagramPacket.sender(), (ByteBuf)datagramPacket.content());
    }

    public Network getNetwork() {
        return this.c;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

