/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;

public class BlockVector3
implements Cloneable {
    public int x;
    public int y;
    public int z;

    public BlockVector3(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
    }

    public BlockVector3() {
    }

    public BlockVector3 setComponents(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
        return this;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public BlockVector3 setX(int n) {
        this.x = n;
        return this;
    }

    public BlockVector3 setY(int n) {
        this.y = n;
        return this;
    }

    public BlockVector3 setZ(int n) {
        this.z = n;
        return this;
    }

    public Vector3 add(double d2) {
        return this.add(d2, 0.0, 0.0);
    }

    public Vector3 add(double d2, double d3) {
        return this.add(d2, d3, 0.0);
    }

    public Vector3 add(double d2, double d3, double d4) {
        return new Vector3((double)this.x + d2, (double)this.y + d3, (double)this.z + d4);
    }

    public Vector3 add(Vector3 vector3) {
        return new Vector3((double)this.x + vector3.getX(), (double)this.y + vector3.getY(), (double)this.z + vector3.getZ());
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
        return this.add(-vector3.getX(), -vector3.getY(), -vector3.getZ());
    }

    public BlockVector3 add(int n) {
        return this.add(n, 0, 0);
    }

    public BlockVector3 add(int n, int n2) {
        return this.add(n, n2, 0);
    }

    public BlockVector3 add(int n, int n2, int n3) {
        return new BlockVector3(this.x + n, this.y + n2, this.z + n3);
    }

    public BlockVector3 add(BlockVector3 blockVector3) {
        return new BlockVector3(this.x + blockVector3.x, this.y + blockVector3.y, this.z + blockVector3.z);
    }

    public BlockVector3 subtract() {
        return this.subtract(0, 0, 0);
    }

    public BlockVector3 subtract(int n) {
        return this.subtract(n, 0, 0);
    }

    public BlockVector3 subtract(int n, int n2) {
        return this.subtract(n, n2, 0);
    }

    public BlockVector3 subtract(int n, int n2, int n3) {
        return this.add(-n, -n2, -n3);
    }

    public BlockVector3 subtract(BlockVector3 blockVector3) {
        return this.add(-blockVector3.x, -blockVector3.y, -blockVector3.z);
    }

    public BlockVector3 multiply(int n) {
        return new BlockVector3(this.x * n, this.y * n, this.z * n);
    }

    public BlockVector3 divide(int n) {
        return new BlockVector3(this.x / n, this.y / n, this.z / n);
    }

    public BlockVector3 getSide(BlockFace blockFace) {
        return this.getSide(blockFace, 1);
    }

    public BlockVector3 getSide(BlockFace blockFace, int n) {
        return new BlockVector3(this.x + blockFace.getXOffset() * n, this.y + blockFace.getYOffset() * n, this.z + blockFace.getZOffset() * n);
    }

    public BlockVector3 up() {
        return this.up(1);
    }

    public BlockVector3 up(int n) {
        return this.getSide(BlockFace.UP, n);
    }

    public BlockVector3 down() {
        return this.down(1);
    }

    public BlockVector3 down(int n) {
        return this.getSide(BlockFace.DOWN, n);
    }

    public BlockVector3 north() {
        return this.north(1);
    }

    public BlockVector3 north(int n) {
        return this.getSide(BlockFace.NORTH, n);
    }

    public BlockVector3 south() {
        return this.south(1);
    }

    public BlockVector3 south(int n) {
        return this.getSide(BlockFace.SOUTH, n);
    }

    public BlockVector3 east() {
        return this.east(1);
    }

    public BlockVector3 east(int n) {
        return this.getSide(BlockFace.EAST, n);
    }

    public BlockVector3 west() {
        return this.west(1);
    }

    public BlockVector3 west(int n) {
        return this.getSide(BlockFace.WEST, n);
    }

    public double distance(Vector3 vector3) {
        return Math.sqrt(this.distanceSquared(vector3));
    }

    public double distance(BlockVector3 blockVector3) {
        return Math.sqrt(this.distanceSquared(blockVector3));
    }

    public double distanceSquared(Vector3 vector3) {
        return this.distanceSquared(vector3.x, vector3.y, vector3.z);
    }

    public double distanceSquared(BlockVector3 blockVector3) {
        return this.distanceSquared(blockVector3.x, blockVector3.y, blockVector3.z);
    }

    public double distanceSquared(double d2, double d3, double d4) {
        return Math.pow((double)this.x - d2, 2.0) + Math.pow((double)this.y - d3, 2.0) + Math.pow((double)this.z - d4, 2.0);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof BlockVector3)) {
            return false;
        }
        BlockVector3 blockVector3 = (BlockVector3)object;
        return this.x == blockVector3.x && this.y == blockVector3.y && this.z == blockVector3.z;
    }

    public final int hashCode() {
        return this.x ^ this.z << 12 ^ this.y << 24;
    }

    public String toString() {
        return "BlockVector3(level=,x=" + this.x + ",y=" + this.y + ",z=" + this.z + ')';
    }

    public BlockVector3 clone() {
        try {
            return (BlockVector3)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public Vector3 asVector3() {
        return new Vector3(this.x, this.y, this.z);
    }

    public Vector3f asVector3f() {
        return new Vector3f(this.x, this.y, this.z);
    }
}

