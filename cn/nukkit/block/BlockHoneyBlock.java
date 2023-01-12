/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;

public class BlockHoneyBlock
extends BlockSolid {
    @Override
    public String getName() {
        return "Honey Block";
    }

    @Override
    public int getId() {
        return 475;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!(entity.onGround || !(entity.motionY <= 0.08) || entity instanceof Player && ((Player)entity).getAdventureSettings().get(AdventureSettings.Type.FLYING))) {
            double d2 = Math.abs(this.x + 0.5 - entity.x);
            double d3 = Math.abs(this.z + 0.5 - entity.z);
            double d4 = 0.4375 + (double)(entity.getWidth() / 2.0f);
            if (d2 + 0.001 > d4 || d3 + 0.001 > d4) {
                Vector3 vector3 = entity.getMotion();
                vector3.y = -0.05;
                if (entity.motionY < -0.13) {
                    double d5 = -0.05 / entity.motionY;
                    vector3.x *= d5;
                    vector3.z *= d5;
                }
                if (!entity.getMotion().equals(vector3)) {
                    entity.setMotion(vector3);
                }
                entity.resetFallDistance();
                if (Utils.random.nextInt(10) == 0) {
                    this.level.addSound((Vector3)entity, Sound.LAND_SLIME);
                }
            }
        }
    }
}

