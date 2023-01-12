/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class PlayerArmorDamagePacket
extends DataPacket {
    public static final byte NETWORK_ID = -107;
    public final Set<PlayerArmorDamageFlag> flags = EnumSet.noneOf(PlayerArmorDamageFlag.class);
    public final int[] damage = new int[4];

    @Override
    public byte pid() {
        return -107;
    }

    @Override
    public void decode() {
        int n = this.getByte();
        for (int k = 0; k < 4; ++k) {
            if ((n & 1 << k) == 0) continue;
            this.flags.add(PlayerArmorDamageFlag.values()[k]);
            this.damage[k] = this.getVarInt();
        }
    }

    @Override
    public void encode() {
        this.reset();
        int n = 0;
        for (PlayerArmorDamageFlag playerArmorDamageFlag : this.flags) {
            n |= 1 << playerArmorDamageFlag.ordinal();
        }
        this.putByte((byte)n);
        for (PlayerArmorDamageFlag playerArmorDamageFlag : this.flags) {
            this.putVarInt(this.damage[playerArmorDamageFlag.ordinal()]);
        }
    }

    public String toString() {
        return "PlayerArmorDamagePacket(flags=" + this.flags + ", damage=" + Arrays.toString(this.damage) + ")";
    }

    public static enum PlayerArmorDamageFlag {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS;

    }
}

