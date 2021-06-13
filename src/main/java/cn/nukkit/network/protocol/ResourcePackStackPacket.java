package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.utils.Utils;
import lombok.ToString;

@ToString
public class ResourcePackStackPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_STACK_PACKET;

    public boolean mustAccept = false;
    public ResourcePack[] behaviourPackStack = new ResourcePack[0];
    public ResourcePack[] resourcePackStack = new ResourcePack[0];
    public boolean isExperimental = false;

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
                if (Server.getInstance().enableCustomItems) {
                    this.putLInt(2); // Experiments length
                    this.putString("data_driven_items");
                    this.putBoolean(true);
                    this.putString("experimental_custom_ui");
                    this.putBoolean(true);

                    this.putBoolean(true); // Were experiments previously toggled
                }else {
                    this.putLInt(0); // Experiments length
                    this.putBoolean(false); // Were experiments previously toggled
                }
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
