package org.iq80.leveldb;

public class Defaults {

    public static final ReadOptions DEFAULT_READ_OPTIONS = new ImmutableReadOptions();
    public static final WriteOptions DEFAULT_WRITE_OPTIONS = new ImmutableWriteOptions();
}

class ImmutableReadOptions extends ReadOptions {

    @Override
    public ReadOptions snapshot(Snapshot snapshot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReadOptions fillCache(boolean fillCache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReadOptions verifyChecksums(boolean verifyChecksums) {
        throw new UnsupportedOperationException();
    }
}

class ImmutableWriteOptions extends WriteOptions {

    @Override
    public WriteOptions sync(boolean sync) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteOptions snapshot(boolean snapshot) {
        throw new UnsupportedOperationException();
    }
}
