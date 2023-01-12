/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;

public class SpawnParticleEffectPacket
extends DataPacket {
    public static final byte NETWORK_ID = 118;
    public int dimensionId;
    public long uniqueEntityId = -1L;
    public Vector3f position;
    public String identifier;

    @Override
    public byte pid() {
        return 118;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.dimensionId);
        if (this.protocol >= 332) {
            this.putEntityUniqueId(this.uniqueEntityId);
        }
        this.putVector3f(this.position);
        this.putString(this.identifier);
        if (this.protocol >= 503) {
            this.putBoolean(false);
        }
    }

    public String toString() {
        return "SpawnParticleEffectPacket(dimensionId=" + this.dimensionId + ", uniqueEntityId=" + this.uniqueEntityId + ", position=" + this.position + ", identifier=" + this.identifier + ")";
    }
}

