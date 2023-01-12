/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class UpdateSoftEnumPacket
extends DataPacket {
    public static final byte NETWORK_ID = 114;
    public final String[] values = new String[0];
    public String name = "";
    public Type type = Type.SET;

    @Override
    public byte pid() {
        return 114;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.name);
        this.putUnsignedVarInt(this.values.length);
        for (String string : this.values) {
            this.putString(string);
        }
        this.putByte((byte)this.type.ordinal());
    }

    public String toString() {
        return "UpdateSoftEnumPacket(values=" + Arrays.deepToString(this.values) + ", name=" + this.name + ", type=" + (Object)((Object)this.type) + ")";
    }

    public static enum Type {
        ADD,
        REMOVE,
        SET;

    }
}

