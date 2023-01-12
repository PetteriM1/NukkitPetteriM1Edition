/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class AdventureSettingsPacket
extends DataPacket {
    public static final byte NETWORK_ID = 55;
    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;
    public static final int BITFLAG_SECOND_SET = 65536;
    public static final int WORLD_IMMUTABLE = 1;
    public static final int NO_PVM = 2;
    public static final int NO_MVP = 4;
    public static final int SHOW_NAME_TAGS = 16;
    public static final int AUTO_JUMP = 32;
    public static final int ALLOW_FLIGHT = 64;
    public static final int NO_CLIP = 128;
    public static final int WORLD_BUILDER = 256;
    public static final int FLYING = 512;
    public static final int MUTED = 1024;
    public static final int MINE = 65537;
    public static final int DOORS_AND_SWITCHES = 65538;
    public static final int OPEN_CONTAINERS = 65540;
    public static final int ATTACK_PLAYERS = 65544;
    public static final int ATTACK_MOBS = 65552;
    public static final int OPERATOR = 65568;
    public static final int TELEPORT = 65664;
    public static final int BUILD = 65792;
    public static final int DEFAULT_LEVEL_PERMISSIONS = 66048;
    public long flags = 0L;
    public long flags2 = 0L;
    public long customFlags;
    public long commandPermission = 0L;
    public long playerPermission = 1L;
    public long entityUniqueId;

    @Override
    public void decode() {
        this.flags = this.getUnsignedVarInt();
        this.commandPermission = this.getUnsignedVarInt();
        this.flags2 = this.getUnsignedVarInt();
        this.playerPermission = this.getUnsignedVarInt();
        this.customFlags = this.getUnsignedVarInt();
        this.entityUniqueId = this.getLLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.flags);
        this.putUnsignedVarInt(this.commandPermission);
        this.putUnsignedVarInt(this.flags2);
        this.putUnsignedVarInt(this.playerPermission);
        this.putUnsignedVarInt(this.customFlags);
        this.putLLong(this.entityUniqueId);
    }

    public boolean getFlag(int n) {
        if ((n & 0x10000) != 0) {
            return (this.flags2 & (long)n) != 0L;
        }
        return (this.flags & (long)n) != 0L;
    }

    public void setFlag(int n, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = (n & 0x10000) != 0;
        if (bl) {
            if (bl2) {
                this.flags2 |= (long)n;
            } else {
                this.flags |= (long)n;
            }
        } else if (bl2) {
            this.flags2 &= (long)(~n);
        } else {
            this.flags &= (long)(~n);
        }
    }

    @Override
    public byte pid() {
        return 55;
    }

    public String toString() {
        return "AdventureSettingsPacket(flags=" + this.flags + ", flags2=" + this.flags2 + ", customFlags=" + this.customFlags + ", commandPermission=" + this.commandPermission + ", playerPermission=" + this.playerPermission + ", entityUniqueId=" + this.entityUniqueId + ")";
    }
}

