package cn.nukkit.level.format.generic.serializer;

import cn.nukkit.level.DimensionData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NetworkChunkData {

    private final int protocol;
    private int chunkSections;
    private final boolean antiXray;
    private final DimensionData dimensionData;
}
