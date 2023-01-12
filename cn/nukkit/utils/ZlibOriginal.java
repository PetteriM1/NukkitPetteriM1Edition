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

public class ZlibOriginal
implements e {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] deflate(byte[][] byArray, int n) throws IOException {
        Deflater deflater = new Deflater(n);
        byte[] byArray2 = new byte[1024];
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        try {
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
        }
        finally {
            deflater.end();
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] deflate(byte[] byArray, int n) throws IOException {
        Deflater deflater = new Deflater(n);
        deflater.setInput(byArray);
        deflater.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = new byte[1024];
        try {
            while (!deflater.finished()) {
                int n2 = deflater.deflate(byArray2);
                fastByteArrayOutputStream.write(byArray2, 0, n2);
            }
        }
        finally {
            deflater.end();
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] deflateRaw(byte[][] byArray, int n) throws IOException {
        Deflater deflater = new Deflater(n, true);
        byte[] byArray2 = new byte[1024];
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        try {
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
        }
        finally {
            deflater.end();
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] deflateRaw(byte[] byArray, int n) throws IOException {
        Deflater deflater = new Deflater(n, true);
        deflater.setInput(byArray);
        deflater.finish();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = new byte[1024];
        try {
            while (!deflater.finished()) {
                int n2 = deflater.deflate(byArray2);
                fastByteArrayOutputStream.write(byArray2, 0, n2);
            }
        }
        finally {
            deflater.end();
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] inflate(byte[] byArray, int n) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(byArray);
        inflater.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = new byte[1024];
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
        Inflater inflater = new Inflater(true);
        inflater.setInput(byArray);
        inflater.finished();
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get();
        fastByteArrayOutputStream.reset();
        byte[] byArray2 = new byte[1024];
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

