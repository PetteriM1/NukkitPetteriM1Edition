/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.level.format.generic.BaseRegionLoader;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Zlib;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class RegionLoader
extends BaseRegionLoader {
    public RegionLoader(LevelProvider levelProvider, int n, int n2) throws IOException {
        super(levelProvider, n, n2, "mca");
    }

    @Override
    protected boolean isChunkGenerated(int n) {
        Integer[] integerArray = (Integer[])this.locationTable.get(n);
        return integerArray[0] != 0 && integerArray[1] != 0;
    }

    @Override
    public Chunk readChunk(int n, int n2) throws IOException {
        int n3 = RegionLoader.getChunkOffset(n, n2);
        if (n3 < 0 || n3 >= 4096) {
            return null;
        }
        this.lastUsed = System.currentTimeMillis();
        if (!this.isChunkGenerated(n3)) {
            return null;
        }
        try {
            Integer[] integerArray = (Integer[])this.locationTable.get(n3);
            RandomAccessFile randomAccessFile = this.getRandomAccessFile();
            randomAccessFile.seek(integerArray[0] << 12);
            int n4 = randomAccessFile.readInt();
            byte by = randomAccessFile.readByte();
            if (n4 <= 0 || n4 >= 0x100000) {
                if (n4 >= 0x100000) {
                    integerArray[0] = ++this.lastSector;
                    integerArray[1] = 1;
                    this.locationTable.put(n3, integerArray);
                    MainLogger.getLogger().error("Corrupted chunk header detected (" + n + ", " + n2 + ") (" + this.levelProvider.getName() + "/r." + this.x + "." + this.z + ".mca)");
                }
                return null;
            }
            if (n4 > integerArray[1] << 12) {
                MainLogger.getLogger().error("Corrupted bigger chunk detected (" + n + ", " + n2 + ") (" + this.levelProvider.getName() + "/r." + this.x + "." + this.z + ".mca)");
                integerArray[1] = n4 >> 12;
                this.locationTable.put(n3, integerArray);
                this.writeLocationIndex(n3);
            } else if (by != 2 && by != 1) {
                MainLogger.getLogger().error("Invalid compression type (" + n + ", " + n2 + ") (" + this.levelProvider.getName() + "/r." + this.x + "." + this.z + ".mca)");
                return null;
            }
            byte[] byArray = new byte[n4 - 1];
            randomAccessFile.readFully(byArray);
            Chunk chunk = this.unserializeChunk(byArray);
            if (chunk != null) {
                return chunk;
            }
            MainLogger.getLogger().error("Corrupted chunk detected (" + n + ", " + n2 + ") (" + this.levelProvider.getName() + "/r." + this.x + "." + this.z + ".mca)");
            return null;
        }
        catch (EOFException eOFException) {
            MainLogger.getLogger().error("World corruption occurred (" + n + ", " + n2 + ") (" + this.levelProvider.getName() + "/r." + this.x + "." + this.z + ".mca)");
            return null;
        }
    }

    @Override
    protected Chunk unserializeChunk(byte[] byArray) {
        return Chunk.fromBinary(byArray, this.levelProvider);
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return this.isChunkGenerated(RegionLoader.getChunkOffset(n, n2));
    }

    @Override
    protected void saveChunk(int n, int n2, byte[] byArray) throws IOException {
        block5: {
            int n3 = byArray.length + 1;
            if (n3 + 4 > 0x100000) {
                throw new ChunkException("Chunk [" + n + ", " + n2 + "] is too big! " + (n3 + 4) + " > " + 0x100000);
            }
            int n4 = (int)Math.ceil((double)(n3 + 4) / 4096.0);
            int n5 = RegionLoader.getChunkOffset(n, n2);
            boolean bl = false;
            Integer[] integerArray = (Integer[])this.locationTable.get(n5);
            if (integerArray[1] < n4) {
                integerArray[0] = this.lastSector + 1;
                this.locationTable.put(n5, integerArray);
                this.lastSector += n4;
                bl = true;
            } else if (integerArray[1] != n4) {
                bl = true;
            }
            integerArray[1] = n4;
            integerArray[2] = (int)((double)System.currentTimeMillis() / 1000.0);
            this.locationTable.put(n5, integerArray);
            RandomAccessFile randomAccessFile = this.getRandomAccessFile();
            randomAccessFile.seek(integerArray[0] << 12);
            BinaryStream binaryStream = new BinaryStream();
            binaryStream.put(Binary.writeInt(n3));
            binaryStream.putByte((byte)2);
            binaryStream.put(byArray);
            byte[] byArray2 = binaryStream.getBuffer();
            if (byArray2.length < n4 << 12) {
                byte[] byArray3 = new byte[n4 << 12];
                System.arraycopy(byArray2, 0, byArray3, 0, byArray2.length);
                byArray2 = byArray3;
            }
            randomAccessFile.write(byArray2);
            if (!bl) break block5;
            this.writeLocationIndex(n5);
        }
    }

    @Override
    public void removeChunk(int n, int n2) {
        int n3 = RegionLoader.getChunkOffset(n, n2);
        Integer[] integerArray = (Integer[])this.locationTable.get(0);
        integerArray[0] = 0;
        integerArray[1] = 0;
        this.locationTable.put(n3, integerArray);
    }

    @Override
    public void writeChunk(FullChunk fullChunk) throws Exception {
        this.lastUsed = System.currentTimeMillis();
        byte[] byArray = fullChunk.toBinary();
        this.saveChunk(fullChunk.getX() & 0x1F, fullChunk.getZ() & 0x1F, byArray);
    }

    protected static int getChunkOffset(int n, int n2) {
        return n | n2 << 5;
    }

    @Override
    public void close() throws IOException {
        this.a();
        this.levelProvider = null;
        super.close();
    }

    @Override
    public int doSlowCleanUp() throws Exception {
        int n;
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        for (n = 0; n < 1024; ++n) {
            Integer[] integerArray = (Integer[])this.locationTable.get(n);
            if (integerArray[0] == 0 || integerArray[1] == 0) continue;
            randomAccessFile.seek(integerArray[0] << 12);
            byte[] byArray = new byte[integerArray[1] << 12];
            randomAccessFile.readFully(byArray);
            int n2 = Binary.readInt(Arrays.copyOfRange(byArray, 0, 3));
            if (n2 <= 1) {
                integerArray = new Integer[]{0, 0, 0};
                this.locationTable.put(n, integerArray);
            }
            try {
                byArray = Zlib.inflate(Arrays.copyOf(byArray, 5));
            }
            catch (Exception exception) {
                this.locationTable.put(n, new Integer[]{0, 0, 0});
                continue;
            }
            byArray = Zlib.deflate(byArray, 9);
            ByteBuffer byteBuffer = ByteBuffer.allocate(5 + byArray.length);
            byteBuffer.put(Binary.writeInt(byArray.length + 1));
            byteBuffer.put((byte)2);
            byteBuffer.put(byArray);
            byArray = byteBuffer.array();
            int n3 = (int)Math.ceil((double)byArray.length / 4096.0);
            if (n3 > integerArray[1]) {
                integerArray[0] = this.lastSector + 1;
                this.lastSector += n3;
                this.locationTable.put(n, integerArray);
            }
            randomAccessFile.seek(integerArray[0] << 12);
            byte[] byArray2 = new byte[n3 << 12];
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(byArray2);
            byteBuffer2.put(byArray);
            randomAccessFile.write(byteBuffer2.array());
        }
        this.a();
        n = this.b();
        this.a();
        return n;
    }

    @Override
    protected void loadLocationTable() throws IOException {
        int n;
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        randomAccessFile.seek(0L);
        this.lastSector = 1;
        int[] nArray = new int[2048];
        for (n = 0; n < 2048; ++n) {
            nArray[n] = randomAccessFile.readInt();
        }
        for (n = 0; n < 1024; ++n) {
            int n2 = nArray[n];
            this.locationTable.put(n, new Integer[]{n2 >> 8, n2 & 0xFF, nArray[1024 + n]});
            int n3 = ((Integer[])this.locationTable.get(n))[0] + ((Integer[])this.locationTable.get(n))[1] - 1;
            if (n3 <= this.lastSector) continue;
            this.lastSector = n3;
        }
    }

    private void a() throws IOException {
        Integer[] integerArray;
        int n;
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        randomAccessFile.seek(0L);
        for (n = 0; n < 1024; ++n) {
            integerArray = (Integer[])this.locationTable.get(n);
            randomAccessFile.writeInt(integerArray[0] << 8 | integerArray[1]);
        }
        for (n = 0; n < 1024; ++n) {
            integerArray = (Integer[])this.locationTable.get(n);
            randomAccessFile.writeInt(integerArray[2]);
        }
    }

    private int b() throws IOException {
        Object object;
        int n;
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        TreeMap<Object, Integer> treeMap = new TreeMap<Object, Integer>();
        for (Map.Entry entry : this.locationTable.entrySet()) {
            n = (Integer)entry.getKey();
            object = (Integer[])entry.getValue();
            if (object[0] == 0 || object[1] == 0) {
                this.locationTable.put(n, new Integer[]{0, 0, 0});
                continue;
            }
            treeMap.put(object[0], n);
        }
        if (treeMap.size() == this.lastSector - 2) {
            return 0;
        }
        int n2 = 0;
        int n3 = 1;
        randomAccessFile.seek(8192L);
        n = 2;
        object = treeMap.keySet().iterator();
        while (object.hasNext()) {
            Object[] objectArray;
            int n4;
            n = n4 = ((Integer)object.next()).intValue();
            int n5 = (Integer)treeMap.get(n4);
            if (n4 - n3 > 1) {
                n2 += n4 - n3 - 1;
            }
            if (n2 > 0) {
                randomAccessFile.seek(n4 << 12);
                objectArray = new byte[4096];
                randomAccessFile.readFully((byte[])objectArray);
                randomAccessFile.seek(n4 - n2 << 12);
                randomAccessFile.write((byte[])objectArray);
            }
            Integer[] integerArray = objectArray = (Integer[])this.locationTable.get(n5);
            Integer.valueOf(integerArray[0] - n2);
            this.locationTable.put(n5, objectArray);
            this.lastSector = n4;
        }
        randomAccessFile.setLength(n + 1 << 12);
        return n2;
    }

    @Override
    protected void writeLocationIndex(int n) throws IOException {
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        Integer[] integerArray = (Integer[])this.locationTable.get(n);
        randomAccessFile.seek(n << 2);
        randomAccessFile.writeInt(integerArray[0] << 8 | integerArray[1]);
        randomAccessFile.seek(4096 + (n << 2));
        randomAccessFile.writeInt(integerArray[2]);
    }

    @Override
    protected void createBlank() throws IOException {
        int n;
        RandomAccessFile randomAccessFile = this.getRandomAccessFile();
        randomAccessFile.seek(0L);
        randomAccessFile.setLength(0L);
        this.lastSector = 1;
        int n2 = (int)((double)System.currentTimeMillis() / 1000.0);
        for (n = 0; n < 1024; ++n) {
            this.locationTable.put(n, new Integer[]{0, 0, n2});
            randomAccessFile.writeInt(0);
        }
        for (n = 0; n < 1024; ++n) {
            randomAccessFile.writeInt(n2);
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

