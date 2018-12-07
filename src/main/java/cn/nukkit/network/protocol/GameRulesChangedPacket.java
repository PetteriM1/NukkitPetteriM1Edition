package cn.nukkit.network.protocol;

import cn.nukkit.utils.RuleData;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class GameRulesChangedPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.GAME_RULES_CHANGED_PACKET;
    }

    public RuleData[] ruleDatas = new RuleData[0];

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putInt(this.ruleDatas.length);
        for (RuleData rule : this.ruleDatas) {
            this.putRuleData(rule);
        }
    }
}
