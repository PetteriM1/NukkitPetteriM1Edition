/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class GenericParticle
extends Particle {
    protected int id;
    protected final int data;

    public GenericParticle(Vector3 vector3, int n) {
        this(vector3, n, 0);
    }

    public GenericParticle(Vector3 vector3, int n, int n2) {
        super(vector3.x, vector3.y, vector3.z);
        this.id = n;
        this.data = n2;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = (short)(0x4000 | GenericParticle.getMultiversionId(n, this.id));
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = this.data;
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

