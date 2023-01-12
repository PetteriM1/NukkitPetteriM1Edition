/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.UUID;

public interface PlayerDataSerializer {
    public Optional<InputStream> read(String var1, UUID var2) throws IOException;

    public OutputStream write(String var1, UUID var2) throws IOException;
}

