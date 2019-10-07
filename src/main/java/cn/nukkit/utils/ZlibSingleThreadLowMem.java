package cn.nukkit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;

public class ZlibSingleThreadLowMem implements ZlibProvider {

    private static final int BUFFER_SIZE = 8192;
    private Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
    private byte[] buffer = new byte[BUFFER_SIZE];

    @Override
    public synchronized byte[] deflate(byte[][] datas, int level) throws Exception {
        Deflater deflater = this.deflater;
        if (deflater == null) throw new IllegalArgumentException("No deflate for level " + level + " !");
        deflater.reset();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(datas.length);
        for (byte[] data : datas) {
            deflater.setInput(data);
            while (!deflater.needsInput()) {
                int i = deflater.deflate(buffer);
                bos.write(buffer, 0, i);
            }
        }
        deflater.finish();
        while (!deflater.finished()) {
            int i = deflater.deflate(buffer);
            bos.write(buffer, 0, i);
        }
        return bos.toByteArray();
    }

    @Override
    synchronized public byte[] deflate(byte[] data, int level) throws Exception {
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            while (!deflater.finished()) {
                int i = deflater.deflate(buffer);
                bos.write(buffer, 0, i);
            }
        } finally {}
        return bos.toByteArray();
    }

    @Override
    synchronized public byte[] inflate(InputStream stream) throws IOException {
        InflaterInputStream inputStream = new InflaterInputStream(stream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
        byte[] bufferOut;
        int length;

        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            outputStream.flush();
            bufferOut = outputStream.toByteArray();
            outputStream.close();
            inputStream.close();
        }

        return bufferOut;
    }
}
