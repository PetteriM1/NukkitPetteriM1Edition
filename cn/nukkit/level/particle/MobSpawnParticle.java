/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class MobSpawnParticle
extends Particle {
    protected final int width;
    protected final int height;

    public MobSpawnParticle(Vector3 vector3, float f2, float f3) {
        super(vector3.x, vector3.y, vector3.z);
        this.width = (int)f2;
        this.height = (int)f3;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 2004;
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = (this.width & 0xFF) + ((this.height & 0xFF) << 8);
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

