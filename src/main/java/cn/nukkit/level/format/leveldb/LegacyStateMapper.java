package cn.nukkit.level.format.leveldb;

public interface LegacyStateMapper {
    int legacyToRuntime(int legacyId, int meta);

    int runtimeToFullId(int runtimeId);

    int runtimeToLegacyData(int runtimeId);

    int runtimeToLegacyId(int runtimeId);
}
