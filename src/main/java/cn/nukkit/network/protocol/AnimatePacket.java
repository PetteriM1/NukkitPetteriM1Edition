package cn.nukkit.network.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class AnimatePacket extends DataPacket {

    public long eid;
    public Action action;
    public float rowingTime;

    @Override
    public void decodePayload(int protocolId) {
        this.action = Action.fromId(this.getVarInt());
        this.eid = getEntityRuntimeId();
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            this.rowingTime = this.getLFloat();
        }
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.action.getId());
        this.putEntityRuntimeId(this.eid);
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            this.putLFloat(this.rowingTime);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.ANIMATE_PACKET;
    }

    public enum Action {
        NO_ACTION(0),
        SWING_ARM(1),
        WAKE_UP(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5),
        ROW_RIGHT(128),
        ROW_LEFT(129);

        private static final Int2ObjectMap<Action> ID_LOOKUP = new Int2ObjectOpenHashMap<>();

        static {
            for (Action value : values()) {
                ID_LOOKUP.put(value.id, value);
            }
        }

        private final int id;

        Action(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Action fromId(int id) {
            return ID_LOOKUP.get(id);
        }
    }
}
