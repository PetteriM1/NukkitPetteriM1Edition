/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.entity.Entity;
import cn.nukkit.metadata.MetadataStore;
import cn.nukkit.metadata.Metadatable;

public class EntityMetadataStore
extends MetadataStore {
    @Override
    protected String disambiguate(Metadatable metadatable, String string) {
        if (!(metadatable instanceof Entity)) {
            throw new IllegalArgumentException("Argument must be an Entity instance");
        }
        return ((Entity)metadatable).getId() + ":" + string;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

