/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.MainLogger;
import java.util.Map;

public class ConsoleCommandSender
implements CommandSender {
    @NOBF
    private final PermissibleBase perm = new PermissibleBase(this);

    @Override
    public boolean isPermissionSet(String string) {
        return this.perm.isPermissionSet(string);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String string) {
        return this.perm.hasPermission(string);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string) {
        return this.perm.addAttachment(plugin, string);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, Boolean bl) {
        return this.perm.addAttachment(plugin, string, bl);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        this.perm.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Server getServer() {
        return Server.getInstance();
    }

    @Override
    public void sendMessage(String string) {
        string = this.getServer().getLanguage().translateString(string);
        for (String string2 : string.trim().split("\n")) {
            MainLogger.getLogger().info(string2);
        }
    }

    @Override
    public void sendMessage(TextContainer textContainer) {
        this.sendMessage(this.getServer().getLanguage().translate(textContainer));
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean bl) {
    }
}

