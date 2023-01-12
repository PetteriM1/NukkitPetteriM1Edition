/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.block.BlockDispenser;
import cn.nukkit.dispenser.DispenseBehavior;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultDispenseBehavior
implements DispenseBehavior {
    public boolean success = true;

    @Override
    public Item dispense(BlockDispenser blockDispenser, BlockFace blockFace, Item item) {
        Vector3 vector3 = blockDispenser.getDispensePosition();
        vector3.y = blockFace.getAxis() == BlockFace.Axis.Y ? (vector3.y -= 0.125) : (vector3.y -= 0.15625);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Vector3 vector32 = new Vector3();
        double d2 = threadLocalRandom.nextDouble() * 0.1 + 0.2;
        vector32.x = (double)blockFace.getXOffset() * d2;
        vector32.y = 0.2f;
        vector32.z = (double)blockFace.getZOffset() * d2;
        vector32.x += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        vector32.y += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        vector32.z += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        Item item2 = item.clone();
        item2.setCount(1);
        blockDispenser.level.dropItem(vector3, item2, vector32);
        return null;
    }
}

