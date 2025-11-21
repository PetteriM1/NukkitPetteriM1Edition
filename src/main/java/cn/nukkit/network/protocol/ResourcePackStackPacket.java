package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ExperimentData;
import cn.nukkit.resourcepacks.ResourcePack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.ToString;

import java.util.List;

@ToString
public class ResourcePackStackPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_STACK_PACKET;

    public boolean mustAccept;
    public String gameVersion = "*";
    public ResourcePack[] behaviourPackStack = ResourcePack.EMPTY_ARRAY;
    public ResourcePack[] resourcePackStack = ResourcePack.EMPTY_ARRAY;
    /**
     * Below v1.16.100
     */
    public boolean isExperimental;
    /**
     * v1.16.100 and above
     */
    public final List<ExperimentData> experiments = new ObjectArrayList<>(1);

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);
        this.putUnsignedVarInt(this.behaviourPackStack.length);
        for (ResourcePack entry : this.behaviourPackStack) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            if (protocol >= 313) {
                this.putString("");
            }
        }
        this.putUnsignedVarInt(this.resourcePackStack.length);
        for (ResourcePack entry : this.resourcePackStack) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            if (protocol >= 313) {
                this.putString("");
            }
        }
        if (protocol >= 313) {
            if (protocol < ProtocolInfo.v1_16_100) {
                this.putBoolean(isExperimental);
            }
            if (protocol >= 388) {
                this.putString(this.gameVersion);
                if (protocol >= ProtocolInfo.v1_16_100) {
                    this.putExperiments(this.experiments);
                    if (protocol >= ProtocolInfo.v1_20_80) {
                        this.putBoolean(false); // Has Editor Packs
                    }
                }
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
