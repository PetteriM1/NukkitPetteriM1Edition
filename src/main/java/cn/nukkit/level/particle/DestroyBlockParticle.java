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
    public DataPacket[] encode() {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, block.getId(), block.getDamage());

        return new DataPacket[]{pk};
    }

    @Override
    public DataPacket mvEncode(int protocol) {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getId(), block.getDamage());

        return pk;
    }
}
