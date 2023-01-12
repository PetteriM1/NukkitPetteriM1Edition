/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class TransferPacket
extends DataPacket {
    public static final byte NETWORK_ID = 85;
    public String address;
    public int port = 19132;

    @Override
    public void decode() {
        this.address = this.getString();
        this.port = (short)this.getLShort();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.address);
        this.putLShort(this.port);
    }

    @Override
    public byte pid() {
        return 85;
    }

    public String toString() {
        return "TransferPacket(address=" + this.address + ", port=" + this.port + ")";
    }
}

