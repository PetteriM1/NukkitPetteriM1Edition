/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.entity.custom.EntityManager;
import cn.nukkit.network.protocol.DataPacket;
import com.google.common.io.ByteStreams;
import java.io.InputStream;

public class AvailableEntityIdentifiersPacket
extends DataPacket {
    public static final byte NETWORK_ID = 119;
    public static final byte[] TAG;

    @Override
    public byte pid() {
        return 119;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        if (this.protocol <= 407) {
            this.put(EntityManager.get().getNetworkTagCachedOld());
        } else {
            this.put(EntityManager.get().getNetworkTagCached());
        }
    }

    static {
        try {
            InputStream inputStream = Nukkit.class.getClassLoader().getResourceAsStream("entity_identifiers_544.dat");
            if (inputStream == null) {
                throw new AssertionError((Object)"Could not find entity_identifiers_544.dat");
            }
            TAG = ByteStreams.toByteArray(inputStream);
        }
        catch (Exception exception) {
            throw new AssertionError("Error whilst loading entity_identifiers_544.dat", exception);
        }
    }

    private static Exception b(Exception exception) {
        return exception;
    }
}

