/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class PunchBlockParticle
extends Particle {
    protected final int blockId;
    protected final int blockDamage;
    protected final int index;
    protected final int face;

    public PunchBlockParticle(Vector3 vector3, Block block, BlockFace blockFace) {
        this(vector3, block.getId(), block.getDamage(), blockFace);
    }

    public PunchBlockParticle(Vector3 vector3, int n, int n2, BlockFace blockFace) {
        super(vector3.x, vector3.y, vector3.z);
        this.blockId = n;
        this.blockDamage = n2;
        this.face = blockFace.getIndex();
        this.index = this.face << 24;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 2014;
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = n <= 201 ? this.blockId | this.blockDamage << 8 | this.face << 16 : GlobalBlockPalette.getOrCreateRuntimeId(n, this.blockId, this.blockDamage) | this.index;
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

