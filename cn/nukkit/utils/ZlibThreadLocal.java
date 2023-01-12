/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.utils.ThreadCache;
import cn.nukkit.utils.e;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class ZlibThreadLocal
implements e {
    private static final ThreadLocal<Inflater> a = ThreadLocal.withInitial(Inflater::new);
    private static final ThreadLocal<Deflater> e = ThreadLocal.withInitial(Deflater::new);
    private static final ThreadLocal<Inflater> c = ThreadLocal.withInitial(() -> new Inflater(true));
    private static final ThreadLocal<Deflater> b = ThreadLocal.withInitial(() -> new Deflater(7, true));
    private static final ThreadLocal<byte[]> d = ThreadLocal.withInitial(() -> new byte[8192]);

    @Override
    public byte[] deflate(byte[][] byArray, int n) throws IOException {
        Deflater deflater = e.get();
        deflater.reset();
        deflater.setLevel(n);
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        for (byte[] byArray3 : byArray) {
            deflater.setInput(byArray3);
            while (!deflater.needsInput()) {
                int n2 = deflater.deflate(byArray2);
                fastByteArrayOutputStream.write(byArray2, 0, n2);
            }
        }
        deflater.finish();
        while (!deflater.finished()) {
            int n3 = deflater.deflate(byArray2);
            fastByteArrayOutputStream.write(byArray2, 0, n3);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] deflate(byte[] byArray, int n) throws IOException {
        Deflater deflater = e.get();
        deflater.reset();
        deflater.setLevel(n);
        deflater.setInput(byArray);
        deflater.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        while (!deflater.finished()) {
            int n2 = deflater.deflate(byArray2);
            fastByteArrayOutputStream.write(byArray2, 0, n2);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[][] byArray, int n) throws IOException {
        Deflater deflater = b.get();
        deflater.reset();
        deflater.setLevel(n);
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        for (byte[] byArray3 : byArray) {
            deflater.setInput(byArray3);
            while (!deflater.needsInput()) {
                int n2 = deflater.deflate(byArray2);
                fastByteArrayOutputStream.write(byArray2, 0, n2);
            }
        }
        deflater.finish();
        while (!deflater.finished()) {
            int n3 = deflater.deflate(byArray2);
            fastByteArrayOutputStream.write(byArray2, 0, n3);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[] byArray, int n) throws IOException {
        Deflater deflater = b.get();
        deflater.reset();
        deflater.setLevel(n);
        deflater.setInput(byArray);
        deflater.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        while (!deflater.finished()) {
            int n2 = deflater.deflate(byArray2);
            fastByteArrayOutputStream.write(byArray2, 0, n2);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] inflate(byte[] byArray, int n) throws IOException {
        Inflater inflater = a.get();
        inflater.reset();
        inflater.setInput(byArray);
        inflater.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        try {
            int n2 = 0;
            while (!inflater.finished()) {
                int n3 = inflater.inflate(byArray2);
                if (n > 0 && (n2 += n3) >= n) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                fastByteArrayOutputStream.write(byArray2, 0, n3);
            }
            return fastByteArrayOutputStream.toByteArray();
        }
        catch (DataFormatException dataFormatException) {
            throw new IOException("Unable to inflate Zlib stream", dataFormatException);
        }
    }

    @Override
    public byte[] inflateRaw(byte[] byArray, int n) throws IOException {
        Inflater inflater = c.get();
        inflater.reset();
        inflater.setInput(byArray);
        inflater.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = d.get();
        try {
            int n2 = 0;
            while (!inflater.finished()) {
                int n3 = inflater.inflate(byArray2);
                if (n3 == 0) {
                    throw new IOException("Could not decompress data");
                }
                if (n > 0 && (n2 += n3) >= n) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                fastByteArrayOutputStream.write(byArray2, 0, n3);
            }
            return fastByteArrayOutputStream.toByteArray();
        }
        catch (DataFormatException dataFormatException) {
            throw new IOException("Unable to inflate Zlib stream", dataFormatException);
        }
    }

    private static DataFormatException a(DataFormatException dataFormatException) {
        return dataFormatException;
    }
}

