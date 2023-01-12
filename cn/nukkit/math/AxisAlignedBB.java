/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.Server;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;

public class AxisAlignedBB
implements Cloneable {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public AxisAlignedBB(Vector3 vector3, Vector3 vector32) {
        this.minX = Math.min(vector3.x, vector32.x);
        this.minY = Math.min(vector3.y, vector32.y);
        this.minZ = Math.min(vector3.z, vector32.z);
        this.maxX = Math.max(vector3.x, vector32.x);
        this.maxY = Math.max(vector3.y, vector32.y);
        this.maxZ = Math.max(vector3.z, vector32.z);
    }

    public AxisAlignedBB(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.minX = d2;
        this.minY = d3;
        this.minZ = d4;
        this.maxX = d5;
        this.maxY = d6;
        this.maxZ = d7;
    }

    public AxisAlignedBB setBounds(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.minX = d2;
        this.minY = d3;
        this.minZ = d4;
        this.maxX = d5;
        this.maxY = d6;
        this.maxZ = d7;
        return this;
    }

    public AxisAlignedBB addCoord(double d2, double d3, double d4) {
        double d5 = this.minX;
        double d6 = this.minY;
        double d7 = this.minZ;
        double d8 = this.maxX;
        double d9 = this.maxY;
        double d10 = this.maxZ;
        if (d2 < 0.0) {
            d5 += d2;
        }
        if (d2 > 0.0) {
            d8 += d2;
        }
        if (d3 < 0.0) {
            d6 += d3;
        }
        if (d3 > 0.0) {
            d9 += d3;
        }
        if (d4 < 0.0) {
            d7 += d4;
        }
        if (d4 > 0.0) {
            d10 += d4;
        }
        return new AxisAlignedBB(d5, d6, d7, d8, d9, d10);
    }

    public AxisAlignedBB grow(double d2, double d3, double d4) {
        return new AxisAlignedBB(this.minX - d2, this.minY - d3, this.minZ - d4, this.maxX + d2, this.maxY + d3, this.maxZ + d4);
    }

    public AxisAlignedBB expand(double d2, double d3, double d4) {
        this.minX -= d2;
        this.minY -= d3;
        this.minZ -= d4;
        this.maxX += d2;
        this.maxY += d3;
        this.maxZ += d4;
        return this;
    }

    public AxisAlignedBB offset(double d2, double d3, double d4) {
        this.minX += d2;
        this.minY += d3;
        this.minZ += d4;
        this.maxX += d2;
        this.maxY += d3;
        this.maxZ += d4;
        return this;
    }

    public AxisAlignedBB shrink(double d2, double d3, double d4) {
        return new AxisAlignedBB(this.minX + d2, this.minY + d3, this.minZ + d4, this.maxX - d2, this.maxY - d3, this.maxZ - d4);
    }

    public AxisAlignedBB contract(double d2, double d3, double d4) {
        this.minX += d2;
        this.minY += d3;
        this.minZ += d4;
        this.maxX -= d2;
        this.maxY -= d3;
        this.maxZ -= d4;
        return this;
    }

    public AxisAlignedBB setBB(AxisAlignedBB axisAlignedBB) {
        this.minX = axisAlignedBB.minX;
        this.minY = axisAlignedBB.minY;
        this.minZ = axisAlignedBB.minZ;
        this.maxX = axisAlignedBB.maxX;
        this.maxY = axisAlignedBB.maxY;
        this.maxZ = axisAlignedBB.maxZ;
        return this;
    }

    public AxisAlignedBB getOffsetBoundingBox(double d2, double d3, double d4) {
        return new AxisAlignedBB(this.minX + d2, this.minY + d3, this.minZ + d4, this.maxX + d2, this.maxY + d3, this.maxZ + d4);
    }

    public double calculateXOffset(AxisAlignedBB axisAlignedBB, double d2) {
        double d3;
        if (axisAlignedBB.maxY <= this.minY || axisAlignedBB.minY >= this.maxY) {
            return d2;
        }
        if (axisAlignedBB.maxZ <= this.minZ || axisAlignedBB.minZ >= this.maxZ) {
            return d2;
        }
        if (d2 > 0.0 && axisAlignedBB.maxX <= this.minX && (d3 = this.minX - axisAlignedBB.maxX) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && axisAlignedBB.minX >= this.maxX && (d3 = this.maxX - axisAlignedBB.minX) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public double calculateYOffset(AxisAlignedBB axisAlignedBB, double d2) {
        double d3;
        if (axisAlignedBB.maxX <= this.minX || axisAlignedBB.minX >= this.maxX) {
            return d2;
        }
        if (axisAlignedBB.maxZ <= this.minZ || axisAlignedBB.minZ >= this.maxZ) {
            return d2;
        }
        if (d2 > 0.0 && axisAlignedBB.maxY <= this.minY && (d3 = this.minY - axisAlignedBB.maxY) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && axisAlignedBB.minY >= this.maxY && (d3 = this.maxY - axisAlignedBB.minY) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public double calculateZOffset(AxisAlignedBB axisAlignedBB, double d2) {
        double d3;
        if (axisAlignedBB.maxX <= this.minX || axisAlignedBB.minX >= this.maxX) {
            return d2;
        }
        if (axisAlignedBB.maxY <= this.minY || axisAlignedBB.minY >= this.maxY) {
            return d2;
        }
        if (d2 > 0.0 && axisAlignedBB.maxZ <= this.minZ && (d3 = this.minZ - axisAlignedBB.maxZ) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && axisAlignedBB.minZ >= this.maxZ && (d3 = this.maxZ - axisAlignedBB.minZ) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public boolean intersectsWith(AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB.maxX > this.minX && axisAlignedBB.minX < this.maxX && axisAlignedBB.maxY > this.minY && axisAlignedBB.minY < this.maxY) {
            return axisAlignedBB.maxZ > this.minZ && axisAlignedBB.minZ < this.maxZ;
        }
        return false;
    }

    public boolean isVectorInside(Vector3 vector3) {
        return vector3.x >= this.minX && vector3.x <= this.maxX && vector3.y >= this.minY && vector3.y <= this.maxY && vector3.z >= this.minZ && vector3.z <= this.maxZ;
    }

    public double getAverageEdgeLength() {
        return (this.maxX - this.minX + this.maxY - this.minY + this.maxZ - this.minZ) / 3.0;
    }

    public boolean isVectorInYZ(Vector3 vector3) {
        return vector3.y >= this.minY && vector3.y <= this.maxY && vector3.z >= this.minZ && vector3.z <= this.maxZ;
    }

    public boolean isVectorInXZ(Vector3 vector3) {
        return vector3.x >= this.minX && vector3.x <= this.maxX && vector3.z >= this.minZ && vector3.z <= this.maxZ;
    }

    public boolean isVectorInXY(Vector3 vector3) {
        return vector3.x >= this.minX && vector3.x <= this.maxX && vector3.y >= this.minY && vector3.y <= this.maxY;
    }

    public MovingObjectPosition calculateIntercept(Vector3 vector3, Vector3 vector32) {
        Vector3 vector33 = vector3.getIntermediateWithXValue(vector32, this.minX);
        Vector3 vector34 = vector3.getIntermediateWithXValue(vector32, this.maxX);
        Vector3 vector35 = vector3.getIntermediateWithYValue(vector32, this.minY);
        Vector3 vector36 = vector3.getIntermediateWithYValue(vector32, this.maxY);
        Vector3 vector37 = vector3.getIntermediateWithZValue(vector32, this.minZ);
        Vector3 vector38 = vector3.getIntermediateWithZValue(vector32, this.maxZ);
        if (vector33 != null && !this.isVectorInYZ(vector33)) {
            vector33 = null;
        }
        if (vector34 != null && !this.isVectorInYZ(vector34)) {
            vector34 = null;
        }
        if (vector35 != null && !this.isVectorInXZ(vector35)) {
            vector35 = null;
        }
        if (vector36 != null && !this.isVectorInXZ(vector36)) {
            vector36 = null;
        }
        if (vector37 != null && !this.isVectorInXY(vector37)) {
            vector37 = null;
        }
        if (vector38 != null && !this.isVectorInXY(vector38)) {
            vector38 = null;
        }
        Vector3 vector39 = null;
        if (vector33 != null) {
            vector39 = vector33;
        }
        if (vector34 != null && (vector39 == null || vector3.distanceSquared(vector34) < vector3.distanceSquared(vector39))) {
            vector39 = vector34;
        }
        if (vector35 != null && (vector39 == null || vector3.distanceSquared(vector35) < vector3.distanceSquared(vector39))) {
            vector39 = vector35;
        }
        if (vector36 != null && (vector39 == null || vector3.distanceSquared(vector36) < vector3.distanceSquared(vector39))) {
            vector39 = vector36;
        }
        if (vector37 != null && (vector39 == null || vector3.distanceSquared(vector37) < vector3.distanceSquared(vector39))) {
            vector39 = vector37;
        }
        if (vector38 != null && (vector39 == null || vector3.distanceSquared(vector38) < vector3.distanceSquared(vector39))) {
            vector39 = vector38;
        }
        if (vector39 == null) {
            return null;
        }
        int n = -1;
        if (vector39 == vector33) {
            n = 4;
        } else if (vector39 == vector34) {
            n = 5;
        } else if (vector39 == vector35) {
            n = 0;
        } else if (vector39 == vector36) {
            n = 1;
        } else if (vector39 == vector37) {
            n = 2;
        } else if (vector39 == vector38) {
            n = 3;
        }
        return MovingObjectPosition.fromBlock(0, 0, 0, n, vector39);
    }

    public String toString() {
        return "AxisAlignedBB(" + this.minX + ", " + this.minY + ", " + this.minZ + ", " + this.maxX + ", " + this.maxY + ", " + this.maxZ + ')';
    }

    public AxisAlignedBB clone() {
        try {
            return (AxisAlignedBB)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            Server.getInstance().getLogger().logException(cloneNotSupportedException);
            return null;
        }
    }

    public void forEach(BBConsumer bBConsumer) {
        int n = NukkitMath.floorDouble(this.minX);
        int n2 = NukkitMath.floorDouble(this.minY);
        int n3 = NukkitMath.floorDouble(this.minZ);
        int n4 = NukkitMath.floorDouble(this.maxX);
        int n5 = NukkitMath.floorDouble(this.maxY);
        int n6 = NukkitMath.floorDouble(this.maxZ);
        for (int k = n; k <= n4; ++k) {
            for (int i2 = n2; i2 <= n5; ++i2) {
                for (int i3 = n3; i3 <= n6; ++i3) {
                    bBConsumer.accept(k, i2, i3);
                }
            }
        }
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public void setMaxX(double d2) {
        this.maxX = d2;
    }

    public void setMaxY(double d2) {
        this.maxY = d2;
    }

    public void setMaxZ(double d2) {
        this.maxZ = d2;
    }

    public void setMinX(double d2) {
        this.minX = d2;
    }

    public void setMinY(double d2) {
        this.minY = d2;
    }

    public void setMinZ(double d2) {
        this.minZ = d2;
    }

    public static interface BBConsumer<T> {
        public void accept(int var1, int var2, int var3);

        default public T get() {
            return null;
        }
    }
}

