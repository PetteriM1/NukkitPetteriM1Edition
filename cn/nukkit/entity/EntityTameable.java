/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityTameable {
    public static final String NAMED_TAG_OWNER = "Owner";
    public static final String NAMED_TAG_OWNER_UUID = "OwnerUUID";
    public static final String NAMED_TAG_SITTING = "Sitting";

    public Player getOwner();

    public boolean hasOwner();

    public void setOwner(Player var1);

    public String getOwnerUUID();

    public void setOwnerUUID(String var1);

    public boolean isSitting();

    public void setSitting(boolean var1);
}

