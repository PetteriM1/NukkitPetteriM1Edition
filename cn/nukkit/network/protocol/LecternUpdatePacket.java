/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class LecternUpdatePacket
extends DataPacket {
    public static final byte NETWORK_ID = 125;
    public int page;
    public int totalPages;
    public BlockVector3 blockPosition;
    public boolean dropBook;

    @Override
    public byte pid() {
        return 125;
    }

    @Override
    public void decode() {
        if (this.protocol < 354) {
            this.page = this.getByte();
            this.blockPosition = this.getBlockVector3();
            this.dropBook = this.getBoolean();
        } else {
            this.page = this.getByte();
            this.totalPages = this.getByte();
            this.blockPosition = this.getBlockVector3();
            this.dropBook = this.getBoolean();
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "LecternUpdatePacket(page=" + this.page + ", totalPages=" + this.totalPages + ", blockPosition=" + this.blockPosition + ", dropBook=" + this.dropBook + ")";
    }
}

