/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class CommandBlockUpdatePacket
extends DataPacket {
    public static final byte NETWORK_ID = 78;
    public boolean isBlock;
    public int x;
    public int y;
    public int z;
    public int commandBlockMode;
    public boolean isRedstoneMode;
    public boolean isConditional;
    public long minecartEid;
    public String command;
    public String lastOutput;
    public String name;
    public boolean shouldTrackOutput;

    @Override
    public byte pid() {
        return 78;
    }

    @Override
    public void decode() {
        this.isBlock = this.getBoolean();
        if (this.isBlock) {
            BlockVector3 blockVector3 = this.getBlockVector3();
            this.x = blockVector3.x;
            this.y = blockVector3.y;
            this.z = blockVector3.z;
            this.commandBlockMode = (int)this.getUnsignedVarInt();
            this.isRedstoneMode = this.getBoolean();
            this.isConditional = this.getBoolean();
        } else {
            this.minecartEid = this.getEntityRuntimeId();
        }
        this.command = this.getString();
        this.lastOutput = this.getString();
        this.name = this.getString();
        this.shouldTrackOutput = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.isBlock);
        if (this.isBlock) {
            this.putBlockVector3(this.x, this.y, this.z);
            this.putUnsignedVarInt(this.commandBlockMode);
            this.putBoolean(this.isRedstoneMode);
            this.putBoolean(this.isConditional);
        } else {
            this.putEntityRuntimeId(this.minecartEid);
        }
        this.putString(this.command);
        this.putString(this.lastOutput);
        this.putString(this.name);
        this.putBoolean(this.shouldTrackOutput);
    }

    public String toString() {
        return "CommandBlockUpdatePacket(isBlock=" + this.isBlock + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", commandBlockMode=" + this.commandBlockMode + ", isRedstoneMode=" + this.isRedstoneMode + ", isConditional=" + this.isConditional + ", minecartEid=" + this.minecartEid + ", command=" + this.command + ", lastOutput=" + this.lastOutput + ", name=" + this.name + ", shouldTrackOutput=" + this.shouldTrackOutput + ")";
    }
}

