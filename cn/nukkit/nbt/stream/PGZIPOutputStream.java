/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.nbt.stream.PGZIPBlock;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PGZIPOutputStream
extends FilterOutputStream {
    private static final ExecutorService j = Executors.newCachedThreadPool();
    private static final int g = 35615;
    private final IntList h = new IntArrayList();
    private int i = 1;
    private int b = 0;
    private final ExecutorService f;
    private final int e;
    private final CRC32 c = new CRC32();
    private final BlockingQueue<Future<byte[]>> a;
    private PGZIPBlock d = new PGZIPBlock(this);
    private int k = 0;

    public static ExecutorService getSharedThreadPool() {
        return j;
    }

    protected Deflater newDeflater() {
        Deflater deflater = new Deflater(this.i, true);
        deflater.setStrategy(this.b);
        return deflater;
    }

    public void setStrategy(int n) {
        this.b = n;
    }

    public void setLevel(int n) {
        this.i = n;
    }

    protected static DeflaterOutputStream newDeflaterOutputStream(OutputStream outputStream, Deflater deflater) {
        return new DeflaterOutputStream(outputStream, deflater, 512, true);
    }

    public PGZIPOutputStream(OutputStream outputStream, ExecutorService executorService, int n) throws IOException {
        super(outputStream);
        this.f = executorService;
        this.e = n;
        this.a = new ArrayBlockingQueue<Future<byte[]>>(n);
        this.b();
    }

    public PGZIPOutputStream(OutputStream outputStream, int n) throws IOException {
        this(outputStream, PGZIPOutputStream.getSharedThreadPool(), n);
    }

    public PGZIPOutputStream(OutputStream outputStream) throws IOException {
        this(outputStream, Runtime.getRuntime().availableProcessors());
    }

    private void b() throws IOException {
        this.out.write(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 3});
    }

    @Override
    public void write(int n) throws IOException {
        byte[] byArray = new byte[]{(byte)(n & 0xFF)};
        this.write(byArray);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.write(byArray, 0, byArray.length);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.c.update(byArray, n, n2);
        this.k += n2;
        while (n2 > 0) {
            int n3 = this.d.in.length - this.d.in_length;
            if (n2 >= n3) {
                System.arraycopy(byArray, n, this.d.in, this.d.in_length, n3);
                this.d.in_length += n3;
                n += n3;
                n2 -= n3;
                this.c();
                continue;
            }
            System.arraycopy(byArray, n, this.d.in, this.d.in_length, n2);
            this.d.in_length += n2;
            break;
        }
    }

    private void c() throws IOException {
        this.a(this.e - 1);
        this.a.add(this.f.submit(this.d));
        this.d = new PGZIPBlock(this);
    }

    private void a() throws IOException, InterruptedException, ExecutionException {
        Future future;
        while ((future = (Future)this.a.peek()) != null) {
            if (!future.isDone()) {
                return;
            }
            this.a.remove();
            byte[] byArray = (byte[])future.get();
            this.h.add(byArray.length);
            this.out.write(byArray);
        }
        return;
    }

    private void a(int n) throws IOException {
        try {
            while (this.a.size() > n) {
                Future future = (Future)this.a.remove();
                byte[] byArray = (byte[])future.get();
                this.h.add(byArray.length);
                this.out.write(byArray);
            }
            this.a();
        }
        catch (ExecutionException executionException) {
            throw new IOException(executionException);
        }
        catch (InterruptedException interruptedException) {
            throw new InterruptedIOException();
        }
    }

    @Override
    public void flush() throws IOException {
        if (this.d.in_length > 0) {
            this.c();
        }
        this.a(0);
        super.flush();
    }

    @Override
    public void close() throws IOException {
        if (this.k >= 0) {
            this.flush();
            PGZIPOutputStream.newDeflaterOutputStream(this.out, this.newDeflater()).finish();
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putInt((int)this.c.getValue());
            byteBuffer.putInt(this.k);
            this.out.write(byteBuffer.array());
            this.out.flush();
            this.out.close();
            this.k = Integer.MIN_VALUE;
        }
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

