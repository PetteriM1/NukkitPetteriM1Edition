/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.noise.nukkit.f;

public abstract class NoiseF {
    protected int[] perm;
    protected float offsetX = 0.0f;
    protected float offsetY = 0.0f;
    protected float offsetZ = 0.0f;
    protected float octaves = 8.0f;
    protected float persistence;
    protected float expansion;

    public static int floor(float f2) {
        return f2 >= 0.0f ? (int)f2 : (int)(f2 - 1.0f);
    }

    public static float fade(float f2) {
        return f2 * f2 * f2 * (f2 * (f2 * 6.0f - 15.0f) + 10.0f);
    }

    public static float lerp(float f2, float f3, float f4) {
        return f3 + f2 * (f4 - f3);
    }

    public static float linearLerp(float f2, float f3, float f4, float f5, float f6) {
        return (f4 - f2) / (f4 - f3) * f5 + (f2 - f3) / (f4 - f3) * f6;
    }

    public static float bilinearLerp(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        float f12 = (f9 - f2) / (f9 - f8);
        float f13 = (f2 - f8) / (f9 - f8);
        return (f11 - f3) / (f11 - f10) * (f12 * f4 + f13 * f6) + (f3 - f10) / (f11 - f10) * (f12 * f5 + f13 * f7);
    }

    public static float trilinearLerp(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18) {
        float f19 = (f14 - f2) / (f14 - f13);
        float f20 = (f2 - f13) / (f14 - f13);
        float f21 = (f16 - f3) / (f16 - f15);
        float f22 = (f3 - f15) / (f16 - f15);
        return (f18 - f4) / (f18 - f17) * (f21 * (f19 * f5 + f20 * f9) + f22 * (f19 * f6 + f20 * f10)) + (f4 - f17) / (f18 - f17) * (f21 * (f19 * f7 + f20 * f11) + f22 * (f19 * f8 + f20 * f12));
    }

    public static float grad(int n, float f2, float f3, float f4) {
        float f5;
        float f6 = f5 = (n &= 0xF) < 8 ? f2 : f3;
        float f7 = n < 4 ? f3 : (n == 12 || n == 14 ? f2 : f4);
        return ((n & 1) == 0 ? f5 : -f5) + ((n & 2) == 0 ? f7 : -f7);
    }

    public abstract float getNoise2D(float var1, float var2);

    public abstract float getNoise3D(float var1, float var2, float var3);

    public float noise2D(float f2, float f3) {
        return this.noise2D(f2, f3, false);
    }

    public float noise2D(float f2, float f3, boolean bl) {
        float f4 = 0.0f;
        float f5 = 1.0f;
        float f6 = 1.0f;
        float f7 = 0.0f;
        f2 *= this.expansion;
        f3 *= this.expansion;
        int n = 0;
        while ((float)n < this.octaves) {
            f4 += this.getNoise2D(f2 * f6, f3 * f6) * f5;
            f7 += f5;
            f6 *= 2.0f;
            f5 *= this.persistence;
            ++n;
        }
        if (bl) {
            f4 /= f7;
        }
        return f4;
    }

    public float noise3D(float f2, float f3, float f4) {
        return this.noise3D(f2, f3, f4, false);
    }

    public float noise3D(float f2, float f3, float f4, boolean bl) {
        float f5 = 0.0f;
        float f6 = 1.0f;
        float f7 = 1.0f;
        float f8 = 0.0f;
        f2 *= this.expansion;
        f3 *= this.expansion;
        f4 *= this.expansion;
        int n = 0;
        while ((float)n < this.octaves) {
            f5 += this.getNoise3D(f2 * f7, f3 * f7, f4 * f7) * f6;
            f8 += f6;
            f7 *= 2.0f;
            f6 *= this.persistence;
            ++n;
        }
        if (bl) {
            f5 /= f8;
        }
        return f5;
    }

    public void setOffset(float f2, float f3, float f4) {
        this.offsetX = f2;
        this.offsetY = f3;
        this.offsetZ = f4;
    }
}

