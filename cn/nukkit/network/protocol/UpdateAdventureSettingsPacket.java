/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class UpdateAdventureSettingsPacket
extends DataPacket {
    private boolean i;
    private boolean g;
    private boolean h;
    private boolean e;
    private boolean f;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.isNoPvM());
        this.putBoolean(this.isNoMvP());
        this.putBoolean(this.isImmutableWorld());
        this.putBoolean(this.isShowNameTags());
        this.putBoolean(this.isAutoJump());
    }

    @Override
    public byte pid() {
        return -68;
    }

    public String toString() {
        return "UpdateAdventureSettingsPacket(noPvM=" + this.isNoPvM() + ", noMvP=" + this.isNoMvP() + ", immutableWorld=" + this.isImmutableWorld() + ", showNameTags=" + this.isShowNameTags() + ", autoJump=" + this.isAutoJump() + ")";
    }

    public boolean isNoPvM() {
        return this.i;
    }

    public boolean isNoMvP() {
        return this.g;
    }

    public boolean isImmutableWorld() {
        return this.h;
    }

    public boolean isShowNameTags() {
        return this.e;
    }

    public boolean isAutoJump() {
        return this.f;
    }

    public void setNoPvM(boolean bl) {
        this.i = bl;
    }

    public void setNoMvP(boolean bl) {
        this.g = bl;
    }

    public void setImmutableWorld(boolean bl) {
        this.h = bl;
    }

    public void setShowNameTags(boolean bl) {
        this.e = bl;
    }

    public void setAutoJump(boolean bl) {
        this.f = bl;
    }
}

