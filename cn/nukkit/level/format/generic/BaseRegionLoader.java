/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.generic.BaseFullChunk;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class BaseRegionLoader {
    public static final byte COMPRESSION_GZIP = 1;
    public static final byte COMPRESSION_ZLIB = 2;
    public static final int MAX_SECTOR_LENGTH = 0x100000;
    protected int x;
    protected int z;
    protected int lastSector;
    protected LevelProvider levelProvider;
    private final RandomAccessFile a;
    protected final Int2ObjectMap<Integer[]> locationTable = new Int2ObjectOpenHashMap<Integer[]>();
    public long lastUsed;

    public BaseRegionLoader(LevelProvider levelProvider, int n, int n2, String string) {
        try {
            this.x = n;
            this.z = n2;
            this.levelProvider = levelProvider;
            String string2 = this.levelProvider.getPath() + "region/r." + n + '.' + n2 + '.' + string;
            File file = new File(string2);
            boolean bl = file.exists();
            if (!bl) {
                file.createNewFile();
            }
            this.a = new RandomAccessFile(string2, "rw");
            if (!bl) {
                this.createBlank();
            } else {
                this.loadLocationTable();
            }
            this.lastUsed = System.currentTimeMillis();
        }
        catch (IOException iOException) {
            throw new RuntimeException("Unable to load r." + n + '.' + n2 + '.' + string, iOException);
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return this.a;
    }

    protected abstract boolean isChunkGenerated(int var1);

    public abstract BaseFullChunk readChunk(int var1, int var2) throws IOException;

    protected abstract BaseFullChunk unserializeChunk(byte[] var1);

    public abstract boolean chunkExists(int var1, int var2);

    protected abstract void saveChunk(int var1, int var2, byte[] var3) throws IOException;

    public abstract void removeChunk(int var1, int var2);

    public abstract void writeChunk(FullChunk var1) throws Exception;

    public void close() throws IOException {
        block0: {
            if (this.a == null) break block0;
            this.a.close();
        }
    }

    protected abstract void loadLocationTable() throws IOException;

    public abstract int doSlowCleanUp() throws Exception;

    protected abstract void writeLocationIndex(int var1) throws IOException;

    protected abstract void createBlank() throws IOException;

    public abstract int getX();

    public abstract int getZ();

    public Integer[] getLocationIndexes() {
        return this.locationTable.keySet().toArray((T[])new Integer[0]);
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

