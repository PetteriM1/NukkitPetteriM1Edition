package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class RemoveEntityPacket extends DataPacket {

    public long eid;

    @Override
    public byte pid() {
        return ProtocolInfo.REMOVE_ENTITY_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.eid);
    }
}
