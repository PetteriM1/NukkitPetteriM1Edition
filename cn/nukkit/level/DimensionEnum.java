/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.DimensionData;

public enum DimensionEnum {
    OVERWORLD(new DimensionData(0, -64, 319)),
    NETHER(new DimensionData(1, 0, 127)),
    END(new DimensionData(2, 0, 255));

    private final DimensionData a;

    private DimensionEnum(DimensionData dimensionData) {
        this.a = dimensionData;
    }

    public DimensionData getDimensionData() {
        return this.a;
    }

    public static DimensionData getDataFromId(int n) {
        for (DimensionEnum dimensionEnum : DimensionEnum.values()) {
            if (dimensionEnum.getDimensionData().getDimensionId() != n) continue;
            return dimensionEnum.getDimensionData();
        }
        return null;
    }
}

