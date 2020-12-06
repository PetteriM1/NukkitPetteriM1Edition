package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

@ToString
public class SpawnParticleEffectPacket extends DataPacket {

    public int dimensionId;
    public long uniqueEntityId = -1;
    public Vector3f position;
    public String identifier;

    @Override
    public byte pid() {
        return ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putByte((byte) this.dimensionId);
        if (protocolId >= 332) {
            this.putEntityUniqueId(uniqueEntityId);
        }
        this.putVector3f(this.position);
        this.putString(this.identifier);
    }
}
