/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.item.Item;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class ItemBreakParticle
extends Particle {
    private final Item a;

    public ItemBreakParticle(Vector3 vector3, Item item) {
        super(vector3.x, vector3.y, vector3.z);
        this.a = item;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        int n2 = this.a.getId();
        if (n >= 419) {
            n2 = this.a.getNetworkId(n);
        }
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = (short)(0x4000 | ItemBreakParticle.getMultiversionId(n, 14));
        levelEventPacket.x = (float)this.x;
        levelEventPacket.y = (float)this.y;
        levelEventPacket.z = (float)this.z;
        levelEventPacket.data = n2 << 16 | this.a.getDamage();
        levelEventPacket.protocol = n;
        levelEventPacket.tryEncode();
        return new DataPacket[]{levelEventPacket};
    }
}

