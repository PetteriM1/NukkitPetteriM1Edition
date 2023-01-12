/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

public class PersonaPiece {
    public final String id;
    public final String type;
    public final String packId;
    public final boolean isDefault;
    public final String productId;

    public PersonaPiece(String string, String string2, String string3, boolean bl, String string4) {
        this.id = string;
        this.type = string2;
        this.packId = string3;
        this.isDefault = bl;
        this.productId = string4;
    }

    public String toString() {
        return "PersonaPiece(id=" + this.id + ", type=" + this.type + ", packId=" + this.packId + ", isDefault=" + this.isDefault + ", productId=" + this.productId + ")";
    }
}

