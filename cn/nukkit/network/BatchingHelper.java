/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchingHelper {
    private final ExecutorService a;

    public BatchingHelper() {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("Batching Executor");
        threadFactoryBuilder.setUncaughtExceptionHandler((thread, throwable) -> Server.getInstance().getLogger().error("Failed to batch packets", throwable));
        this.a = Executors.newSingleThreadExecutor(threadFactoryBuilder.build());
    }

    public void batchPackets(Player[] playerArray, DataPacket[] dataPacketArray) {
        block0: {
            if (playerArray.length <= 0 || dataPacketArray.length <= 0) break block0;
            this.a.execute(() -> BatchingHelper.a(playerArray, dataPacketArray));
        }
    }

    private static void a(Player[] playerArray, DataPacket[] dataPacketArray) {
        Object object5;
        Object object2;
        Object object3;
        if (playerArray.length == 1) {
            for (DataPacket dataPacket : dataPacketArray) {
                dataPacket.protocol = playerArray[0].protocol;
                playerArray[0].getNetworkSession().sendPacket(dataPacket);
            }
            return;
        }
        Int2ObjectOpenHashMap<ObjectList> int2ObjectOpenHashMap = new Int2ObjectOpenHashMap<ObjectList>();
        for (Player player : playerArray) {
            int2ObjectOpenHashMap.computeIfAbsent(player.protocol, n -> new ObjectArrayList()).add(player);
        }
        Int2ObjectOpenHashMap int2ObjectOpenHashMap2 = new Int2ObjectOpenHashMap();
        for (DataPacket object4 : dataPacketArray) {
            int object5;
            object3 = new Int2IntOpenHashMap();
            object2 = int2ObjectOpenHashMap.keySet().iterator();
            while (object2.hasNext()) {
                int n2 = (Integer)object2.next();
                object3.put(n2, n2);
            }
            object2 = new Int2ObjectOpenHashMap();
            object5 = object3.values().iterator();
            while (object5.hasNext()) {
                object5 = (Integer)object5.next();
                if (object2.containsKey(object5)) continue;
                DataPacket player = object4.clone();
                player.protocol = object5;
                player.tryEncode();
                object2.put(object5, player);
            }
            object5 = object3.values().iterator();
            while (object5.hasNext()) {
                object5 = (Integer)object5.next();
                int n3 = object3.get(object5);
                int2ObjectOpenHashMap2.computeIfAbsent(object5, n -> new ObjectArrayList()).add(object2.get(n3));
            }
        }
        IntIterator intIterator = int2ObjectOpenHashMap.keySet().iterator();
        while (intIterator.hasNext()) {
            Object object4;
            int n5 = (Integer)intIterator.next();
            ObjectList objectList = (ObjectList)int2ObjectOpenHashMap2.get(n5);
            ObjectList objectList2 = (ObjectList)int2ObjectOpenHashMap.get(n5);
            object3 = new BinaryStream();
            for (Object object5 : objectList) {
                if (object5 instanceof BatchPacket) {
                    throw new RuntimeException("Cannot batch BatchPacket");
                }
                ((DataPacket)object5).tryEncode();
                object4 = ((BinaryStream)object5).getBuffer();
                ((BinaryStream)object3).putUnsignedVarInt(((byte[])object4).length);
                ((BinaryStream)object3).put((byte[])object4);
            }
            try {
                object2 = Binary.appendBytes(((BinaryStream)object3).getBuffer(), (byte[][])new byte[0][]);
                object5 = new BatchPacket();
                ((BatchPacket)object5).payload = n5 >= 407 ? Zlib.deflateRaw((byte[])object2, Server.getInstance().networkCompressionLevel) : Zlib.deflate((byte[])object2, Server.getInstance().networkCompressionLevel);
                object4 = objectList2.iterator();
                while (object4.hasNext()) {
                    Player player = (Player)object4.next();
                    CompressionProvider compressionProvider = player.getNetworkSession().getCompression();
                    if (compressionProvider == CompressionProvider.NONE) {
                        BatchPacket batchPacket = new BatchPacket();
                        batchPacket.payload = (byte[])object2;
                        player.dataPacket(batchPacket);
                        continue;
                    }
                    player.dataPacket((DataPacket)object5);
                }
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public void shutdown() {
        this.a.shutdownNow();
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

