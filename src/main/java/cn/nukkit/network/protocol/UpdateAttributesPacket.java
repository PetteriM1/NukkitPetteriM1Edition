package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class UpdateAttributesPacket extends DataPacket {

    public Attribute[] entries;
    public long entityId;
    public long frame;

    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;
    }

    public void decode() {
    }

    public void encode() {
        this.reset();

        this.putEntityRuntimeId(this.entityId);

        if (this.entries == null) {
            this.putUnsignedVarInt(0);
        } else {
            this.putUnsignedVarInt(this.entries.length);
            for (Attribute entry : this.entries) {
                this.putLFloat(entry.getMinValue());
                this.putLFloat(entry.getMaxValue());
                this.putLFloat(entry.getValue());
                this.putLFloat(entry.getDefaultValue());
                this.putString(entry.getName());
            }
        }

        if (protocol >= ProtocolInfo.v1_16_100) {
            this.putUnsignedVarInt(this.frame);
        }
    }
}
