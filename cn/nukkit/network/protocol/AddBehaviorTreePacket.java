/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class AddBehaviorTreePacket
extends DataPacket {
    public static final byte NETWORK_ID = 89;
    public String behaviorTreeJson;

    @Override
    public byte pid() {
        return 89;
    }

    @Override
    public void decode() {
        this.behaviorTreeJson = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.behaviorTreeJson);
    }

    public String toString() {
        return "AddBehaviorTreePacket(behaviorTreeJson=" + this.behaviorTreeJson + ")";
    }
}

