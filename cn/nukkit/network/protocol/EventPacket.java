/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class EventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 65;
    public long eid;
    public int unknown1;
    public byte unknown2;
    public static final int TYPE_ACHIEVEMENT_AWARDED = 0;
    public static final int TYPE_ENTITY_INTERACT = 1;
    public static final int TYPE_PORTAL_BUILT = 2;
    public static final int TYPE_PORTAL_USED = 3;
    public static final int TYPE_MOB_KILLED = 4;
    public static final int TYPE_CAULDRON_USED = 5;
    public static final int TYPE_PLAYER_DEATH = 6;
    public static final int TYPE_BOSS_KILLED = 7;
    public static final int TYPE_AGENT_COMMAND = 8;
    public static final int TYPE_AGENT_CREATED = 9;
    public static final int TYPE_PATTERN_REMOVED = 10;
    public static final int TYPE_COMMANED_EXECUTED = 11;
    public static final int TYPE_FISH_BUCKETED = 12;
    public static final int TYPE_MOB_BORN = 13;
    public static final int TYPE_PET_DIED = 14;
    public static final int TYPE_CAULDRON_BLOCK_USED = 15;
    public static final int TYPE_COMPOSTER_BLOCK_USED = 16;
    public static final int TYPE_BELL_BLOCK_USED = 17;

    @Override
    public byte pid() {
        return 65;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.eid);
        this.putVarInt(this.unknown1);
        this.putByte(this.unknown2);
    }

    public String toString() {
        return "EventPacket(eid=" + this.eid + ", unknown1=" + this.unknown1 + ", unknown2=" + this.unknown2 + ")";
    }
}

