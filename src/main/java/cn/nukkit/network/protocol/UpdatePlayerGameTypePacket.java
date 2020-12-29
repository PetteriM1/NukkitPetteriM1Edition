package cn.nukkit.network.protocol;

public class UpdatePlayerGameTypePacket extends DataPacket {

    public GameType gameType;
    public long entityId;

    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_PLAYER_GAME_TYPE_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.gameType = GameType.from(this.getVarInt());
        this.entityId = this.getVarLong();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.gameType.ordinal());
        this.putVarLong(entityId);
    }

    public enum GameType {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SURVIVAL_VIEWER,
        CREATIVE_VIEWER,
        DEFAULT,
        WORLD_DEFAULT;

        private static final GameType[] VALUES = values();

        public static GameType from(int id) {
            return VALUES[id];
        }
    }
}
