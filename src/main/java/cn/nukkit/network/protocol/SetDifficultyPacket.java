package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class SetDifficultyPacket extends DataPacket {

    public int difficulty;

    @Override
    public void decode() {
        this.difficulty = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.difficulty);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_DIFFICULTY_PACKET;
    }
}
