package cn.nukkit.permission;

import cn.nukkit.plugin.Plugin;

import java.util.Map;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface Permissible extends ServerOperator {

    Map<String, PermissionAttachmentInfo> getEffectivePermissions();

    PermissionAttachment addAttachment(Plugin plugin);

    PermissionAttachment addAttachment(Plugin plugin, String name);

    PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value);

    boolean hasPermission(Permission permission);

    boolean hasPermission(String name);

    boolean isPermissionSet(Permission permission);

    boolean isPermissionSet(String name);

    void recalculatePermissions();

    void removeAttachment(PermissionAttachment attachment);
}
