package cn.nukkit.network.protocol;

import cn.nukkit.resourcepacks.ResourcePack;
import lombok.ToString;

@ToString
public class ResourcePacksInfoPacket extends DataPacket {

    public boolean mustAccept;
    public boolean scripting;
    public ResourcePack[] behaviourPackEntries = new ResourcePack[0];
    public ResourcePack[] resourcePackEntries = new ResourcePack[0];

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putBoolean(this.mustAccept);
        if (protocolId >= 332) {
            this.putBoolean(this.scripting);
        }

        encodePacks(this.resourcePackEntries, protocolId);
        encodePacks(this.behaviourPackEntries, protocolId);
    }

    private void encodePacks(ResourcePack[] packs, int protocolId) {
        this.putLShort(packs.length);
        for (ResourcePack entry : packs) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            this.putLLong(entry.getPackSize());
            this.putString(""); // encryption key
            this.putString(""); // sub-pack name
            if (protocolId > 274) {
                this.putString(""); // content identity
                if (protocolId >= 332) {
                    this.putBoolean(false); // scripting
                }
            }
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;
    }
}