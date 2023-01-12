/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetSpawnPositionPacket
extends DataPacket {
    public static final byte NETWORK_ID = 43;
    public static final int TYPE_PLAYER_SPAWN = 0;
    public static final int TYPE_WORLD_SPAWN = 1;
    public int spawnType;
    public int y;
    public int z;
    public int x;
    public int dimension = 0;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.spawnType);
        this.putBlockVector3(this.x, this.y, this.z);
        if (this.protocol >= 407) {
            this.putVarInt(this.dimension);
            this.putBlockVector3(this.x, this.y, this.z);
        } else {
            this.putBoolean(false);
        }
    }

    @Override
    public byte pid() {
        return 43;
    }

    public String toString() {
        return "SetSpawnPositionPacket(spawnType=" + this.spawnType + ", y=" + this.y + ", z=" + this.z + ", x=" + this.x + ", dimension=" + this.dimension + ")";
    }
}

