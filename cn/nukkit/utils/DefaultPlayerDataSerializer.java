/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.utils.PlayerDataSerializer;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.UUID;

public class DefaultPlayerDataSerializer
implements PlayerDataSerializer {
    private final String a;

    public DefaultPlayerDataSerializer(Server server) {
        this(server.getDataPath());
    }

    public DefaultPlayerDataSerializer(String string) {
        this.a = string;
    }

    @Override
    public Optional<InputStream> read(String string, UUID uUID) throws IOException {
        File file = new File(this.a + "players/" + string + ".dat");
        if (!file.exists()) {
            return Optional.empty();
        }
        return Optional.of(new FileInputStream(file));
    }

    @Override
    public OutputStream write(String string, UUID uUID) throws IOException {
        Preconditions.checkNotNull(string, "name");
        File file = new File(this.a + "players/" + string + ".dat");
        return new FileOutputStream(file);
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

