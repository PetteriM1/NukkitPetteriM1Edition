/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.DimensionData;
import cn.nukkit.level.Level;
import cn.nukkit.level.c;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.level.format.generic.serializer.NetworkChunkData;
import cn.nukkit.level.format.generic.serializer.NetworkChunkSerializer;
import cn.nukkit.utils.BinaryStream;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

class e {
    private final ExecutorService b;
    final Queue<c> a = new ConcurrentLinkedQueue<c>();

    e(String string) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("AsyncChunkThread for " + string);
        threadFactoryBuilder.setUncaughtExceptionHandler((thread, throwable) -> Server.getInstance().getLogger().error("[" + string + "] Uncaught exception", throwable));
        this.b = Executors.newSingleThreadExecutor(threadFactoryBuilder.build());
    }

    void b(IntSet intSet, Chunk chunk, long l, int n, int n2, boolean bl, DimensionData dimensionData) {
        this.b.execute(() -> this.a(intSet, chunk, l, n, n2, bl, dimensionData));
    }

    private void a(IntSet intSet, Chunk chunk, long l, int n, int n2, boolean bl, DimensionData dimensionData) {
        BiConsumer<BinaryStream, NetworkChunkData> biConsumer = (binaryStream, networkChunkData) -> this.a.add(new c(networkChunkData.getProtocol(), l, n, n2, Level.chunkHash(n, n2), binaryStream.getBuffer(), networkChunkData.getChunkSections()));
        NetworkChunkSerializer.serialize(chunk, intSet, biConsumer, bl, dimensionData);
    }

    void a() {
        this.b.shutdownNow();
    }
}

