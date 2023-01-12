/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class AnimatePacket
extends DataPacket {
    public static final byte NETWORK_ID = 44;
    public long eid;
    public Action action;
    public float rowingTime;

    @Override
    public void decode() {
        this.action = Action.fromId(this.getVarInt());
        this.eid = this.getEntityRuntimeId();
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            this.rowingTime = this.getLFloat();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.action.getId());
        this.putEntityRuntimeId(this.eid);
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            this.putLFloat(this.rowingTime);
        }
    }

    @Override
    public byte pid() {
        return 44;
    }

    public String toString() {
        return "AnimatePacket(eid=" + this.eid + ", action=" + (Object)((Object)this.action) + ", rowingTime=" + this.rowingTime + ")";
    }

    public static enum Action {
        NO_ACTION(0),
        SWING_ARM(1),
        WAKE_UP(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5),
        ROW_RIGHT(128),
        ROW_LEFT(129);

        private static final Int2ObjectMap<Action> a;
        private final int c;

        private Action(int n2) {
            this.c = n2;
        }

        public int getId() {
            return this.c;
        }

        public static Action fromId(int n) {
            return (Action)((Object)a.get(n));
        }

        static {
            a = new Int2ObjectOpenHashMap<Action>();
            for (Action action : Action.values()) {
                a.put(action.c, action);
            }
        }
    }
}

