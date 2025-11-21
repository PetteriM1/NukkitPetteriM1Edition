package cn.nukkit.resourcepacks;

import java.util.UUID;

public interface ResourcePack {

    ResourcePack[] EMPTY_ARRAY = new ResourcePack[0];

    default String getCDNUrl() {
        return "";
    }

    default String getEncryptionKey() {
        return "";
    }

    byte[] getPackChunk(int off, int len);

    UUID getPackId();

    String getPackName();

    int getPackSize();

    String getPackVersion();

    byte[] getSha256();

    default String getSubPackName() {
        return "";
    }

    default boolean isAddonPack() {
        return false;
    }

    default boolean isRaytracingCapable() {
        return false;
    }

    default boolean usesScripting() {
        return false;
    }
}
