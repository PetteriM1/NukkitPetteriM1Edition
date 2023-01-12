/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class BlockPickRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 34;
    public int x;
    public int y;
    public int z;
    public boolean addUserData;
    public int selectedSlot;

    @Override
    public byte pid() {
        return 34;
    }

    @Override
    public void decode() {
        BlockVector3 blockVector3 = this.getSignedBlockPosition();
        this.x = blockVector3.x;
        this.y = blockVector3.y;
        this.z = blockVector3.z;
        this.addUserData = this.getBoolean();
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "BlockPickRequestPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", addUserData=" + this.addUserData + ", selectedSlot=" + this.selectedSlot + ")";
    }
}

