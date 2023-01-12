/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.custom;

import cn.nukkit.entity.custom.EntityDefinition;

public interface CustomEntity {
    public EntityDefinition getEntityDefinition();

    default public String getIdentifier() {
        return this.getEntityDefinition().getIdentifier();
    }

    default public int getNetworkId() {
        return this.getEntityDefinition().getRuntimeId();
    }
}

