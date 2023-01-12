/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;

public class MovingObjectPosition {
    public int typeOfHit;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int sideHit;
    public Vector3 hitVector;
    public Entity entityHit;

    public static MovingObjectPosition fromBlock(int n, int n2, int n3, int n4, Vector3 vector3) {
        MovingObjectPosition movingObjectPosition = new MovingObjectPosition();
        movingObjectPosition.typeOfHit = 0;
        movingObjectPosition.blockX = n;
        movingObjectPosition.blockY = n2;
        movingObjectPosition.blockZ = n3;
        movingObjectPosition.hitVector = new Vector3(vector3.x, vector3.y, vector3.z);
        return movingObjectPosition;
    }

    public static MovingObjectPosition fromEntity(Entity entity) {
        MovingObjectPosition movingObjectPosition = new MovingObjectPosition();
        movingObjectPosition.typeOfHit = 1;
        movingObjectPosition.entityHit = entity;
        movingObjectPosition.hitVector = new Vector3(entity.x, entity.y, entity.z);
        return movingObjectPosition;
    }
}

