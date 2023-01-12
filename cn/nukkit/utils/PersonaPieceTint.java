/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class PersonaPieceTint {
    public final String pieceType;
    public final ImmutableList<String> colors;

    public PersonaPieceTint(String string, List<String> list) {
        this.pieceType = string;
        this.colors = ImmutableList.copyOf(list);
    }

    public String toString() {
        return "PersonaPieceTint(pieceType=" + this.pieceType + ", colors=" + this.colors + ")";
    }
}

