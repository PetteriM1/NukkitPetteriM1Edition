package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.DimensionDefinition;
import lombok.ToString;

import java.util.List;

@ToString
public class DimensionDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.DIMENSION_DATA_PACKET;

    public List<DimensionDefinition> definitions;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(definitions.size());
        for (DimensionDefinition definition : definitions) {
            this.putString(definition.getId());
            this.putVarInt(definition.getMaximumHeight());
            this.putVarInt(definition.getMinimumHeight());
            this.putVarInt(definition.getGeneratorType());
            if (protocol >= ProtocolInfo.v1_26_20_26) {
                this.putVarInt(definition.getDimensionType());
                if (protocol >= ProtocolInfo.v1_26_40) {
                    this.putUUID(definition.getPackId());
                }
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
