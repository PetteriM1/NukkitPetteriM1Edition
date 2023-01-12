/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.util;

import cn.nukkit.level.util.BitArray;
import cn.nukkit.level.util.BitArrayVersion;
import cn.nukkit.math.MathHelper;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class Pow2BitArray
implements BitArray {
    private final int[] b;
    private final BitArrayVersion a;
    private final int c;

    Pow2BitArray(BitArrayVersion bitArrayVersion, int n, int[] nArray) {
        this.c = n;
        this.a = bitArrayVersion;
        this.b = nArray;
        int n2 = MathHelper.ceil((float)n / (float)bitArrayVersion.e);
        if (nArray.length != n2) {
            throw new IllegalArgumentException("Invalid length given for storage, got: " + nArray.length + " but expected: " + n2);
        }
    }

    @Override
    public void set(int n, int n2) {
        Preconditions.checkElementIndex(n, this.c);
        Preconditions.checkArgument(n2 >= 0 && n2 <= this.a.a, "Max value: %s. Received value", this.a.a, n2);
        int n3 = n * this.a.b;
        int n4 = n3 >> 5;
        int n5 = n3 & 0x1F;
        this.b[n4] = this.b[n4] & ~(this.a.a << n5) | (n2 & this.a.a) << n5;
    }

    @Override
    public int get(int n) {
        Preconditions.checkElementIndex(n, this.c);
        int n2 = n * this.a.b;
        int n3 = n2 >> 5;
        int n4 = n2 & 0x1F;
        return this.b[n3] >>> n4 & this.a.a;
    }

    @Override
    public int size() {
        return this.c;
    }

    @Override
    public int[] getWords() {
        return this.b;
    }

    @Override
    public BitArrayVersion getVersion() {
        return this.a;
    }

    @Override
    public BitArray copy() {
        return new Pow2BitArray(this.a, this.c, Arrays.copyOf(this.b, this.b.length));
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

