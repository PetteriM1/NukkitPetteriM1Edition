/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.anvil.util;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public class NibbleArray
implements Cloneable {
    private final byte[] a;

    public NibbleArray(int n) {
        this.a = new byte[n >> 1];
    }

    public NibbleArray(byte[] byArray) {
        this.a = byArray;
    }

    public byte get(int n) {
        if (n >= this.a.length << 1) {
            throw new IndexOutOfBoundsException();
        }
        byte by = this.a[n >> 1];
        if ((n & 1) == 0) {
            return (byte)(by & 0xF);
        }
        return (byte)((by & 0xF0) >>> 4);
    }

    public void set(int n, byte by) {
        if (by != (by & 0xF)) {
            throw new IllegalArgumentException("Nibbles must have a value between 0 and 15.");
        }
        if (n >= this.a.length << 1 || n < 0) {
            throw new IndexOutOfBoundsException();
        }
        by = (byte)(by & 0xF);
        int n2 = n >> 1;
        byte by2 = this.a[n2];
        this.a[n2] = (n & 1) == 0 ? (byte)(by2 & 0xF0 | by) : (byte)(by2 & 0xF | by << 4);
    }

    public void remove(int n) {
        if (n >= this.a.length << 1 || n < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n2 = n >> 1;
        byte by = this.a[n2];
        this.a[n2] = (n & 1) == 0 ? (byte)(by & 0xF0) : (byte)(by & 0xF);
    }

    public void fill(byte by) {
        Preconditions.checkArgument(by >= 0 && by < 16, "Nibbles must have a value between 0 and 15.");
        by = (byte)(by & 0xF);
        Arrays.fill(this.a, (byte)(by << 4 | by));
    }

    public void copyFrom(byte[] byArray) {
        Preconditions.checkNotNull(byArray, "bytes");
        Preconditions.checkArgument(byArray.length == this.a.length, "length of provided byte array is %s but expected %s", byArray.length, this.a.length);
        System.arraycopy(byArray, 0, this.a, 0, this.a.length);
    }

    public void copyFrom(NibbleArray nibbleArray) {
        Preconditions.checkNotNull(nibbleArray, "array");
        this.copyFrom(nibbleArray.a);
    }

    public byte[] getData() {
        return this.a;
    }

    public NibbleArray copy() {
        return new NibbleArray((byte[])this.getData().clone());
    }

    private static IndexOutOfBoundsException a(IndexOutOfBoundsException indexOutOfBoundsException) {
        return indexOutOfBoundsException;
    }
}

