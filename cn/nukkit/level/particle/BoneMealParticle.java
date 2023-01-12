/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class BoneMealParticle
extends Particle {
    public BoneMealParticle(Vector3 vector3) {
        super(vector3.x, vector3.y, vector3.z);
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 2005;
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = 0;
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

