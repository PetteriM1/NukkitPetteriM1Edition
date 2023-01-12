/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;
import cn.nukkit.math.Vector3f;

public class Vector3fEntityData
extends EntityData<Vector3f> {
    public float x;
    public float y;
    public float z;

    public Vector3fEntityData(int n, float f2, float f3, float f4) {
        super(n);
        this.x = f2;
        this.y = f3;
        this.z = f4;
    }

    public Vector3fEntityData(int n, Vector3f vector3f) {
        this(n, vector3f.x, vector3f.y, vector3f.z);
    }

    @Override
    public Vector3f getData() {
        return new Vector3f(this.x, this.y, this.z);
    }

    @Override
    public void setData(Vector3f vector3f) {
        if (vector3f != null) {
            this.x = vector3f.x;
            this.y = vector3f.y;
            this.z = vector3f.z;
        }
    }

    @Override
    public int getType() {
        return 8;
    }
}

