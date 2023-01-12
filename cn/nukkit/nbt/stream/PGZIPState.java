/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.nbt.stream.PGZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PGZIPState {
    protected final DeflaterOutputStream str;
    protected final ByteArrayOutputStream buf;
    protected final Deflater def;

    public PGZIPState(PGZIPOutputStream pGZIPOutputStream) {
        this.def = pGZIPOutputStream.newDeflater();
        this.buf = new ByteArrayOutputStream(65536);
        this.str = PGZIPOutputStream.newDeflaterOutputStream(this.buf, this.def);
    }
}

