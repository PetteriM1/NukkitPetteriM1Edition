package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class SetDifficultyPacket extends DataPacket {

    public int difficulty;

    @Override
    public void decodePayload(int protocolId) {
        this.difficulty = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putUnsignedVarInt(this.difficulty);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_DIFFICULTY_PACKET;
    }
}
