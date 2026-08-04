package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class VoxelShapesPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.__INTERNAL__VOXEL_SHAPES_PACKET;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(0); // empty shapes array
        this.putUnsignedVarInt(0); // empty names map
        if (protocol >= ProtocolInfo.v1_26_0) {
            this.putLShort(0); // custom shapes count
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
