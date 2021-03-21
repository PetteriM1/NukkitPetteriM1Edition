package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.utils.Binary;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class SetEntityDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public EntityMetadata metadata;
    public long frame;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.put(Binary.writeMetadata(protocol, this.metadata));
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.putUnsignedVarLong(this.frame);
        }
    }
}
