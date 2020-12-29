package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

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
                Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
                for (Player player : entry.players) {
                    targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
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
                            if (!pk.isEncoded) {
                                pk.encode(encodingProtocol);
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
                    ObjectList<Player> finalTargets = targets.get(protocolId);

                    BinaryStream batched = new BinaryStream();
                    for (DataPacket packet : packetList) {
                        if (packet instanceof BatchPacket) {
                            throw new RuntimeException("Cannot batch BatchPacket");
                        }
                        if (!packet.isEncoded) {
                            packet.encode(protocolId);
                        }
                        byte[] buf = packet.getBuffer();
                        batched.putUnsignedVarInt(buf.length);
                        batched.put(buf);
                    }

                    try {
                        byte[] bytes = Binary.appendBytes(batched.getBuffer());
                        BatchPacket pk = new BatchPacket();
                        if (protocolId >= ProtocolInfo.v1_16_0) {
                            pk.payload = Zlib.deflateRaw(bytes, Server.getInstance().networkCompressionLevel);
                        } else {
                            pk.payload = Zlib.deflate(bytes, Server.getInstance().networkCompressionLevel);
                        }
                        for (Player pl : finalTargets) {
                            pl.directDataPacket(pk);
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
