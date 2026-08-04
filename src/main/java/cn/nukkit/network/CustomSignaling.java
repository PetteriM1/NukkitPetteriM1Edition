package cn.nukkit.network;

import org.cloudburstmc.netty.channel.nethernet.signaling.IceServerInfo;
import org.cloudburstmc.netty.channel.nethernet.signaling.NetherNetServerSignaling;
import org.cloudburstmc.netty.channel.nethernet.signaling.PongData;
import org.cloudburstmc.netty.util.nethernet.OperatorIdentity;
import io.netty.channel.EventLoop;
import lombok.RequiredArgsConstructor;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class CustomSignaling implements NetherNetServerSignaling {

    private static final Pattern HOST_CANDIDATE_ADDRESS = Pattern.compile("(candidate:\\S+ \\d+ \\S+ \\d+ )([0-9a-fA-F.:]+)( \\d+ typ host\\b)");
    private static final Pattern SRFLX_CANDIDATE_LINE = Pattern.compile("^a=candidate:.*\\btyp srflx\\b.*$\\R?", Pattern.MULTILINE);
    private static final Pattern IS_SRFLX_SIGNAL = Pattern.compile("\\btyp srflx\\b");

    private final NetherNetServerSignaling delegate;
    private final String advertiseAddress;

    private String rewriteSdp(String sdp) {
        if (advertiseAddress == null) return sdp;
        if (sdp == null) return null;
        String withoutSrflx = SRFLX_CANDIDATE_LINE.matcher(sdp).replaceAll("");
        return HOST_CANDIDATE_ADDRESS.matcher(withoutSrflx).replaceAll(mr -> mr.group(1) + advertiseAddress + mr.group(3));
    }

    private String rewriteCandidateSignal(String data) {
        if (advertiseAddress == null) return data;
        if (data == null) return null;
        if (IS_SRFLX_SIGNAL.matcher(data).find()) return null;
        return HOST_CANDIDATE_ADDRESS.matcher(data).replaceAll(mr -> mr.group(1) + advertiseAddress + mr.group(3));
    }

    @Override
    public void bind(SocketAddress localAddress, InetSocketAddress iceAddress, EventLoop eventLoop) throws ConnectException {
        delegate.bind(localAddress, iceAddress, eventLoop);
    }

    @Override
    public void setNewConnectionHandler(NewConnectionHandler handler) {
        delegate.setNewConnectionHandler(handler);
    }

    @Override
    public void setAdvertisementData(PongData pongData) {
        delegate.setAdvertisementData(pongData);
    }

    @Override
    public List<IceServerInfo> getIceServers() {
        return delegate.getIceServers();
    }

    @Override
    public OperatorIdentity serverIdentity() {
        return delegate.serverIdentity();
    }

    @Override
    public boolean allowsIceOnLocalPort() {
        return delegate.allowsIceOnLocalPort();
    }

    @Override
    public boolean usesTrickleIce() {
        return delegate.usesTrickleIce();
    }

    @Override
    public void sendSignal(String targetNetworkId, String data) {
        String rewritten = rewriteCandidateSignal(data);
        if (rewritten != null) {
            delegate.sendSignal(targetNetworkId, rewritten);
        }
    }

    @Override
    public void sendDescription(String targetNetworkId, String sdp) {
        delegate.sendDescription(targetNetworkId, rewriteSdp(sdp));
    }

    @Override
    public void setSignalHandler(String connectionId, SignalHandler handler) {
        delegate.setSignalHandler(connectionId, handler);
    }

    @Override
    public void removeSignalHandler(String connectionId) {
        delegate.removeSignalHandler(connectionId);
    }

    @Override
    public String getLocalNetworkId() {
        return delegate.getLocalNetworkId();
    }

    @Override
    public boolean isActive() {
        return delegate.isActive();
    }

    @Override
    public void close() {
        delegate.close();
    }
}
