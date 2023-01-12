/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.permission.ServerOperator;
import cn.nukkit.plugin.Plugin;
import java.util.Map;

public interface Permissible
extends ServerOperator {
    public boolean isPermissionSet(String var1);

    public boolean isPermissionSet(Permission var1);

    public boolean hasPermission(String var1);

    public boolean hasPermission(Permission var1);

    public PermissionAttachment addAttachment(Plugin var1);

    public PermissionAttachment addAttachment(Plugin var1, String var2);

    public PermissionAttachment addAttachment(Plugin var1, String var2, Boolean var3);

    public void removeAttachment(PermissionAttachment var1);

    public void recalculatePermissions();

    public Map<String, PermissionAttachmentInfo> getEffectivePermissions();
}

