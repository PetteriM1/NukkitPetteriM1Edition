/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import java.util.Locale;

public final class Angle
implements Comparable<Angle> {
    private final float a;
    private final double d;
    private final boolean b;
    private final boolean c;

    public static Angle fromDegree(float f2) {
        return new Angle(f2, true);
    }

    public static Angle fromDegree(double d2) {
        return new Angle(d2, true);
    }

    public static Angle fromRadian(float f2) {
        return new Angle(f2, false);
    }

    public static Angle fromRadian(double d2) {
        return new Angle(d2, false);
    }

    public static Angle asin(double d2) {
        return Angle.fromRadian(Math.asin(d2));
    }

    public static Angle acos(double d2) {
        return Angle.fromRadian(Math.acos(d2));
    }

    public static Angle atan(double d2) {
        return Angle.fromRadian(Math.atan(d2));
    }

    public double sin() {
        return Math.sin(this.asDoubleRadian());
    }

    public double cos() {
        return Math.cos(this.asDoubleRadian());
    }

    public double tan() {
        return Math.tan(this.asDoubleRadian());
    }

    public float asFloatRadian() {
        if (this.c) {
            if (this.b) {
                return (float)(this.d * Math.PI / 180.0);
            }
            return (float)this.d;
        }
        if (this.b) {
            return this.a * (float)Math.PI / 180.0f;
        }
        return this.a;
    }

    public double asDoubleRadian() {
        if (this.c) {
            if (this.b) {
                return this.d * Math.PI / 180.0;
            }
            return this.d;
        }
        if (this.b) {
            return (double)this.a * Math.PI / 180.0;
        }
        return this.a;
    }

    public float asFloatDegree() {
        if (this.c) {
            if (this.b) {
                return (float)this.d;
            }
            return (float)(this.d * 180.0 / Math.PI);
        }
        if (this.b) {
            return this.a;
        }
        return this.a * 180.0f / (float)Math.PI;
    }

    public double asDoubleDegree() {
        if (this.c) {
            if (this.b) {
                return this.d;
            }
            return this.d * 180.0 / Math.PI;
        }
        if (this.b) {
            return this.a;
        }
        return (double)this.a * 180.0 / Math.PI;
    }

    public static int compare(Angle angle, Angle angle2) {
        return angle.compareTo(angle2);
    }

    public String toString() {
        Object[] objectArray = new Object[6];
        objectArray[0] = this.c ? "Double" : "Float";
        objectArray[1] = this.c ? this.d : (double)this.a;
        Object object = objectArray[2] = this.b ? "deg" : "rad";
        objectArray[3] = this.b ? (this.c ? this.asDoubleRadian() : (double)this.asFloatRadian()) : (this.c ? this.asDoubleDegree() : (double)this.asFloatDegree());
        objectArray[4] = this.b ? "rad" : "deg";
        objectArray[5] = this.hashCode();
        return String.format(Locale.ROOT, "Angle[%s, %f%s = %f%s] [%d]", objectArray);
    }

    @Override
    public int compareTo(Angle angle) {
        return Double.compare(this.asDoubleRadian(), angle.asDoubleRadian());
    }

    public boolean equals(Object object) {
        return object instanceof Angle && this.compareTo((Angle)object) == 0;
    }

    public int hashCode() {
        int n = this.c ? Double.hashCode(this.d) : Float.hashCode(this.a);
        if (this.b) {
            n ^= 0xABCD1234;
        }
        return n;
    }

    private Angle(float f2, boolean bl) {
        this.c = false;
        this.a = f2;
        this.d = 0.0;
        this.b = bl;
    }

    private Angle(double d2, boolean bl) {
        this.c = true;
        this.a = 0.0f;
        this.d = d2;
        this.b = bl;
    }
}

