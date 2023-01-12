/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class UpdateAttributesPacket
extends DataPacket {
    public static final byte NETWORK_ID = 29;
    public Attribute[] entries;
    public long entityId;
    public long frame;

    @Override
    public byte pid() {
        return 29;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityId);
        if (this.entries == null) {
            this.putUnsignedVarInt(0L);
        } else {
            this.putUnsignedVarInt(this.entries.length);
            for (Attribute attribute : this.entries) {
                this.putLFloat(attribute.getMinValue());
                this.putLFloat(attribute.getMaxValue());
                this.putLFloat(attribute.getValue());
                this.putLFloat(attribute.getDefaultValue());
                this.putString(attribute.getName());
                if (this.protocol < 544) continue;
                this.putUnsignedVarInt(0L);
            }
        }
        if (this.protocol >= 419) {
            this.putUnsignedVarInt(this.frame);
        }
    }

    public String toString() {
        return "UpdateAttributesPacket(entries=" + Arrays.deepToString(this.entries) + ", entityId=" + this.entityId + ", frame=" + this.frame + ")";
    }
}

