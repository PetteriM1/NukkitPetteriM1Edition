/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metadata;

import cn.nukkit.plugin.Plugin;
import java.lang.ref.WeakReference;

public abstract class MetadataValue {
    protected final WeakReference<Plugin> owningPlugin;

    protected MetadataValue(Plugin plugin) {
        this.owningPlugin = new WeakReference<Plugin>(plugin);
    }

    public Plugin getOwningPlugin() {
        return (Plugin)this.owningPlugin.get();
    }

    public abstract Object value();

    public abstract void invalidate();
}

