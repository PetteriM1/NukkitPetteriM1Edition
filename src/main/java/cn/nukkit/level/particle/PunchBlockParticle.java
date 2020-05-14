package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

public class PunchBlockParticle extends Particle {

    protected final int blockId;
    protected final int blockDamage;
    protected final int index;

    public PunchBlockParticle(Vector3 pos, Block block, BlockFace face) {
        this(pos, block.getId(), block.getDamage(), face);
    }

    public PunchBlockParticle(Vector3 pos, int blockId, int blockDamage, BlockFace face) {
        super(pos.x, pos.y, pos.z);
        this.blockId = blockId;
        this.blockDamage = blockDamage;
        this.index = face.getIndex() << 24;
    }

    @Override
    public DataPacket[] encode() {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_PUNCH_BLOCK;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, blockId, blockDamage) | index;

        return new DataPacket[]{pk};
    }

    @Override
    public DataPacket mvEncode(int protocol) {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_PUNCH_BLOCK;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = GlobalBlockPalette.getOrCreateRuntimeId(protocol, blockId, blockDamage) | index;

        return pk;
    }
}
