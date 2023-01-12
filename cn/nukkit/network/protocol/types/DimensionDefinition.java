/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

public final class DimensionDefinition {
    private final String b;
    private final int c;
    private final int a;
    private final int d;

    public DimensionDefinition(String string, int n, int n2, int n3) {
        this.b = string;
        this.c = n;
        this.a = n2;
        this.d = n3;
    }

    public String getId() {
        return this.b;
    }

    public int getMaximumHeight() {
        return this.c;
    }

    public int getMinimumHeight() {
        return this.a;
    }

    public int getGeneratorType() {
        return this.d;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof DimensionDefinition)) {
            return false;
        }
        DimensionDefinition dimensionDefinition = (DimensionDefinition)object;
        String string = this.getId();
        String string2 = dimensionDefinition.getId();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        if (this.getMaximumHeight() != dimensionDefinition.getMaximumHeight()) {
            return false;
        }
        if (this.getMinimumHeight() != dimensionDefinition.getMinimumHeight()) {
            return false;
        }
        return this.getGeneratorType() == dimensionDefinition.getGeneratorType();
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        String string = this.getId();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        n2 = n2 * 59 + this.getMaximumHeight();
        n2 = n2 * 59 + this.getMinimumHeight();
        n2 = n2 * 59 + this.getGeneratorType();
        return n2;
    }

    public String toString() {
        return "DimensionDefinition(id=" + this.getId() + ", maximumHeight=" + this.getMaximumHeight() + ", minimumHeight=" + this.getMinimumHeight() + ", generatorType=" + this.getGeneratorType() + ")";
    }
}

