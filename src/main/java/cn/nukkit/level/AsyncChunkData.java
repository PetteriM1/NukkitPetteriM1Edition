package cn.nukkit.level;

class AsyncChunkData {

    final int protocolId;
    final long timestamp;
    final int x;
    final int z;
    final long hash;
    final byte[] data;
    final int count;

    AsyncChunkData(int protocolId, long timestamp, int x, int z, long hash, byte[] data, int count) {
        this.protocolId = protocolId;
        this.timestamp = timestamp;
        this.x = x;
        this.z = z;
        this.hash = hash;
        this.data = data;
        this.count = count;
    }
}
