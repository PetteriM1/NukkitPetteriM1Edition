/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class PlaySoundPacket
extends DataPacket {
    public static final byte NETWORK_ID = 86;
    public String name;
    public int x;
    public int y;
    public int z;
    public float volume;
    public float pitch;

    @Override
    public byte pid() {
        return 86;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.name);
        this.putBlockVector3(this.x << 3, this.y << 3, this.z << 3);
        this.putLFloat(this.volume);
        this.putLFloat(this.pitch);
    }

    public String toString() {
        return "PlaySoundPacket(name=" + this.name + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", volume=" + this.volume + ", pitch=" + this.pitch + ")";
    }
}

