/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

public class NukkitRandom {
    protected long seed;

    public NukkitRandom() {
        this(-1L);
    }

    public NukkitRandom(long l) {
        if (l == -1L) {
            l = System.currentTimeMillis() / 1000L;
        }
        this.setSeed(l);
    }

    public void setSeed(long l) {
        CRC32 cRC32 = new CRC32();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt((int)l);
        cRC32.update(byteBuffer.array());
        this.seed = cRC32.getValue();
    }

    public int nextSignedInt() {
        int n = ((int)(this.seed * 65535L + 31337L) >> 8) + 1337;
        this.seed ^= (long)n;
        return n;
    }

    public int nextInt() {
        return this.nextSignedInt() & Integer.MAX_VALUE;
    }

    public double nextDouble() {
        return (double)this.nextInt() / 2.147483647E9;
    }

    public float nextFloat() {
        return (float)this.nextInt() / 2.14748365E9f;
    }

    public float nextSignedFloat() {
        return (float)this.nextInt() / 2.14748365E9f;
    }

    public double nextSignedDouble() {
        return (double)this.nextSignedInt() / 2.147483647E9;
    }

    public boolean nextBoolean() {
        return (this.nextSignedInt() & 1) == 0;
    }

    public int nextRange() {
        return this.nextRange(0, Integer.MAX_VALUE);
    }

    public int nextRange(int n) {
        return this.nextRange(n, Integer.MAX_VALUE);
    }

    public int nextRange(int n, int n2) {
        return n + this.nextInt() % (n2 + 1 - n);
    }

    public int nextBoundedInt(int n) {
        return n == 0 ? 0 : this.nextInt() % n;
    }
}

