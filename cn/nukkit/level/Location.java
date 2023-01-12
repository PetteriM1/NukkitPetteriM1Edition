/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.LevelException;

public class Location
extends Position {
    public double yaw;
    public double pitch;
    public double headYaw;

    public Location() {
        this(0.0);
    }

    public Location(double d2) {
        this(d2, 0.0);
    }

    public Location(double d2, double d3) {
        this(d2, d3, 0.0);
    }

    public Location(double d2, double d3, double d4) {
        this(d2, d3, d4, 0.0, 0.0, 0.0, null);
    }

    public Location(double d2, double d3, double d4, Level level) {
        this(d2, d3, d4, 0.0, 0.0, 0.0, level);
    }

    public Location(double d2, double d3, double d4, double d5) {
        this(d2, d3, d4, d5, 0.0, 0.0, null);
    }

    public Location(double d2, double d3, double d4, double d5, double d6) {
        this(d2, d3, d4, d5, d6, 0.0, null);
    }

    public Location(double d2, double d3, double d4, double d5, double d6, Level level) {
        this(d2, d3, d4, d5, d6, 0.0, level);
    }

    public Location(double d2, double d3, double d4, double d5, double d6, double d7) {
        this(d2, d3, d4, d5, d6, d7, null);
    }

    public Location(double d2, double d3, double d4, double d5, double d6, double d7, Level level) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
        this.yaw = d5;
        this.pitch = d6;
        this.headYaw = d7;
        this.level = level;
    }

    public static Location fromObject(Vector3 vector3) {
        return Location.fromObject(vector3, null, 0.0, 0.0, 0.0);
    }

    public static Location fromObject(Vector3 vector3, Level level) {
        return Location.fromObject(vector3, level, 0.0, 0.0, 0.0);
    }

    public static Location fromObject(Vector3 vector3, Level level, double d2) {
        return Location.fromObject(vector3, level, d2, 0.0, 0.0);
    }

    public static Location fromObject(Vector3 vector3, Level level, double d2, double d3) {
        return Location.fromObject(vector3, level, d2, d3, 0.0);
    }

    public static Location fromObject(Vector3 vector3, Level level, double d2, double d3, double d4) {
        return new Location(vector3.x, vector3.y, vector3.z, d2, d3, d4, level == null ? (vector3 instanceof Position ? ((Position)vector3).level : null) : level);
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

    public double getHeadYaw() {
        return this.headYaw;
    }

    public Location setYaw(double d2) {
        this.yaw = d2;
        return this;
    }

    public Location setBothYaw(double d2) {
        this.yaw = d2;
        this.headYaw = d2;
        return this;
    }

    public Location setPitch(double d2) {
        this.pitch = d2;
        return this;
    }

    public Location setHeadYaw(double d2) {
        this.headYaw = d2;
        return this;
    }

    @Override
    public String toString() {
        return "Location(level=" + (this.isValid() ? this.getLevel().getName() : "null") + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ", headYaw=" + this.headYaw + ')';
    }

    @Override
    public Location getLocation() {
        if (this.isValid()) {
            return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.headYaw, this.level);
        }
        throw new LevelException("Undefined Level reference");
    }

    @Override
    public Location add(double d2) {
        return this.add(d2, 0.0, 0.0);
    }

    @Override
    public Location add(double d2, double d3) {
        return this.add(d2, d3, 0.0);
    }

    @Override
    public Location add(double d2, double d3, double d4) {
        return new Location(this.x + d2, this.y + d3, this.z + d4, this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location add(Vector3 vector3) {
        return new Location(this.x + vector3.getX(), this.y + vector3.getY(), this.z + vector3.getZ(), this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location subtract() {
        return this.subtract(0.0, 0.0, 0.0);
    }

    @Override
    public Location subtract(double d2) {
        return this.subtract(d2, 0.0, 0.0);
    }

    @Override
    public Location subtract(double d2, double d3) {
        return this.subtract(d2, d3, 0.0);
    }

    @Override
    public Location subtract(double d2, double d3, double d4) {
        return this.add(-d2, -d3, -d4);
    }

    @Override
    public Location subtract(Vector3 vector3) {
        return this.add(-vector3.getX(), -vector3.getY(), -vector3.getZ());
    }

    @Override
    public Location multiply(double d2) {
        return new Location(this.x * d2, this.y * d2, this.z * d2, this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location divide(double d2) {
        return new Location(this.x / d2, this.y / d2, this.z / d2, this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location ceil() {
        return new Location((int)Math.ceil(this.x), (int)Math.ceil(this.y), (int)Math.ceil(this.z), this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location floor() {
        return new Location(this.getFloorX(), this.getFloorY(), this.getFloorZ(), this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location round() {
        return new Location(Math.round(this.x), Math.round(this.y), Math.round(this.z), this.yaw, this.pitch, this.headYaw, this.level);
    }

    @Override
    public Location abs() {
        return new Location((int)Math.abs(this.x), (int)Math.abs(this.y), (int)Math.abs(this.z), this.yaw, this.pitch, this.headYaw, this.level);
    }

    public Vector3 getDirectionVector() {
        double d2 = (this.pitch + 90.0) * Math.PI / 180.0;
        double d3 = (this.yaw + 90.0) * Math.PI / 180.0;
        double d4 = Math.sin(d2) * Math.cos(d3);
        double d5 = Math.sin(d2) * Math.sin(d3);
        double d6 = Math.cos(d2);
        return new Vector3(d4, d6, d5).normalize();
    }

    @Override
    public Location clone() {
        return (Location)super.clone();
    }

    private static LevelException a(LevelException levelException) {
        return levelException;
    }
}

