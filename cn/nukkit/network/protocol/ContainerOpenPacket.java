/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class ContainerOpenPacket
extends DataPacket {
    public static final byte NETWORK_ID = 46;
    public int windowId;
    public int type;
    public int x;
    public int y;
    public int z;
    public long entityId = -1L;

    @Override
    public byte pid() {
        return 46;
    }

    @Override
    public void decode() {
        this.windowId = this.getByte();
        this.type = this.getByte();
        BlockVector3 blockVector3 = this.getBlockVector3();
        this.x = blockVector3.x;
        this.y = blockVector3.y;
        this.z = blockVector3.z;
        this.entityId = this.getEntityUniqueId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.windowId);
        this.putByte((byte)this.type);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putEntityUniqueId(this.entityId);
    }

    public String toString() {
        return "ContainerOpenPacket(windowId=" + this.windowId + ", type=" + this.type + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", entityId=" + this.entityId + ")";
    }
}

