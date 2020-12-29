package cn.nukkit.network.protocol;

import cn.nukkit.level.GameRules;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class GameRulesChangedPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.GAME_RULES_CHANGED_PACKET;
    }

    public GameRules gameRules;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        putGameRules(gameRules);
    }
}
