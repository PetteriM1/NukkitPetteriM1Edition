package cn.nukkit.network.protocol;

import cn.nukkit.resourcepacks.ResourcePack;
import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
public class ResourcePacksInfoPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;

    public boolean mustAccept;
    public boolean scripting;
    /**
     * Note: Unused since 1.21.30
     */
    public boolean forceServerPacks;
    public boolean hasAddonPacks;
    /**
     * Since 1.21.90
     */
    public boolean forceDisableVibrantVisuals;
    public UUID worldTemplateId = new UUID(0, 0);
    public String worldTemplateVersion = "";
    public ResourcePack[] behaviourPackEntries = ResourcePack.EMPTY_ARRAY;
    public ResourcePack[] resourcePackEntries = ResourcePack.EMPTY_ARRAY;

    @Value
    private static class CDNEntry {
        String packId;
        String remoteUrl;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);

        if (protocol >= ProtocolInfo.v1_9_0) {
            if (protocol >= ProtocolInfo.v1_20_70) {
                this.putBoolean(this.hasAddonPacks);
            }

            this.putBoolean(this.scripting);

            if (protocol >= ProtocolInfo.v1_17_10 && protocol < ProtocolInfo.v1_21_30) {
                this.putBoolean(this.forceServerPacks);
            }

            if (protocol >= ProtocolInfo.v1_21_50_28) {
                if (protocol >= ProtocolInfo.v1_21_90) {
                    this.putBoolean(this.forceDisableVibrantVisuals);
                }

                this.putUUID(this.worldTemplateId);
                this.putString(this.worldTemplateVersion);
            }
        }

        if (protocol < ProtocolInfo.v1_21_30) {
            this.encodeBehaviourPacks(this.behaviourPackEntries);
        }

        this.encodeResourcePacks(this.resourcePackEntries);

        if (protocol >= ProtocolInfo.v1_20_30 && protocol < ProtocolInfo.v1_21_40) {
            List<CDNEntry> CDNEntries = new ArrayList<>(0);

            for (ResourcePack behavior : behaviourPackEntries) {
                if (!behavior.getCDNUrl().isEmpty()) {
                    CDNEntries.add(new CDNEntry(behavior.getPackId().toString(), behavior.getCDNUrl()));
                }
            }

            for (ResourcePack resource : resourcePackEntries) {
                if (!resource.getCDNUrl().isEmpty()) {
                    CDNEntries.add(new CDNEntry(resource.getPackId().toString(), resource.getCDNUrl()));
                }
            }

            this.putUnsignedVarInt(CDNEntries.size());
            for (CDNEntry entry : CDNEntries) {
                this.putString(entry.getPackId());
                this.putString(entry.getRemoteUrl());
            }
        }
    }

    private void encodeBehaviourPacks(ResourcePack[] packs) {
        this.putLShort(packs.length);
        for (ResourcePack entry : packs) {
            this.putString(entry.getPackId().toString());
            this.putString(entry.getPackVersion());
            this.putLLong(entry.getPackSize());
            this.putString(entry.getEncryptionKey());
            this.putString(entry.getSubPackName());
            if (protocol > ProtocolInfo.v1_5_0) {
                this.putString(!entry.getEncryptionKey().isEmpty() ? entry.getPackId().toString() : ""); // content identity
                if (protocol >= ProtocolInfo.v1_9_0) {
                    this.putBoolean(entry.usesScripting());
                    if (protocol >= ProtocolInfo.v1_21_20) {
                        this.putBoolean(entry.isAddonPack());
                    }
                }
            }
        }
    }

    private void encodeResourcePacks(ResourcePack[] packs) {
        if (protocol >= ProtocolInfo.v1_26_40) {
            this.putUnsignedVarInt(packs.length);
        } else {
            this.putLShort(packs.length);
        }

        for (ResourcePack entry : packs) {
            if (protocol >= ProtocolInfo.v1_21_50_28) {
                this.putUUID(entry.getPackId());
            } else {
                this.putString(entry.getPackId().toString());
            }
            this.putString(entry.getPackVersion());
            this.putLLong(entry.getPackSize());
            this.putString(entry.getEncryptionKey());
            this.putString(entry.getSubPackName());
            if (protocol > ProtocolInfo.v1_5_0) {
                this.putString(!entry.getEncryptionKey().isEmpty() ? entry.getPackId().toString() : ""); // content identity
                if (protocol >= ProtocolInfo.v1_9_0) {
                    this.putBoolean(entry.usesScripting());
                    if (protocol >= ProtocolInfo.v1_16_200) {
                        if (protocol >= ProtocolInfo.v1_21_20) {
                            this.putBoolean(entry.isAddonPack());
                        }
                        this.putBoolean(entry.isRaytracingCapable());
                        if (protocol >= ProtocolInfo.v1_21_40) {
                            this.putString(entry.getCDNUrl());
                        }
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
