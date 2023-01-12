/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class DestroyBlockParticle
extends Particle {
    protected final Block block;

    public DestroyBlockParticle(Vector3 vector3, Block block) {
        super(vector3.x, vector3.y, vector3.z);
        this.block = block;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 2001;
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = n <= 201 ? this.block.getId() | this.block.getDamage() << 8 : GlobalBlockPalette.getOrCreateRuntimeId(n, this.block.getId(), this.block.getDamage());
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

