/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.nbt.stream.PGZIPOutputStream;
import cn.nukkit.nbt.stream.PGZIPState;
import cn.nukkit.nbt.stream.PGZIPThreadLocal;
import java.util.concurrent.Callable;

public class PGZIPBlock
implements Callable<byte[]> {
    protected final ThreadLocal<PGZIPState> STATE;
    public static final int SIZE = 65536;
    protected final byte[] in = new byte[65536];
    protected int in_length = 0;

    public PGZIPBlock(PGZIPOutputStream pGZIPOutputStream) {
        this.STATE = new PGZIPThreadLocal(pGZIPOutputStream);
    }

    @Override
    public byte[] call() throws Exception {
        PGZIPState pGZIPState = this.STATE.get();
        pGZIPState.def.reset();
        pGZIPState.buf.reset();
        pGZIPState.str.write(this.in, 0, this.in_length);
        pGZIPState.str.flush();
        return pGZIPState.buf.toByteArray();
    }

    public String toString() {
        return "Block(" + this.in_length + "/" + this.in.length + " bytes)";
    }
}

