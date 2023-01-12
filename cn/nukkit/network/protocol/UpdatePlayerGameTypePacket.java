/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class UpdatePlayerGameTypePacket
extends DataPacket {
    public static final byte NETWORK_ID = -105;
    public GameType gameType;
    public long entityId;

    @Override
    public byte pid() {
        return -105;
    }

    @Override
    public void decode() {
        this.gameType = GameType.from(this.getVarInt());
        this.entityId = this.getVarLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.gameType.ordinal());
        this.putVarLong(this.entityId);
    }

    public String toString() {
        return "UpdatePlayerGameTypePacket(gameType=" + (Object)((Object)this.gameType) + ", entityId=" + this.entityId + ")";
    }

    public static enum GameType {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SURVIVAL_VIEWER,
        CREATIVE_VIEWER,
        DEFAULT,
        WORLD_DEFAULT;

        private static final GameType[] a;

        public static GameType from(int n) {
            return a[n];
        }

        static {
            a = GameType.values();
        }
    }
}

