package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ShowProfilePacket extends DataPacket {

    public String xuid;

    @Override
    public byte pid() {
        return ProtocolInfo.SHOW_PROFILE_PACKET;
    }

    @Override
    public void decode() {
        this.xuid = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.xuid);
    }
}
