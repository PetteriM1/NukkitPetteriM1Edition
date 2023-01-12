/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.a;
import cn.nukkit.network.protocol.types.DimensionDefinition;
import java.util.List;

public class DimensionDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = -76;
    private static final List<DimensionDefinition> e = new a();
    public List<DimensionDefinition> definitions = e;

    @Override
    public byte pid() {
        return -76;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.definitions.size());
        for (DimensionDefinition dimensionDefinition : this.definitions) {
            this.putString(dimensionDefinition.getId());
            this.putVarInt(dimensionDefinition.getMaximumHeight());
            this.putVarInt(dimensionDefinition.getMinimumHeight());
            this.putVarInt(dimensionDefinition.getGeneratorType());
        }
    }

    public String toString() {
        return "DimensionDataPacket(definitions=" + this.definitions + ")";
    }
}

