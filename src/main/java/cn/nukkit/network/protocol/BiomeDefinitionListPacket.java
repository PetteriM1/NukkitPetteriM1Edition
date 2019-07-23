package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import com.google.common.io.ByteStreams;
import lombok.ToString;

import java.io.InputStream;

@ToString(exclude = "tag")
public class BiomeDefinitionListPacket extends DataPacket {

    private static final byte[] TAG;

    static {
        try {
            InputStream inputStream = Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_361.dat");
            //noinspection UnstableApiUsage
            TAG = ByteStreams.toByteArray(inputStream);
        } catch (Exception e) {
            throw new AssertionError("Error whilst loading biome_definitions_361.dat", e);
        }
    }

    public byte[] tag = TAG;

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
        this.put(tag);
    }
}
