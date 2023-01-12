/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.permission.Permissible;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionRemovedExecutor;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.PluginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionAttachment {
    private PermissionRemovedExecutor d = null;
    private final Map<String, Boolean> b = new HashMap<String, Boolean>();
    private final Permissible a;
    private final Plugin c;

    public PermissionAttachment(Plugin plugin, Permissible permissible) {
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }
        this.a = permissible;
        this.c = plugin;
    }

    public Plugin getPlugin() {
        return this.c;
    }

    public void setRemovalCallback(PermissionRemovedExecutor permissionRemovedExecutor) {
        this.d = permissionRemovedExecutor;
    }

    public PermissionRemovedExecutor getRemovalCallback() {
        return this.d;
    }

    public Map<String, Boolean> getPermissions() {
        return this.b;
    }

    public void clearPermissions() {
        this.b.clear();
        this.a.recalculatePermissions();
    }

    public void setPermissions(Map<String, Boolean> map) {
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            String string = entry.getKey();
            Boolean bl = entry.getValue();
            this.b.put(string, bl);
        }
        this.a.recalculatePermissions();
    }

    public void unsetPermissions(List<String> list) {
        for (String string : list) {
            this.b.remove(string);
        }
        this.a.recalculatePermissions();
    }

    public void setPermission(Permission permission, boolean bl) {
        this.setPermission(permission.getName(), bl);
    }

    public void setPermission(String string, boolean bl) {
        if (this.b.containsKey(string)) {
            if (this.b.get(string).equals(bl)) {
                return;
            }
            this.b.remove(string);
        }
        this.b.put(string, bl);
        this.a.recalculatePermissions();
    }

    public void unsetPermission(Permission permission, boolean bl) {
        this.unsetPermission(permission.getName(), bl);
    }

    public void unsetPermission(String string, boolean bl) {
        block0: {
            if (!this.b.containsKey(string)) break block0;
            this.b.remove(string);
            this.a.recalculatePermissions();
        }
    }

    public void remove() {
        this.a.removeAttachment(this);
    }

    private static PluginException a(PluginException pluginException) {
        return pluginException;
    }
}

