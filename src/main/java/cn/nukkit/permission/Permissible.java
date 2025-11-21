package cn.nukkit.permission;

import cn.nukkit.plugin.Plugin;

import java.util.Map;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface Permissible extends ServerOperator {

    PermissionAttachment addAttachment(Plugin plugin);

    PermissionAttachment addAttachment(Plugin plugin, String name);

    PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value);

    Map<String, PermissionAttachmentInfo> getEffectivePermissions();

    boolean hasPermission(String name);

    boolean hasPermission(Permission permission);

    boolean isPermissionSet(String name);

    boolean isPermissionSet(Permission permission);

    void recalculatePermissions();

    void removeAttachment(PermissionAttachment attachment);
}
