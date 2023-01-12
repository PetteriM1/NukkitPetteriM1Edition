/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

public enum AuthInteractionModel {
    TOUCH,
    CROSSHAIR,
    CLASSIC;

    private static final AuthInteractionModel[] a;

    public static AuthInteractionModel fromOrdinal(int n) {
        return a[n];
    }

    static {
        a = AuthInteractionModel.values();
    }
}

