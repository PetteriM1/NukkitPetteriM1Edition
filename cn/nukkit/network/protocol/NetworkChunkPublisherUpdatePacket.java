/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class NetworkChunkPublisherUpdatePacket
extends DataPacket {
    public static final byte NETWORK_ID = 121;
    public BlockVector3 position;
    public int radius;

    @Override
    public byte pid() {
        return 121;
    }

    @Override
    public void decode() {
        this.position = this.getSignedBlockPosition();
        this.radius = (int)this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putSignedBlockPosition(this.position);
        this.putUnsignedVarInt(this.radius);
        if (this.protocol >= 544) {
            this.putInt(0);
        }
    }

    public String toString() {
        return "NetworkChunkPublisherUpdatePacket(position=" + this.position + ", radius=" + this.radius + ")";
    }
}

