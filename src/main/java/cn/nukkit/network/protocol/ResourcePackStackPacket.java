package cn.nukkit.network.protocol;

import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@ToString
public class ResourcePackStackPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_STACK_PACKET;

    public boolean mustAccept = false;
    public ResourcePack[] behaviourPackStack = new ResourcePack[0];
    public ResourcePack[] resourcePackStack = new ResourcePack[0];
    /**
     * Below v1.16.100
     */
    public boolean isExperimental = false;
    /**
     * v1.16.100 and above
     */
    public final List<ExperimentData> experiments = new ObjectArrayList<>();

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);
        this.putUnsignedVarInt(this.behaviourPackStack.length);
        for (ResourcePack entry : this.behaviourPackStack) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            if (this.protocol >= 313) {
                this.putString("");
            }
        }
        this.putUnsignedVarInt(this.resourcePackStack.length);
        for (ResourcePack entry : this.resourcePackStack) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            if (this.protocol >= 313) {
                this.putString("");
            }
        }
        if (this.protocol >= 313) {
            if (protocol < ProtocolInfo.v1_16_100) {
                this.putBoolean(isExperimental);
            }
            if (protocol >= 388) {
                this.putString(Utils.getVersionByProtocol(protocol));
            }
            if (protocol >= ProtocolInfo.v1_16_100) {
                this.putLInt(this.experiments.size());
                for (ExperimentData experimentData : this.experiments) {
                    this.putString(experimentData.getName());
                    this.putBoolean(experimentData.isEnabled());
                }
                this.putBoolean(false); // Were experiments previously toggled
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Value
    public static class ExperimentData {
        String name;
        boolean enabled;
    }
}
