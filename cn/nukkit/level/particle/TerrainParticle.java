/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ProtocolInfo;

public class TerrainParticle
extends GenericParticle {
    public TerrainParticle(Vector3 vector3, Block block) {
        super(vector3, 20, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, block.getId(), block.getDamage()));
    }
}

