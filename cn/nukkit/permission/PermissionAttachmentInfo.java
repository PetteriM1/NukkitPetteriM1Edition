/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.permission.Permissible;
import cn.nukkit.permission.PermissionAttachment;

public class PermissionAttachmentInfo {
    private final Permissible b;
    private final String a;
    private final PermissionAttachment c;
    private boolean d;

    public PermissionAttachmentInfo(Permissible permissible, String string, PermissionAttachment permissionAttachment, boolean bl) {
        if (string == null) {
            throw new IllegalStateException("Permission may not be null");
        }
        this.b = permissible;
        this.a = string;
        this.c = permissionAttachment;
        this.d = bl;
    }

    public Permissible getPermissible() {
        return this.b;
    }

    public String getPermission() {
        return this.a;
    }

    public PermissionAttachment getAttachment() {
        return this.c;
    }

    public boolean getValue() {
        return this.d;
    }

    public void setValue(boolean bl) {
        this.d = bl;
    }
}

