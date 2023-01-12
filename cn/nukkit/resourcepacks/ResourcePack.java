/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.resourcepacks;

import java.util.UUID;

public interface ResourcePack {
    public String getPackName();

    public UUID getPackId();

    public String getPackVersion();

    public int getPackSize();

    public byte[] getSha256();

    public byte[] getPackChunk(int var1, int var2);

    public String getEncryptionKey();
}

