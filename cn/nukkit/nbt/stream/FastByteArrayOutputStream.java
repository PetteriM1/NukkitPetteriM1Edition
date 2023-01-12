/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FastByteArrayOutputStream
extends OutputStream {
    public static final long ONEOVERPHI = 106039L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public byte[] array;
    public int length;
    private int a;

    public FastByteArrayOutputStream() {
        this(16);
    }

    public FastByteArrayOutputStream(int n) {
        this.array = new byte[n];
    }

    public FastByteArrayOutputStream(byte[] byArray) {
        this.array = byArray;
    }

    public FastByteArrayOutputStream reset() {
        this.length = 0;
        this.a = 0;
        return this;
    }

    @Override
    public void write(int n) {
        if (this.a == this.length) {
            ++this.length;
            if (this.a == this.array.length) {
                this.array = FastByteArrayOutputStream.grow(this.array, this.length);
            }
        }
        this.array[this.a++] = (byte)n;
    }

    public static void ensureOffsetLength(int n, int n2, int n3) {
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + n2 + ") is negative");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("Length (" + n3 + ") is negative");
        }
        if (n2 + n3 > n) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (n2 + n3) + ") is greater than array length (" + n + ')');
        }
    }

    public static byte[] grow(byte[] byArray, int n) {
        if (n > byArray.length) {
            byte[] byArray2 = new byte[(int)Math.min(Math.max(106039L * (long)byArray.length >>> 16, (long)n), Integer.MAX_VALUE)];
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            return byArray2;
        }
        return byArray;
    }

    public static byte[] grow(byte[] byArray, int n, int n2) {
        if (n > byArray.length) {
            byte[] byArray2 = new byte[(int)Math.min(Math.max(106039L * (long)byArray.length >>> 16, (long)n), Integer.MAX_VALUE)];
            System.arraycopy(byArray, 0, byArray2, 0, n2);
            return byArray2;
        }
        return byArray;
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        block1: {
            if (this.a + n2 > this.array.length) {
                this.array = FastByteArrayOutputStream.grow(this.array, this.a + n2, this.a);
            }
            System.arraycopy(byArray, n, this.array, this.a, n2);
            if (this.a + n2 <= this.length) break block1;
            this.length = this.a += n2;
        }
    }

    public void position(long l) {
        if (this.a > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Position too large: " + l);
        }
        this.a = (int)l;
    }

    public long position() {
        return this.a;
    }

    public long length() throws IOException {
        return this.length;
    }

    public byte[] toByteArray() {
        if (this.a == this.array.length) {
            return this.array;
        }
        return Arrays.copyOfRange(this.array, 0, this.a);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

