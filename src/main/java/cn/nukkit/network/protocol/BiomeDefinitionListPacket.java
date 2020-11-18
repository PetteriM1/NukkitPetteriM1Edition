package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import com.google.common.io.ByteStreams;
import lombok.ToString;

@ToString(exclude = "tag")
public class BiomeDefinitionListPacket extends DataPacket {

    private static final byte[] TAG361;
    private static final byte[] TAG419;

    static {
        try {
            TAG361 = ByteStreams.toByteArray(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_361.dat"));
            TAG419 = ByteStreams.toByteArray(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_419.dat"));
        } catch (Exception e) {
            throw new AssertionError("Error whilst loading biome definitions", e);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.put(protocol >= ProtocolInfo.v1_16_100 ? TAG419 : TAG361);
    }
}
