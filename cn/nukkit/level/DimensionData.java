/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

public class DimensionData {
    public static final DimensionData LEGACY_DIMENSION = new DimensionData(0, 0, 255);
    private final int d;
    private final int a;
    private final int b;
    private final int c;

    public DimensionData(int n, int n2, int n3) {
        this.d = n;
        this.a = n2;
        this.b = n3;
        int n4 = n3 - n2;
        if (n2 <= 0 && n3 > 0) {
            ++n4;
        }
        this.c = n4;
    }

    public int getDimensionId() {
        return this.d;
    }

    public int getMinHeight() {
        return this.a;
    }

    public int getMaxHeight() {
        return this.b;
    }

    public int getHeight() {
        return this.c;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof DimensionData)) {
            return false;
        }
        DimensionData dimensionData = (DimensionData)object;
        if (!dimensionData.canEqual(this)) {
            return false;
        }
        if (this.getDimensionId() != dimensionData.getDimensionId()) {
            return false;
        }
        if (this.getMinHeight() != dimensionData.getMinHeight()) {
            return false;
        }
        if (this.getMaxHeight() != dimensionData.getMaxHeight()) {
            return false;
        }
        return this.getHeight() == dimensionData.getHeight();
    }

    protected boolean canEqual(Object object) {
        return object instanceof DimensionData;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + this.getDimensionId();
        n2 = n2 * 59 + this.getMinHeight();
        n2 = n2 * 59 + this.getMaxHeight();
        n2 = n2 * 59 + this.getHeight();
        return n2;
    }

    public String toString() {
        return "DimensionData(dimensionId=" + this.getDimensionId() + ", minHeight=" + this.getMinHeight() + ", maxHeight=" + this.getMaxHeight() + ", height=" + this.getHeight() + ")";
    }
}

