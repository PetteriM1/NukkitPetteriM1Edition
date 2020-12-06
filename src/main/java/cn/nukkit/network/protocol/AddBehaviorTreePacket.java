package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class AddBehaviorTreePacket extends DataPacket {

    public String behaviorTreeJson;

    @Override
    public byte pid() {
        return ProtocolInfo.ADD_BEHAVIOR_TREE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.behaviorTreeJson = this.getString();
    }

    @Override
    public void encode(int protocolId) {
        this.putString(behaviorTreeJson);
    }
}
