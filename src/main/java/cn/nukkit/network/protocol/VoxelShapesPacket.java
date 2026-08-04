package cn.nukkit.network.protocol;

import com.google.common.io.ByteStreams;
import lombok.ToString;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.Deflater;

@ToString
public class VoxelShapesPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.__INTERNAL__VOXEL_SHAPES_PACKET;

    private static final BatchPacket CACHED_PACKET_2192;
    private static final BatchPacket CACHED_PACKET_974;

    private byte[] bin;

    static {
        VoxelShapesPacket pk = new VoxelShapesPacket();
        pk.protocol = ProtocolInfo.v1_26_20_26;
        try {
            pk.bin = ByteStreams.toByteArray(Objects.requireNonNull(VoxelShapesPacket.class.getClassLoader().getResourceAsStream("voxel_shapes_2192.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pk.tryEncode();
        CACHED_PACKET_2192 = pk.compress(Deflater.BEST_COMPRESSION);

        pk = new VoxelShapesPacket();
        pk.protocol = ProtocolInfo.v1_26_20_26;
        pk.tryEncode();
        CACHED_PACKET_974 = pk.compress(Deflater.BEST_COMPRESSION);
    }

    public static BatchPacket getCachedPacket(int protocol) {
        if (protocol < ProtocolInfo.v1_26_50_27) {
            if (protocol < ProtocolInfo.v1_26_0) {
                throw new IllegalStateException("Should not be sent");
            }
            return CACHED_PACKET_974;
        }
        return CACHED_PACKET_2192;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        if (this.bin != null) {
            this.put(this.bin);
            return;
        }

        this.putUnsignedVarInt(0); // empty shapes array
        this.putUnsignedVarInt(0); // empty names map
        if (protocol >= ProtocolInfo.v1_26_0) {
            this.putLShort(0); // custom shapes count
        }
    }
}
