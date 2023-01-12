/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class LevelSoundEventPacketV1
extends LevelSoundEventPacket {
    public static final byte NETWORK_ID = 24;
    public int sound;
    public float x;
    public float y;
    public float z;
    public int extraData = -1;
    public int pitch = 1;
    public boolean isBabyMob;
    public boolean isGlobal;

    @Override
    public void decode() {
        this.sound = this.getByte();
        Vector3f vector3f = this.getVector3f();
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        this.extraData = this.getVarInt();
        this.pitch = this.getVarInt();
        this.isBabyMob = this.getBoolean();
        this.isGlobal = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.sound);
        this.putVector3f(this.x, this.y, this.z);
        this.putVarInt(this.extraData);
        this.putVarInt(this.pitch);
        this.putBoolean(this.isBabyMob);
        this.putBoolean(this.isGlobal);
    }

    @Override
    public byte pid() {
        return 24;
    }

    @Override
    public String toString() {
        return "LevelSoundEventPacketV1(sound=" + this.sound + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", extraData=" + this.extraData + ", pitch=" + this.pitch + ", isBabyMob=" + this.isBabyMob + ", isGlobal=" + this.isGlobal + ")";
    }
}

