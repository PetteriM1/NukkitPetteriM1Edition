/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetDifficultyPacket
extends DataPacket {
    public static final byte NETWORK_ID = 60;
    public int difficulty;

    @Override
    public void decode() {
        this.difficulty = (int)this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.difficulty);
    }

    @Override
    public byte pid() {
        return 60;
    }

    public String toString() {
        return "SetDifficultyPacket(difficulty=" + this.difficulty + ")";
    }
}

