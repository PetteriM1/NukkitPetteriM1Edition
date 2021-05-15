package cn.nukkit.level.particle;

import cn.nukkit.item.Item;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * Created on 2015/11/21 by xtypr.
 * Package cn.nukkit.level.particle in project Nukkit .
 */
public class ItemBreakParticle extends Particle {

    private final Item item;

    public ItemBreakParticle(Vector3 pos, Item item) {
        super(pos.x, pos.y, pos.z);
        this.item = item;
    }

    @Override
    public DataPacket[] mvEncode(int protocol) {
        int networkId = this.item.getId();
        if (protocol >= ProtocolInfo.v1_16_100) {
            int networkFullId = RuntimeItems.getRuntimeMapping(protocol).getNetworkFullId(this.item);
            networkId = RuntimeItems.getNetworkId(networkFullId);
        }

        LevelEventPacket packet = new LevelEventPacket();
        packet.evid = (short) (LevelEventPacket.EVENT_ADD_PARTICLE_MASK | getMultiversionId(protocol, Particle.TYPE_ITEM_BREAK));
        packet.x = (float) this.x;
        packet.y = (float) this.y;
        packet.z = (float) this.z;
        packet.data = (networkId << 16 | item.getDamage());
        packet.protocol = protocol;
        packet.tryEncode();
        return new DataPacket[]{packet};
    }
}
