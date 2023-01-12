/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.nbt.stream.PGZIPOutputStream;
import cn.nukkit.nbt.stream.PGZIPState;

public class PGZIPThreadLocal
extends ThreadLocal<PGZIPState> {
    private final PGZIPOutputStream a;

    public PGZIPThreadLocal(PGZIPOutputStream pGZIPOutputStream) {
        this.a = pGZIPOutputStream;
    }

    @Override
    protected PGZIPState initialValue() {
        return new PGZIPState(this.a);
    }
}

