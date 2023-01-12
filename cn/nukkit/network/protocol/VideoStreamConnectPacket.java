/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class VideoStreamConnectPacket
extends DataPacket {
    public static final byte NETWORK_ID = 126;
    public static final byte ACTION_OPEN = 0;
    public static final byte ACTION_CLOSE = 1;
    public String address;
    public float screenshotFrequency;
    public byte action;

    @Override
    public byte pid() {
        return 126;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.address);
        this.putLFloat(this.screenshotFrequency);
        this.putByte(this.action);
    }

    public String toString() {
        return "VideoStreamConnectPacket(address=" + this.address + ", screenshotFrequency=" + this.screenshotFrequency + ", action=" + this.action + ")";
    }
}

