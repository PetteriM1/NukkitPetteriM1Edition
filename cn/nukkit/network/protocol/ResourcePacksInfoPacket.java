/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.resourcepacks.ResourcePack;
import java.util.Arrays;

public class ResourcePacksInfoPacket
extends DataPacket {
    public static final byte NETWORK_ID = 6;
    public boolean mustAccept;
    public boolean scripting;
    public boolean forceServerPacks;
    public ResourcePack[] behaviourPackEntries = new ResourcePack[0];
    public ResourcePack[] resourcePackEntries = new ResourcePack[0];

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);
        if (this.protocol >= 332) {
            this.putBoolean(this.scripting);
            if (this.protocol >= 448) {
                this.putBoolean(this.forceServerPacks);
            }
        }
        this.b(this.behaviourPackEntries);
        this.a(this.resourcePackEntries);
    }

    private void b(ResourcePack[] resourcePackArray) {
        this.putLShort(resourcePackArray.length);
        for (ResourcePack resourcePack : resourcePackArray) {
            this.putString(resourcePack.getPackId().toString());
            this.putString(resourcePack.getPackVersion());
            this.putLLong(resourcePack.getPackSize());
            this.putString("");
            this.putString("");
            if (this.protocol <= 274) continue;
            this.putString("");
            if (this.protocol < 332) continue;
            this.putBoolean(false);
        }
    }

    private void a(ResourcePack[] resourcePackArray) {
        this.putLShort(resourcePackArray.length);
        for (ResourcePack resourcePack : resourcePackArray) {
            this.putString(resourcePack.getPackId().toString());
            this.putString(resourcePack.getPackVersion());
            this.putLLong(resourcePack.getPackSize());
            this.putString(resourcePack.getEncryptionKey());
            this.putString("");
            if (this.protocol <= 274) continue;
            this.putString(!resourcePack.getEncryptionKey().equals("") ? resourcePack.getPackId().toString() : "");
            if (this.protocol < 332) continue;
            this.putBoolean(false);
            if (this.protocol < 422) continue;
            this.putBoolean(false);
        }
    }

    @Override
    public byte pid() {
        return 6;
    }

    public String toString() {
        return "ResourcePacksInfoPacket(mustAccept=" + this.mustAccept + ", scripting=" + this.scripting + ", forceServerPacks=" + this.forceServerPacks + ", behaviourPackEntries=" + Arrays.deepToString(this.behaviourPackEntries) + ", resourcePackEntries=" + Arrays.deepToString(this.resourcePackEntries) + ")";
    }
}

