/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.IPlayer;
import cn.nukkit.metadata.MetadataStore;
import cn.nukkit.metadata.Metadatable;

public class PlayerMetadataStore
extends MetadataStore {
    @Override
    protected String disambiguate(Metadatable metadatable, String string) {
        if (!(metadatable instanceof IPlayer)) {
            throw new IllegalArgumentException("Argument must be an IPlayer instance");
        }
        return (((IPlayer)metadatable).getName() + ':' + string).toLowerCase();
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

