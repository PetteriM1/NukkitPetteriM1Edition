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
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        putGameRules(gameRules);
    }
}
