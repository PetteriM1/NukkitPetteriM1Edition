/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic.serializer;

import cn.nukkit.level.DimensionData;

public class NetworkChunkData {
    private final int b;
    private int d;
    private final boolean a;
    private final DimensionData c;

    public int getProtocol() {
        return this.b;
    }

    public int getChunkSections() {
        return this.d;
    }

    public boolean isAntiXray() {
        return this.a;
    }

    public DimensionData getDimensionData() {
        return this.c;
    }

    public void setChunkSections(int n) {
        this.d = n;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof NetworkChunkData)) {
            return false;
        }
        NetworkChunkData networkChunkData = (NetworkChunkData)object;
        if (!networkChunkData.canEqual(this)) {
            return false;
        }
        if (this.getProtocol() != networkChunkData.getProtocol()) {
            return false;
        }
        if (this.getChunkSections() != networkChunkData.getChunkSections()) {
            return false;
        }
        if (this.isAntiXray() != networkChunkData.isAntiXray()) {
            return false;
        }
        DimensionData dimensionData = this.getDimensionData();
        DimensionData dimensionData2 = networkChunkData.getDimensionData();
        return !(dimensionData == null ? dimensionData2 != null : !((Object)dimensionData).equals(dimensionData2));
    }

    protected boolean canEqual(Object object) {
        return object instanceof NetworkChunkData;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + this.getProtocol();
        n2 = n2 * 59 + this.getChunkSections();
        n2 = n2 * 59 + (this.isAntiXray() ? 79 : 97);
        DimensionData dimensionData = this.getDimensionData();
        n2 = n2 * 59 + (dimensionData == null ? 43 : ((Object)dimensionData).hashCode());
        return n2;
    }

    public String toString() {
        return "NetworkChunkData(protocol=" + this.getProtocol() + ", chunkSections=" + this.getChunkSections() + ", antiXray=" + this.isAntiXray() + ", dimensionData=" + this.getDimensionData() + ")";
    }

    public NetworkChunkData(int n, int n2, boolean bl, DimensionData dimensionData) {
        this.b = n;
        this.d = n2;
        this.a = bl;
        this.c = dimensionData;
    }
}

