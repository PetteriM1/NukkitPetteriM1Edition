/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.plugin.Plugin;
import java.util.List;

public interface Metadatable {
    public void setMetadata(String var1, MetadataValue var2) throws Exception;

    public List<MetadataValue> getMetadata(String var1) throws Exception;

    public boolean hasMetadata(String var1) throws Exception;

    public void removeMetadata(String var1, Plugin var2) throws Exception;
}

