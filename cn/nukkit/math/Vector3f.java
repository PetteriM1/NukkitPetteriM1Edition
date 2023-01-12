/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.Angle;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2f;
import cn.nukkit.math.Vector3;

public class Vector3f
implements Cloneable {
    public static final int SIDE_DOWN = 0;
    public static final int SIDE_UP = 1;
    public static final int SIDE_NORTH = 2;
    public static final int SIDE_SOUTH = 3;
    public static final int SIDE_WEST = 4;
    public static final int SIDE_EAST = 5;
    public float x;
    public float y;
    public float z;

    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector3f(float f2) {
        this(f2, 0.0f, 0.0f);
    }

    public Vector3f(float f2, float f3) {
        this(f2, f3, 0.0f);
    }

    public Vector3f(float f2, float f3, float f4) {
        this.x = f2;
        this.y = f3;
        this.z = f4;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public Vector3f setX(float f2) {
        this.x = f2;
        return this;
    }

    public Vector3f setY(float f2) {
        this.y = f2;
        return this;
    }

    public Vector3f setZ(float f2) {
        this.z = f2;
        return this;
    }

    public int getFloorX() {
        return NukkitMath.floorFloat(this.x);
    }

    public int getFloorY() {
        return NukkitMath.floorFloat(this.y);
    }

    public int getFloorZ() {
        return NukkitMath.floorFloat(this.z);
    }

    public float getRight() {
        return this.x;
    }

    public float getUp() {
        return this.y;
    }

    public float getForward() {
        return this.z;
    }

    public float getSouth() {
        return this.x;
    }

    public float getWest() {
        return this.z;
    }

    public Vector3f add(float f2) {
        return this.add(f2, 0.0f, 0.0f);
    }

    public Vector3f add(float f2, float f3) {
        return this.add(f2, f3, 0.0f);
    }

    public Vector3f add(float f2, float f3, float f4) {
        return new Vector3f(this.x + f2, this.y + f3, this.z + f4);
    }

    public Vector3f add(Vector3f vector3f) {
        return new Vector3f(this.x + vector3f.x, this.y + vector3f.y, this.z + vector3f.z);
    }

    public Vector3f subtract() {
        return this.subtract(0.0f, 0.0f, 0.0f);
    }

    public Vector3f subtract(float f2) {
        return this.subtract(f2, 0.0f, 0.0f);
    }

    public Vector3f subtract(float f2, float f3) {
        return this.subtract(f2, f3, 0.0f);
    }

    public Vector3f subtract(float f2, float f3, float f4) {
        return this.add(-f2, -f3, -f4);
    }

    public Vector3f subtract(Vector3f vector3f) {
        return this.add(-vector3f.x, -vector3f.y, -vector3f.z);
    }

    public Vector3f multiply(float f2) {
        return new Vector3f(this.x * f2, this.y * f2, this.z * f2);
    }

    public Vector3f divide(float f2) {
        return new Vector3f(this.x / f2, this.y / f2, this.z / f2);
    }

    public Vector3f ceil() {
        return new Vector3f((int)Math.ceil(this.x), (int)Math.ceil(this.y), (int)Math.ceil(this.z));
    }

    public Vector3f floor() {
        return new Vector3f(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }

    public Vector3f round() {
        return new Vector3f(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    public Vector3f abs() {
        return new Vector3f((int)Math.abs(this.x), (int)Math.abs(this.y), (int)Math.abs(this.z));
    }

    public Vector3f getSide(int n) {
        return this.getSide(n, 1);
    }

    public Vector3f getSide(int n, int n2) {
        switch (n) {
            case 0: {
                return new Vector3f(this.x, this.y - (float)n2, this.z);
            }
            case 1: {
                return new Vector3f(this.x, this.y + (float)n2, this.z);
            }
            case 2: {
                return new Vector3f(this.x, this.y, this.z - (float)n2);
            }
            case 3: {
                return new Vector3f(this.x, this.y, this.z + (float)n2);
            }
            case 4: {
                return new Vector3f(this.x - (float)n2, this.y, this.z);
            }
            case 5: {
                return new Vector3f(this.x + (float)n2, this.y, this.z);
            }
        }
        return this;
    }

    public static int getOppositeSide(int n) {
        switch (n) {
            case 0: {
                return 1;
            }
            case 1: {
                return 0;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 2;
            }
            case 4: {
                return 5;
            }
            case 5: {
                return 4;
            }
        }
        return -1;
    }

    public double distance(Vector3f vector3f) {
        return Math.sqrt(this.distanceSquared(vector3f));
    }

    public double distanceSquared(Vector3f vector3f) {
        return Math.pow(this.x - vector3f.x, 2.0) + Math.pow(this.y - vector3f.y, 2.0) + Math.pow(this.z - vector3f.z, 2.0);
    }

    public float maxPlainDistance() {
        return this.maxPlainDistance(0.0f, 0.0f);
    }

    public float maxPlainDistance(float f2) {
        return this.maxPlainDistance(f2, 0.0f);
    }

    public float maxPlainDistance(float f2, float f3) {
        return Math.max(Math.abs(this.x - f2), Math.abs(this.z - f3));
    }

    public float maxPlainDistance(Vector2f vector2f) {
        return this.maxPlainDistance(vector2f.x, vector2f.y);
    }

    public float maxPlainDistance(Vector3f vector3f) {
        return this.maxPlainDistance(vector3f.x, vector3f.z);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3f normalize() {
        float f2 = this.lengthSquared();
        if (f2 > 0.0f) {
            return this.divide((float)Math.sqrt(f2));
        }
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public float dot(Vector3f vector3f) {
        return this.x * vector3f.x + this.y * vector3f.y + this.z * vector3f.z;
    }

    public Vector3f cross(Vector3f vector3f) {
        return new Vector3f(this.y * vector3f.z - this.z * vector3f.y, this.z * vector3f.x - this.x * vector3f.z, this.x * vector3f.y - this.y * vector3f.x);
    }

    public Angle angleBetween(Vector3f vector3f) {
        return Angle.fromRadian(Math.acos(Math.min(Math.max(this.normalize().dot(vector3f.normalize()), -1.0f), 1.0f)));
    }

    public Vector3f getIntermediateWithXValue(Vector3f vector3f, float f2) {
        float f3 = vector3f.x - this.x;
        float f4 = vector3f.y - this.y;
        float f5 = vector3f.z - this.z;
        if ((double)(f3 * f3) < 1.0E-7) {
            return null;
        }
        float f6 = (f2 - this.x) / f3;
        if (f6 < 0.0f || f6 > 1.0f) {
            return null;
        }
        return new Vector3f(this.x + f3 * f6, this.y + f4 * f6, this.z + f5 * f6);
    }

    public Vector3f getIntermediateWithYValue(Vector3f vector3f, float f2) {
        float f3 = vector3f.x - this.x;
        float f4 = vector3f.y - this.y;
        float f5 = vector3f.z - this.z;
        if ((double)(f4 * f4) < 1.0E-7) {
            return null;
        }
        float f6 = (f2 - this.y) / f4;
        if (f6 < 0.0f || f6 > 1.0f) {
            return null;
        }
        return new Vector3f(this.x + f3 * f6, this.y + f4 * f6, this.z + f5 * f6);
    }

    public Vector3f getIntermediateWithZValue(Vector3f vector3f, float f2) {
        float f3 = vector3f.x - this.x;
        float f4 = vector3f.y - this.y;
        float f5 = vector3f.z - this.z;
        if ((double)(f5 * f5) < 1.0E-7) {
            return null;
        }
        float f6 = (f2 - this.z) / f5;
        if (f6 < 0.0f || f6 > 1.0f) {
            return null;
        }
        return new Vector3f(this.x + f3 * f6, this.y + f4 * f6, this.z + f5 * f6);
    }

    public Vector3f setComponents(float f2, float f3, float f4) {
        this.x = f2;
        this.y = f3;
        this.z = f4;
        return this;
    }

    public String toString() {
        return "Vector3f(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ')';
    }

    public boolean equals(Object object) {
        if (!(object instanceof Vector3f)) {
            return false;
        }
        Vector3f vector3f = (Vector3f)object;
        return this.x == vector3f.x && this.y == vector3f.y && this.z == vector3f.z;
    }

    public int rawHashCode() {
        return super.hashCode();
    }

    public Vector3f clone() {
        try {
            return (Vector3f)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public Vector3 asVector3() {
        return new Vector3(this.x, this.y, this.z);
    }

    public BlockVector3 asBlockVector3() {
        return new BlockVector3(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }
}

