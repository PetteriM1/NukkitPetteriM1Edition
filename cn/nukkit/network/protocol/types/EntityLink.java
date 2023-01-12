/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

public class EntityLink {
    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDER = 1;
    public static final byte TYPE_PASSENGER = 2;
    public long fromEntityUniquieId;
    public long toEntityUniquieId;
    public byte type;
    public boolean immediate;
    public boolean riderInitiated;

    public EntityLink(long l, long l2, byte by, boolean bl, boolean bl2) {
        this.fromEntityUniquieId = l;
        this.toEntityUniquieId = l2;
        this.type = by;
        this.immediate = bl;
        this.riderInitiated = bl2;
    }
}

