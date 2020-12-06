package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class SetPlayerGameTypePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET;
    }

    public int gamemode;

    @Override
    public void decode(int protocolId) {
        this.gamemode = this.getVarInt();
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(this.gamemode);
    }
}
