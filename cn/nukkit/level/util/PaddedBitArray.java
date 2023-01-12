/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.util;

import cn.nukkit.level.util.BitArray;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.math.MathHelper;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class PaddedBitArray
implements BitArray {
    private final int[] c;
    private final BitArrayVersion a;
    private final int b;

    PaddedBitArray(BitArrayVersion bitArrayVersion, int n, int[] nArray) {
        this.b = n;
        this.a = bitArrayVersion;
        this.c = nArray;
        int n2 = MathHelper.ceil((float)n / (float)bitArrayVersion.e);
        if (nArray.length != n2) {
            throw new IllegalArgumentException("Invalid length given for storage, got: " + nArray.length + " but expected: " + n2);
        }
    }

    @Override
    public void set(int n, int n2) {
        Preconditions.checkElementIndex(n, this.b);
        Preconditions.checkArgument(n2 >= 0 && n2 <= this.a.a, "Max value: %s. Received value", this.a.a, n2);
        int n3 = n / this.a.e;
        int n4 = n % this.a.e * this.a.b;
        this.c[n3] = this.c[n3] & ~(this.a.a << n4) | (n2 & this.a.a) << n4;
    }

    @Override
    public int get(int n) {
        Preconditions.checkElementIndex(n, this.b);
        int n2 = n / this.a.e;
        int n3 = n % this.a.e * this.a.b;
        return this.c[n2] >>> n3 & this.a.a;
    }

    @Override
    public int size() {
        return this.b;
    }

    @Override
    public int[] getWords() {
        return this.c;
    }

    @Override
    public BitArrayVersion getVersion() {
        return this.a;
    }

    @Override
    public BitArray copy() {
        return new PaddedBitArray(this.a, this.b, Arrays.copyOf(this.c, this.c.length));
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

