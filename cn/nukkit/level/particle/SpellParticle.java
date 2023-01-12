/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.BlockColor;

public class SpellParticle
extends Particle {
    protected final int data;

    public SpellParticle(Vector3 vector3) {
        this(vector3, 0);
    }

    public SpellParticle(Vector3 vector3, int n) {
        super(vector3.x, vector3.y, vector3.z);
        this.data = n;
    }

    public SpellParticle(Vector3 vector3, BlockColor blockColor) {
        this(vector3, blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public SpellParticle(Vector3 vector3, int n, int n2, int n3) {
        this(vector3, n, n2, n3, 0);
    }

    protected SpellParticle(Vector3 vector3, int n, int n2, int n3, int n4) {
        this(vector3, (n4 & 0xFF) << 24 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF);
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 2002;
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = this.data;
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

