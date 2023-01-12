/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.block.Block;
import cn.nukkit.math.Angle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3f;

public class Vector3
implements Cloneable {
    public double x;
    public double y;
    public double z;

    public Vector3() {
        this(0.0, 0.0, 0.0);
    }

    public Vector3(double d2) {
        this(d2, 0.0, 0.0);
    }

    public Vector3(double d2, double d3) {
        this(d2, d3, 0.0);
    }

    public Vector3(double d2, double d3, double d4) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vector3 setX(double d2) {
        this.x = d2;
        return this;
    }

    public Vector3 setY(double d2) {
        this.y = d2;
        return this;
    }

    public Vector3 setZ(double d2) {
        this.z = d2;
        return this;
    }

    public int getFloorX() {
        return (int)Math.floor(this.x);
    }

    public int getFloorY() {
        return (int)Math.floor(this.y);
    }

    public int getFloorZ() {
        return (int)Math.floor(this.z);
    }

    public int getChunkX() {
        return this.getFloorX() >> 4;
    }

    public int getChunkZ() {
        return this.getFloorZ() >> 4;
    }

    public double getRight() {
        return this.x;
    }

    public double getUp() {
        return this.y;
    }

    public double getForward() {
        return this.z;
    }

    public double getSouth() {
        return this.x;
    }

    public double getWest() {
        return this.z;
    }

    public Vector3 add(double d2) {
        return this.add(d2, 0.0, 0.0);
    }

    public Vector3 add(double d2, double d3) {
        return this.add(d2, d3, 0.0);
    }

    public Vector3 add(double d2, double d3, double d4) {
        return new Vector3(this.x + d2, this.y + d3, this.z + d4);
    }

    public Vector3 add(Vector3 vector3) {
        return new Vector3(this.x + vector3.x, this.y + vector3.y, this.z + vector3.z);
    }

    public Vector3 subtract() {
        return this.subtract(0.0, 0.0, 0.0);
    }

    public Vector3 subtract(double d2) {
        return this.subtract(d2, 0.0, 0.0);
    }

    public Vector3 subtract(double d2, double d3) {
        return this.subtract(d2, d3, 0.0);
    }

    public Vector3 subtract(double d2, double d3, double d4) {
        return this.add(-d2, -d3, -d4);
    }

    public Vector3 subtract(Vector3 vector3) {
        return this.add(-vector3.x, -vector3.y, -vector3.z);
    }

    public Vector3 multiply(double d2) {
        return new Vector3(this.x * d2, this.y * d2, this.z * d2);
    }

    public Vector3 divide(double d2) {
        return new Vector3(this.x / d2, this.y / d2, this.z / d2);
    }

    public Vector3 ceil() {
        return new Vector3((int)Math.ceil(this.x), (int)Math.ceil(this.y), (int)Math.ceil(this.z));
    }

    public Vector3 floor() {
        return new Vector3(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }

    public Vector3 round() {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    public Vector3 abs() {
        return new Vector3((int)Math.abs(this.x), (int)Math.abs(this.y), (int)Math.abs(this.z));
    }

    public Vector3 getSide(BlockFace blockFace) {
        return this.getSide(blockFace, 1);
    }

    public Vector3 getSide(BlockFace blockFace, int n) {
        return new Vector3(this.x + (double)(blockFace.getXOffset() * n), this.y + (double)(blockFace.getYOffset() * n), this.z + (double)(blockFace.getZOffset() * n));
    }

    public Vector3 getSideVec(BlockFace blockFace) {
        return new Vector3(this.getX() + (double)blockFace.getXOffset(), this.getY() + (double)blockFace.getYOffset(), this.getZ() + (double)blockFace.getZOffset());
    }

    public Vector3 up() {
        return this.up(1);
    }

    public Vector3 up(int n) {
        return this.getSide(BlockFace.UP, n);
    }

    public Vector3 down() {
        return this.down(1);
    }

    public Vector3 down(int n) {
        return this.getSide(BlockFace.DOWN, n);
    }

    public Vector3 north() {
        return this.north(1);
    }

    public Vector3 north(int n) {
        return this.getSide(BlockFace.NORTH, n);
    }

    public Vector3 south() {
        return this.south(1);
    }

    public Vector3 south(int n) {
        return this.getSide(BlockFace.SOUTH, n);
    }

    public Vector3 east() {
        return this.east(1);
    }

    public Vector3 east(int n) {
        return this.getSide(BlockFace.EAST, n);
    }

    public Vector3 west() {
        return this.west(1);
    }

    public Vector3 west(int n) {
        return this.getSide(BlockFace.WEST, n);
    }

    public double distance(Vector3 vector3) {
        return Math.sqrt(this.distanceSquared(vector3));
    }

    public double distanceSquared(Vector3 vector3) {
        return Math.pow(this.x - vector3.x, 2.0) + Math.pow(this.y - vector3.y, 2.0) + Math.pow(this.z - vector3.z, 2.0);
    }

    public double maxPlainDistance() {
        return this.maxPlainDistance(0.0, 0.0);
    }

    public double maxPlainDistance(double d2) {
        return this.maxPlainDistance(d2, 0.0);
    }

    public double maxPlainDistance(double d2, double d3) {
        return Math.max(Math.abs(this.x - d2), Math.abs(this.z - d3));
    }

    public double maxPlainDistance(Vector2 vector2) {
        return this.maxPlainDistance(vector2.x, vector2.y);
    }

    public double maxPlainDistance(Vector3 vector3) {
        return this.maxPlainDistance(vector3.x, vector3.z);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3 normalize() {
        double d2 = this.lengthSquared();
        if (d2 > 0.0) {
            return this.divide(Math.sqrt(d2));
        }
        return new Vector3(0.0, 0.0, 0.0);
    }

    public double dot(Vector3 vector3) {
        return this.x * vector3.x + this.y * vector3.y + this.z * vector3.z;
    }

    public Vector3 cross(Vector3 vector3) {
        return new Vector3(this.y * vector3.z - this.z * vector3.y, this.z * vector3.x - this.x * vector3.z, this.x * vector3.y - this.y * vector3.x);
    }

    public Angle angleBetween(Vector3 vector3) {
        return Angle.fromRadian(Math.acos(Math.min(Math.max(this.normalize().dot(vector3.normalize()), -1.0), 1.0)));
    }

    public Vector3 getIntermediateWithXValue(Vector3 vector3, double d2) {
        double d3 = vector3.x - this.x;
        double d4 = vector3.y - this.y;
        double d5 = vector3.z - this.z;
        if (d3 * d3 < 1.0E-7) {
            return null;
        }
        double d6 = (d2 - this.x) / d3;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return new Vector3(this.x + d3 * d6, this.y + d4 * d6, this.z + d5 * d6);
    }

    public Vector3 getIntermediateWithYValue(Vector3 vector3, double d2) {
        double d3 = vector3.x - this.x;
        double d4 = vector3.y - this.y;
        double d5 = vector3.z - this.z;
        if (d4 * d4 < 1.0E-7) {
            return null;
        }
        double d6 = (d2 - this.y) / d4;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return new Vector3(this.x + d3 * d6, this.y + d4 * d6, this.z + d5 * d6);
    }

    public Vector3 getIntermediateWithZValue(Vector3 vector3, double d2) {
        double d3 = vector3.x - this.x;
        double d4 = vector3.y - this.y;
        double d5 = vector3.z - this.z;
        if (d5 * d5 < 1.0E-7) {
            return null;
        }
        double d6 = (d2 - this.z) / d5;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return new Vector3(this.x + d3 * d6, this.y + d4 * d6, this.z + d5 * d6);
    }

    public Vector3 setComponents(double d2, double d3, double d4) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
        return this;
    }

    public String toString() {
        return "Vector3(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ')';
    }

    public boolean equals(Object object) {
        if (!(object instanceof Vector3)) {
            return false;
        }
        Vector3 vector3 = (Vector3)object;
        return this.x == vector3.x && this.y == vector3.y && this.z == vector3.z;
    }

    public int hashCode() {
        return (int)this.x ^ (int)this.z << 12 ^ (int)this.y << 24;
    }

    public int rawHashCode() {
        return super.hashCode();
    }

    public Vector3 clone() {
        try {
            return (Vector3)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public Vector3f asVector3f() {
        return new Vector3f((float)this.x, (float)this.y, (float)this.z);
    }

    public BlockVector3 asBlockVector3() {
        return new BlockVector3(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }

    protected Block fastCloneBlock() {
        try {
            return (Block)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException(cloneNotSupportedException);
        }
    }

    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }
}

