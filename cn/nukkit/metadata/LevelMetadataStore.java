/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.level.Level;
import cn.nukkit.metadata.MetadataStore;
import cn.nukkit.metadata.Metadatable;

public class LevelMetadataStore
extends MetadataStore {
    @Override
    protected String disambiguate(Metadatable metadatable, String string) {
        if (!(metadatable instanceof Level)) {
            throw new IllegalArgumentException("Argument must be a Level instance");
        }
        return (((Level)metadatable).getName() + ':' + string).toLowerCase();
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

