package cn.nukkit.math;

/**
 * auth||: MagicDroidX
 * Nukkit Project
 */
public class SimpleAxisAlignedBB extends AxisAlignedBB {

    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    public SimpleAxisAlignedBB(Vector3 pos1, Vector3 pos2) {
        super(pos1, pos2);
        this.minX = Math.min(pos1.x, pos2.x);
        this.minY = Math.min(pos1.y, pos2.y);
        this.minZ = Math.min(pos1.z, pos2.z);
        this.maxX = Math.max(pos1.x, pos2.x);
        this.maxY = Math.max(pos1.y, pos2.y);
        this.maxZ = Math.max(pos1.z, pos2.z);
    }

    public SimpleAxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override
    public String toString() {
        return "AxisAlignedBB(" + this.getMinX() + ", " + this.getMinY() + ", " + this.getMinZ() + ", " + this.getMaxX() + ", " + this.getMaxY() + ", " + this.getMaxZ() + ")";
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

    @Override
    public AxisAlignedBB clone() {
        return new SimpleAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
