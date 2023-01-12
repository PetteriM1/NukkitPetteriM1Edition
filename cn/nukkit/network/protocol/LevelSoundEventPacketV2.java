/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class LevelSoundEventPacketV2
extends LevelSoundEventPacket {
    public static final byte NETWORK_ID = 120;
    public int sound;
    public float x;
    public float y;
    public float z;
    public int extraData = -1;
    public String entityIdentifier;
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
        this.entityIdentifier = this.getString();
        this.isBabyMob = this.getBoolean();
        this.isGlobal = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.sound);
        this.putVector3f(this.x, this.y, this.z);
        this.putVarInt(this.extraData);
        this.putString(this.entityIdentifier);
        this.putBoolean(this.isBabyMob);
        this.putBoolean(this.isGlobal);
    }

    @Override
    public byte pid() {
        return 120;
    }

    @Override
    public String toString() {
        return "LevelSoundEventPacketV2(sound=" + this.sound + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", extraData=" + this.extraData + ", entityIdentifier=" + this.entityIdentifier + ", isBabyMob=" + this.isBabyMob + ", isGlobal=" + this.isGlobal + ")";
    }
}

