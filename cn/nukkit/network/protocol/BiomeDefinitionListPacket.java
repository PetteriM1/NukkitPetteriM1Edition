/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.network.protocol.DataPacket;
import com.google.common.io.ByteStreams;

public class BiomeDefinitionListPacket
extends DataPacket {
    public static final byte NETWORK_ID = 122;
    private static final byte[] e;
    private static final byte[] g;
    private static final byte[] f;

    @Override
    public byte pid() {
        return 122;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        if (this.protocol >= 553) {
            this.put(e);
        } else if (this.protocol >= 486) {
            this.put(g);
        } else {
            this.put(f);
        }
    }

    public String toString() {
        return "BiomeDefinitionListPacket()";
    }

    static {
        try {
            f = ByteStreams.toByteArray(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_361.dat"));
            g = ByteStreams.toByteArray(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_486.dat"));
            e = ByteStreams.toByteArray(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_554.dat"));
        }
        catch (Exception exception) {
            throw new AssertionError("Error whilst loading biome definitions", exception);
        }
    }
}

