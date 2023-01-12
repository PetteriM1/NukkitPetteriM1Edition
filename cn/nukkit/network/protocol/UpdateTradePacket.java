/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class UpdateTradePacket
extends DataPacket {
    public static final byte NETWORK_ID = 80;
    public byte windowId;
    public byte windowType = (byte)15;
    public int unknownVarInt1;
    public int unknownVarInt2;
    public int unknownVarInt3;
    public int tradeTier;
    public long trader;
    public long player;
    public String displayName;
    public boolean screen2;
    public boolean isWilling;
    public byte[] offers;

    @Override
    public byte pid() {
        return 80;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.windowId);
        this.putByte(this.windowType);
        this.putVarInt(this.unknownVarInt1);
        if (this.protocol < 354) {
            this.putVarInt(this.unknownVarInt2);
            if (this.protocol >= 313) {
                this.putVarInt(this.unknownVarInt3);
            }
            this.putBoolean(this.isWilling);
        } else {
            this.putVarInt(this.tradeTier);
        }
        this.putEntityUniqueId(this.trader);
        this.putEntityUniqueId(this.player);
        this.putString(this.displayName);
        if (this.protocol >= 354) {
            this.putBoolean(this.screen2);
            this.putBoolean(this.isWilling);
        }
        this.put(this.offers);
    }

    public String toString() {
        return "UpdateTradePacket(windowId=" + this.windowId + ", windowType=" + this.windowType + ", unknownVarInt1=" + this.unknownVarInt1 + ", unknownVarInt2=" + this.unknownVarInt2 + ", unknownVarInt3=" + this.unknownVarInt3 + ", tradeTier=" + this.tradeTier + ", trader=" + this.trader + ", player=" + this.player + ", displayName=" + this.displayName + ", screen2=" + this.screen2 + ", isWilling=" + this.isWilling + ", offers=" + Arrays.toString(this.offers) + ")";
    }
}

