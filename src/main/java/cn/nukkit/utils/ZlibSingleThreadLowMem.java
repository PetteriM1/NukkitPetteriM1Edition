package cn.nukkit.utils;

import cn.nukkit.nbt.stream.FastByteArrayOutputStream;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibSingleThreadLowMem implements ZlibProvider {

    private static final Deflater DEFLATER = new Deflater(7);
    private static final Inflater INFLATER = new Inflater();
    private static final Deflater DEFLATER_RAW = new Deflater(7, true);
    private static final Inflater INFLATER_RAW = new Inflater(true);
    private static final byte[] BUFFER = new byte[8192];

    @Override
    public synchronized byte[] deflate(byte[][] datas, int level) throws IOException {
        DEFLATER.reset();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        for (byte[] data : datas) {
            DEFLATER.setInput(data);
            while (!DEFLATER.needsInput()) {
                int i = DEFLATER.deflate(BUFFER);
                bos.write(BUFFER, 0, i);
            }
        }
        DEFLATER.finish();
        while (!DEFLATER.finished()) {
            int i = DEFLATER.deflate(BUFFER);
            bos.write(BUFFER, 0, i);
        }
        //Deflater::end is called the time when the process exits.
        return bos.toByteArray();
    }

    @Override
    public synchronized byte[] deflate(byte[] data, int level) throws IOException {
        DEFLATER.reset();
        DEFLATER.setInput(data);
        DEFLATER.finish();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        //try {
            while (!DEFLATER.finished()) {
                int i = DEFLATER.deflate(BUFFER);
                bos.write(BUFFER, 0, i);
            }
        /*} finally {
            deflater.end();
        }*/
        return bos.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[][] datas, int level) throws IOException {
        DEFLATER_RAW.reset();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        for (byte[] data : datas) {
            DEFLATER_RAW.setInput(data);
            while (!DEFLATER_RAW.needsInput()) {
                int i = DEFLATER_RAW.deflate(BUFFER);
                bos.write(BUFFER, 0, i);
            }
        }
        DEFLATER_RAW.finish();
        while (!DEFLATER_RAW.finished()) {
            int i = DEFLATER_RAW.deflate(BUFFER);
            bos.write(BUFFER, 0, i);
        }
        //Deflater::end is called the time when the process exits.
        return bos.toByteArray();
    }

    @Override
    public byte[] deflateRaw(byte[] data, int level) throws IOException {
        DEFLATER_RAW.reset();
        DEFLATER_RAW.setInput(data);
        DEFLATER_RAW.finish();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        //try {
        while (!DEFLATER_RAW.finished()) {
            int i = DEFLATER_RAW.deflate(BUFFER);
            bos.write(BUFFER, 0, i);
        }
        /*} finally {
            deflater.end();
        }*/
        return bos.toByteArray();
    }

    @Override
    public synchronized byte[] inflate(byte[] data, int maxSize) throws IOException {
        INFLATER.reset();
        INFLATER.setInput(data);
        INFLATER.finished();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        try {
            int length = 0;
            while (!INFLATER.finished()) {
                int i = INFLATER.inflate(BUFFER);
                length += i;
                if (maxSize > 0 && length >= maxSize) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                bos.write(BUFFER, 0, i);
            }
            return bos.toByteArray();
        } catch (DataFormatException e) {
            throw new IOException("Unable to inflate Zlib stream", e);
        }
    }

    @Override
    public byte[] inflateRaw(byte[] data, int maxSize) throws IOException {
        INFLATER_RAW.reset();
        INFLATER_RAW.setInput(data);
        INFLATER_RAW.finished();
        FastByteArrayOutputStream bos = ThreadCache.fbaos.get();
        bos.reset();
        try {
            int length = 0;
            while (!INFLATER_RAW.finished()) {
                int i = INFLATER_RAW.inflate(BUFFER);
                if (i == 0) {
                    throw new IOException("Could not decompress data");
                }
                length += i;
                if (maxSize > 0 && length >= maxSize) {
                    throw new IOException("Inflated data exceeds maximum size");
                }
                bos.write(BUFFER, 0, i);
            }
            return bos.toByteArray();
        } catch (DataFormatException e) {
            throw new IOException("Unable to inflate Zlib stream", e);
        }
    }
}
