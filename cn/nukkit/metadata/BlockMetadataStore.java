/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.metadata.MetadataStore;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.plugin.Plugin;
import java.util.List;

public class BlockMetadataStore
extends MetadataStore {
    private final Level b;

    public BlockMetadataStore(Level level) {
        this.b = level;
    }

    @Override
    protected String disambiguate(Metadatable metadatable, String string) {
        if (!(metadatable instanceof Block)) {
            throw new IllegalArgumentException("Argument must be a Block instance");
        }
        return ((Block)metadatable).x + ":" + ((Block)metadatable).y + ':' + ((Block)metadatable).z + ':' + string;
    }

    @Override
    public List<MetadataValue> getMetadata(Object object, String string) {
        if (!(object instanceof Block)) {
            throw new IllegalArgumentException("Object must be a Block");
        }
        if (((Block)object).getLevel() == this.b) {
            return super.getMetadata(object, string);
        }
        throw new IllegalStateException("Block does not belong to world " + this.b.getName());
    }

    @Override
    public boolean hasMetadata(Object object, String string) {
        if (!(object instanceof Block)) {
            throw new IllegalArgumentException("Object must be a Block");
        }
        if (((Block)object).getLevel() == this.b) {
            return super.hasMetadata(object, string);
        }
        throw new IllegalStateException("Block does not belong to world " + this.b.getName());
    }

    @Override
    public void removeMetadata(Object object, String string, Plugin plugin) {
        if (!(object instanceof Block)) {
            throw new IllegalArgumentException("Object must be a Block");
        }
        if (((Block)object).getLevel() != this.b) {
            throw new IllegalStateException("Block does not belong to world " + this.b.getName());
        }
        super.removeMetadata(object, string, plugin);
    }

    @Override
    public void setMetadata(Object object, String string, MetadataValue metadataValue) {
        if (!(object instanceof Block)) {
            throw new IllegalArgumentException("Object must be a Block");
        }
        if (((Block)object).getLevel() != this.b) {
            throw new IllegalStateException("Block does not belong to world " + this.b.getName());
        }
        super.setMetadata(object, string, metadataValue);
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

