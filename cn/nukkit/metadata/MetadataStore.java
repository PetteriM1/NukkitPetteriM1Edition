/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.ServerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class MetadataStore {
    private final Map<String, Map<Plugin, MetadataValue>> a = new HashMap<String, Map<Plugin, MetadataValue>>();

    public void setMetadata(Object object, String string2, MetadataValue metadataValue) {
        if (metadataValue == null) {
            throw new ServerException("Value cannot be null");
        }
        Plugin plugin = metadataValue.getOwningPlugin();
        if (plugin == null) {
            throw new PluginException("Plugin cannot be null");
        }
        String string3 = this.disambiguate((Metadatable)object, string2);
        Map map = this.a.computeIfAbsent(string3, string -> new WeakHashMap(1));
        map.put(plugin, metadataValue);
    }

    public List<MetadataValue> getMetadata(Object object, String string) {
        String string2 = this.disambiguate((Metadatable)object, string);
        if (this.a.containsKey(string2)) {
            Collection<MetadataValue> collection = this.a.get(string2).values();
            return Collections.unmodifiableList(new ArrayList<MetadataValue>(collection));
        }
        return Collections.emptyList();
    }

    public boolean hasMetadata(Object object, String string) {
        return this.a.containsKey(this.disambiguate((Metadatable)object, string));
    }

    public void removeMetadata(Object object, String string, Plugin plugin) {
        block2: {
            if (plugin == null) {
                throw new PluginException("Plugin cannot be null");
            }
            String string2 = this.disambiguate((Metadatable)object, string);
            Map<Plugin, MetadataValue> map = this.a.get(string2);
            if (map == null) {
                return;
            }
            map.remove(plugin);
            if (!map.isEmpty()) break block2;
            this.a.remove(string2);
        }
    }

    public void invalidateAll(Plugin plugin) {
        if (plugin == null) {
            throw new PluginException("Plugin cannot be null");
        }
        for (Map<Plugin, MetadataValue> map : this.a.values()) {
            if (!map.containsKey(plugin)) continue;
            map.get(plugin).invalidate();
        }
    }

    protected abstract String disambiguate(Metadatable var1, String var2);

    private static ServerException a(ServerException serverException) {
        return serverException;
    }
}

