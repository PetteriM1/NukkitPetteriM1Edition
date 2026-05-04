package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * Created on 2015/11/21 by xtypr.
 * Package cn.nukkit.level.particle in project Nukkit .
 */
public class DestroyBlockParticle extends Particle {

    protected final Block block;

    public DestroyBlockParticle(Vector3 pos, Block block) {
        super(pos.x, pos.y, pos.z);
        this.block = block;
    }

    @Override
    public DataPacket[] mvEncode(int protocol) {
        LevelEventPacket packet = new LevelEventPacket();
        packet.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY;
        packet.x = (float) this.x;
        packet.y = (float) this.y;
        packet.z = (float) this.z;

        int data;
        if (protocol <= ProtocolInfo.v1_2_10) {
            data = block.getId() | (block.getDamage() << 8);
        } else if (protocol < block.getMinimumVersion()) {
            data = GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getAlternateBlock(protocol).getLegacyId(), block.getAlternateMeta(protocol));
        } else {
            data = GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getId(), block.getDamage());
        }

        packet.data = data;
        packet.protocol = protocol;
        packet.tryEncode();
        return new DataPacket[]{packet};
    }
}
