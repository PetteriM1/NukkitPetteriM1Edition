package cn.nukkit.network;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.server.BatchPacketsEvent;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.SnappyCompression;
import cn.nukkit.utils.Zlib;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchingHelper {

    private final ExecutorService threadedExecutor;

    public BatchingHelper() {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setNameFormat("Batching Executor");
        builder.setUncaughtExceptionHandler((thread, ex) -> {
            Server.getInstance().getLogger().error("Exception in " + thread.getName(), ex);
            ExceptionHandler.handleSilently(ex);
        });
        this.threadedExecutor = Executors.newSingleThreadExecutor(builder.build());
    }

    private static void batchAndSendPackets(Player[] players, DataPacket[] packets) {
        if (players.length == 1) {
            // Save some time if only one player is receiving packets
            for (DataPacket packet : packets) {
                packet.protocol = players[0].protocol;
                players[0].getNetworkSession().sendPacket(packet);
            }
            return;
        }

        Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
        for (Player player : players) {
            targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
        }

        // Encoded packets by encoding protocol
        Int2ObjectMap<ObjectList<DataPacket>> encodedPackets = new Int2ObjectOpenHashMap<>();

        for (DataPacket packet : packets) {
            Int2IntMap encodingProtocols = new Int2IntOpenHashMap();
            for (int protocolId : targets.keySet()) {
                // TODO: encode only by encoding protocols
                // No need to have all versions here
                encodingProtocols.put(protocolId, protocolId);
            }

            Int2ObjectMap<DataPacket> encodedPacket = new Int2ObjectOpenHashMap<>();
            for (int encodingProtocol : encodingProtocols.values()) {
                if (!encodedPacket.containsKey(encodingProtocol)) {
                    DataPacket pk = packet.clone();
                    pk.protocol = encodingProtocol;
                    pk.tryEncode();
                    encodedPacket.put(encodingProtocol, pk);
                }
            }

            for (int protocolId : encodingProtocols.values()) {
                int encodingProtocol = encodingProtocols.get(protocolId);
                encodedPackets.computeIfAbsent(protocolId, i -> new ObjectArrayList<>()).add(encodedPacket.get(encodingProtocol));
            }
        }

        for (int protocolId : targets.keySet()) {
            ObjectList<DataPacket> packetList = encodedPackets.get(protocolId);
            ObjectList<Player> finalTargets = targets.get(protocolId);

            BinaryStream batched = new BinaryStream();
            for (DataPacket packet : packetList) {
                if (packet instanceof BatchPacket) {
                    throw new RuntimeException("Cannot batch BatchPacket");
                }
                packet.tryEncode();
                byte[] buf = packet.getBuffer();
                batched.putUnsignedVarInt(buf.length);
                batched.put(buf);
            }

            try {
                byte[] bytes = batched.getBuffer();
                BatchPacket compress = new BatchPacket();
                if (Server.getInstance().useSnappy && protocolId >= ProtocolInfo.v1_19_30_23) {
                    compress.payload = SnappyCompression.compress(bytes);
                } else if (protocolId >= ProtocolInfo.v1_16_0) {
                    compress.payload = Zlib.deflateRaw(bytes, Server.getInstance().networkCompressionLevel);
                } else {
                    compress.payload = Zlib.deflatePre16Packet(bytes, Server.getInstance().networkCompressionLevel);
                }
                for (Player pl : finalTargets) {
                    CompressionProvider compression = pl.getNetworkSession().getCompression();
                    if (compression == CompressionProvider.NONE) {
                        BatchPacket noCompress = new BatchPacket();
                        noCompress.payload = bytes;
                        pl.dataPacket(noCompress);
                    } else {
                        pl.dataPacket(compress);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void batchPackets(Server server, Player[] players, DataPacket[] packets) {
        if (players == null || packets == null || players.length == 0 || packets.length == 0) {
            return;
        }

        if (server.callBatchPkEvent) {
            BatchPacketsEvent ev = new BatchPacketsEvent(players, packets, true);
            server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return;
            }
        }

        this.threadedExecutor.execute(() -> batchAndSendPackets(players, packets));
    }

    public void shutdown() {
        this.threadedExecutor.shutdownNow();
    }
}
