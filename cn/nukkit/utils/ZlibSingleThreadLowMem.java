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

public class ZlibSingleThreadLowMem
implements e {
    private static final Deflater d = new Deflater(7);
    private static final Inflater c = new Inflater();
    private static final Deflater e = new Deflater(7, true);
    private static final Inflater b = new Inflater(true);
    private static final byte[] a = new byte[8192];

    @Override
    public synchronized byte[] deflate(byte[][] byArray, int n) throws IOException {
        d.reset();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        for (byte[] byArray2 : byArray) {
            d.setInput(byArray2);
            while (!d.needsInput()) {
                int n2 = d.deflate(a);
                fastByteArrayOutputStream.write(a, 0, n2);
            }
        }
        d.finish();
        while (!d.finished()) {
            int n3 = d.deflate(a);
            fastByteArrayOutputStream.write(a, 0, n3);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public synchronized byte[] deflate(byte[] byArray, int n) throws IOException {
        d.reset();
        d.setInput(byArray);
        d.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        while (!d.finished()) {
            int n2 = d.deflate(a);
            fastByteArrayOutputStream.write(a, 0, n2);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[][] byArray, int n) throws IOException {
        e.reset();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        for (byte[] byArray2 : byArray) {
            e.setInput(byArray2);
            while (!e.needsInput()) {
                int n2 = e.deflate(a);
                fastByteArrayOutputStream.write(a, 0, n2);
            }
        }
        e.finish();
        while (!e.finished()) {
            int n3 = e.deflate(a);
            fastByteArrayOutputStream.write(a, 0, n3);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[] byArray, int n) throws IOException {
        e.reset();
        e.setInput(byArray);
        e.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        while (!e.finished()) {
            int n2 = e.deflate(a);
            fastByteArrayOutputStream.write(a, 0, n2);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public synchronized byte[] inflate(byte[] byArray, int n) throws IOException {
        c.reset();
        c.setInput(byArray);
        c.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        try {
            int n2 = 0;
            while (!c.finished()) {
                int n3 = c.inflate(a);
                if (n > 0 && (n2 += n3) >= n) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                fastByteArrayOutputStream.write(a, 0, n3);
            }
            return fastByteArrayOutputStream.toByteArray();
        }
        catch (DataFormatException dataFormatException) {
            throw new IOException("Unable to inflate Zlib stream", dataFormatException);
        }
    }

    @Override
    public byte[] inflateRaw(byte[] byArray, int n) throws IOException {
        b.reset();
        b.setInput(byArray);
        b.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        try {
            int n2 = 0;
            while (!b.finished()) {
                int n3 = b.inflate(a);
                if (n3 == 0) {
                    throw new IOException("Could not decompress data");
                }
                if (n > 0 && (n2 += n3) >= n) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                fastByteArrayOutputStream.write(a, 0, n3);
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

