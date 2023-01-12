/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.potion.Effect;

public class BlockWitherRose
extends BlockFlower {
    public BlockWitherRose() {
        this(0);
    }

    public BlockWitherRose(int n) {
        super(0);
    }

    @Override
    public int getId() {
        return 471;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (this.level.getServer().getDifficulty() != 0 && entity instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving)entity;
            if (!(entityLiving.invulnerable || entityLiving.hasEffect(20) || entityLiving instanceof Player && (((Player)entityLiving).isCreative() || ((Player)entityLiving).isSpectator()))) {
                Effect effect = Effect.getEffect(20);
                effect.setDuration(40);
                effect.setAmplifier(1);
                entityLiving.addEffect(effect);
            }
        }
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.2, this.y + 0.2, this.z + 0.2, this.x + 0.8, this.y + 0.8, this.z + 0.8);
    }
}

