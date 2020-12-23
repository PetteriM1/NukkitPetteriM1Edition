package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BatchingThread extends Thread {

    public static volatile boolean running;
    public static final Queue<BatchEntry> queue = new ConcurrentLinkedQueue<>();

    public BatchingThread() {
        running = true;
        setName("BatchingThread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            BatchEntry entry = queue.poll();
            if (entry != null) {
                Int2ObjectMap<ObjectList<InetSocketAddress>> targets = new Int2ObjectOpenHashMap<>();
                for (Player player : entry.players) {
                    targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player.getSocketAddress());
                }

                // Encoded packets by encoding protocol
                Int2ObjectMap<ObjectList<DataPacket>> encodedPackets = new Int2ObjectOpenHashMap<>();

                for (DataPacket packet : entry.packets) {
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
                            if (!pk.isEncoded) {
                                pk.encode();
                            }
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
                    ObjectList<InetSocketAddress> finalTargets = targets.get(protocolId);

                    byte[][] payload = new byte[(entry.packets.length << 1)][];

                    for (int i = 0; i < packetList.size(); i++) {
                        DataPacket p = packetList.get(i);
                        int idx = i << 1;
                        byte[] buf = p.getBuffer();
                        payload[idx] = Binary.writeUnsignedVarInt(buf.length);
                        payload[idx + 1] = buf;
                    }

                    try {
                        byte[] bytes = Binary.appendBytes(payload);
                        if (protocolId >= ProtocolInfo.v1_16_0) {
                            Server.getInstance().broadcastPacketsCallback(Zlib.deflateRaw(bytes, Server.getInstance().networkCompressionLevel), finalTargets);
                        } else {
                            Server.getInstance().broadcastPacketsCallback(Zlib.deflate(bytes, Server.getInstance().networkCompressionLevel), finalTargets);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception ignore) {
                }
            }
        }
    }
}
