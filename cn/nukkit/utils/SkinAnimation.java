/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.SerializedImage;

public class SkinAnimation {
    public final SerializedImage image;
    public final int type;
    public final float frames;
    public final int expression;

    public SkinAnimation(SerializedImage serializedImage, int n, float f2, int n2) {
        this.image = serializedImage;
        this.type = n;
        this.frames = f2;
        this.expression = n2;
    }

    public String toString() {
        return "SkinAnimation(image=" + this.image + ", type=" + this.type + ", frames=" + this.frames + ", expression=" + this.expression + ")";
    }
}

