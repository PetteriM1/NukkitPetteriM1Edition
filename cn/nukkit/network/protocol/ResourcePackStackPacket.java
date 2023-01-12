/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourcePackStackPacket
extends DataPacket {
    public static final byte NETWORK_ID = 7;
    public boolean mustAccept = false;
    public String gameVersion = "1.19.50";
    public ResourcePack[] behaviourPackStack = new ResourcePack[0];
    public ResourcePack[] resourcePackStack = new ResourcePack[0];
    public boolean isExperimental = false;
    public final List<ExperimentData> experiments = new ObjectArrayList<ExperimentData>();

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.mustAccept);
        this.putUnsignedVarInt(this.behaviourPackStack.length);
        for (ResourcePack resourcePack : this.behaviourPackStack) {
            this.putString(resourcePack.getPackId().toString());
            this.putString(resourcePack.getPackVersion());
            if (this.protocol < 313) continue;
            this.putString("");
        }
        this.putUnsignedVarInt(this.resourcePackStack.length);
        for (ResourcePack resourcePack : this.resourcePackStack) {
            this.putString(resourcePack.getPackId().toString());
            this.putString(resourcePack.getPackVersion());
            if (this.protocol < 313) continue;
            this.putString("");
        }
        if (this.protocol >= 313) {
            if (this.protocol < 419) {
                this.putBoolean(this.isExperimental);
            }
            if (this.protocol >= 388) {
                this.putString(Utils.getVersionByProtocol(this.protocol));
            }
            if (this.protocol >= 419) {
                this.putLInt(this.experiments.size());
                for (ExperimentData experimentData : this.experiments) {
                    this.putString(experimentData.getName());
                    this.putBoolean(experimentData.isEnabled());
                }
                this.putBoolean(false);
            }
        }
    }

    @Override
    public byte pid() {
        return 7;
    }

    public String toString() {
        return "ResourcePackStackPacket(mustAccept=" + this.mustAccept + ", gameVersion=" + this.gameVersion + ", behaviourPackStack=" + Arrays.deepToString(this.behaviourPackStack) + ", resourcePackStack=" + Arrays.deepToString(this.resourcePackStack) + ", isExperimental=" + this.isExperimental + ", experiments=" + this.experiments + ")";
    }

    public static final class ExperimentData {
        private final String a;
        private final boolean b;

        public ExperimentData(String string, boolean bl) {
            this.a = string;
            this.b = bl;
        }

        public String getName() {
            return this.a;
        }

        public boolean isEnabled() {
            return this.b;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof ExperimentData)) {
                return false;
            }
            ExperimentData experimentData = (ExperimentData)object;
            String string = this.getName();
            String string2 = experimentData.getName();
            if (string == null ? string2 != null : !string.equals(string2)) {
                return false;
            }
            return this.isEnabled() == experimentData.isEnabled();
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            String string = this.getName();
            n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
            n2 = n2 * 59 + (this.isEnabled() ? 79 : 97);
            return n2;
        }

        public String toString() {
            return "ResourcePackStackPacket.ExperimentData(name=" + this.getName() + ", enabled=" + this.isEnabled() + ")";
        }
    }
}

