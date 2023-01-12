/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.permission.Permissible;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.permission.PermissionRemovedExecutor;
import cn.nukkit.permission.ServerOperator;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.ServerException;
import co.aikar.timings.Timings;
import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PermissibleBase
implements Permissible {
    ServerOperator b;
    private Permissible a = null;
    @NOBF
    private final Set<PermissionAttachment> attachments = new ConcurrentSet<PermissionAttachment>();
    @NOBF
    private final Map<String, PermissionAttachmentInfo> permissions = new ConcurrentHashMap<String, PermissionAttachmentInfo>();

    public PermissibleBase(ServerOperator serverOperator) {
        this.b = serverOperator;
        if (serverOperator instanceof Permissible) {
            this.a = (Permissible)serverOperator;
        }
    }

    @Override
    public boolean isOp() {
        return this.b != null && this.b.isOp();
    }

    @Override
    public void setOp(boolean bl) {
        if (this.b == null) {
            throw new ServerException("Cannot change op value as no ServerOperator is set");
        }
        this.b.setOp(bl);
    }

    @Override
    public boolean isPermissionSet(String string) {
        return this.permissions.containsKey(string);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.isPermissionSet(permission.getName());
    }

    @Override
    public boolean hasPermission(String string) {
        if (this.isPermissionSet(string)) {
            return this.permissions.get(string).getValue();
        }
        Permission permission = Server.getInstance().getPluginManager().getPermission(string);
        if (permission != null) {
            String string2 = permission.getDefault();
            return "true".equals(string2) || this.isOp() && "op".equals(string2) || !this.isOp() && "notop".equals(string2);
        }
        return "true".equals("op") || this.isOp() && "op".equals("op") || !this.isOp() && "notop".equals("op");
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.hasPermission(permission.getName());
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.addAttachment(plugin, null, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string) {
        return this.addAttachment(plugin, string, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, Boolean bl) {
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }
        PermissionAttachment permissionAttachment = new PermissionAttachment(plugin, this.a != null ? this.a : this);
        this.attachments.add(permissionAttachment);
        if (string != null && bl != null) {
            permissionAttachment.setPermission(string, (boolean)bl);
        }
        this.recalculatePermissions();
        return permissionAttachment;
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        if (this.attachments.contains(permissionAttachment)) {
            this.attachments.remove(permissionAttachment);
            PermissionRemovedExecutor permissionRemovedExecutor = permissionAttachment.getRemovalCallback();
            if (permissionRemovedExecutor != null) {
                permissionRemovedExecutor.attachmentRemoved(permissionAttachment);
            }
            this.recalculatePermissions();
        }
    }

    @Override
    public void recalculatePermissions() {
        block3: {
            if (Timings.permissibleCalculationTimer != null) {
                Timings.permissibleCalculationTimer.startTiming();
            }
            this.clearPermissions();
            Map<String, Permission> map = Server.getInstance().getPluginManager().getDefaultPermissions(this.isOp());
            Server.getInstance().getPluginManager().subscribeToDefaultPerms(this.isOp(), this.a != null ? this.a : this);
            for (Permission object : map.values()) {
                String string = object.getName();
                this.permissions.put(string, new PermissionAttachmentInfo(this.a != null ? this.a : this, string, null, true));
                Server.getInstance().getPluginManager().subscribeToPermission(string, this.a != null ? this.a : this);
                this.a(object.getChildren(), false, null);
            }
            for (PermissionAttachment permissionAttachment : this.attachments) {
                this.a(permissionAttachment.getPermissions(), false, permissionAttachment);
            }
            if (Timings.permissibleCalculationTimer == null) break block3;
            Timings.permissibleCalculationTimer.stopTiming();
        }
    }

    public void clearPermissions() {
        for (String string : this.permissions.keySet()) {
            Server.getInstance().getPluginManager().unsubscribeFromPermission(string, this.a != null ? this.a : this);
        }
        Server.getInstance().getPluginManager().unsubscribeFromDefaultPerms(false, this.a != null ? this.a : this);
        Server.getInstance().getPluginManager().unsubscribeFromDefaultPerms(true, this.a != null ? this.a : this);
        this.permissions.clear();
    }

    private void a(Map<String, Boolean> map, boolean bl, PermissionAttachment permissionAttachment) {
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            String string = entry.getKey();
            Permission permission = Server.getInstance().getPluginManager().getPermission(string);
            boolean bl2 = entry.getValue();
            boolean bl3 = bl2 ^ bl;
            this.permissions.put(string, new PermissionAttachmentInfo(this.a != null ? this.a : this, string, permissionAttachment, bl3));
            Server.getInstance().getPluginManager().subscribeToPermission(string, this.a != null ? this.a : this);
            if (permission == null) continue;
            this.a(permission.getChildren(), !bl3, permissionAttachment);
        }
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.permissions;
    }

    private static ServerException a(ServerException serverException) {
        return serverException;
    }
}

