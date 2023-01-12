/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.NukkitMath;

public class Vector2f {
    public final float x;
    public final float y;

    public Vector2f() {
        this(0.0f, 0.0f);
    }

    public Vector2f(float f2) {
        this(f2, 0.0f);
    }

    public Vector2f(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getFloorX() {
        return NukkitMath.floorFloat(this.x);
    }

    public int getFloorY() {
        return NukkitMath.floorFloat(this.y);
    }

    public Vector2f add(float f2) {
        return this.add(f2, 0.0f);
    }

    public Vector2f add(float f2, float f3) {
        return new Vector2f(this.x + f2, this.y + f3);
    }

    public Vector2f add(Vector2f vector2f) {
        return this.add(vector2f.x, vector2f.y);
    }

    public Vector2f subtract(float f2) {
        return this.subtract(f2, 0.0f);
    }

    public Vector2f subtract(float f2, float f3) {
        return this.add(-f2, -f3);
    }

    public Vector2f subtract(Vector2f vector2f) {
        return this.add(-vector2f.x, -vector2f.y);
    }

    public Vector2f ceil() {
        return new Vector2f((int)(this.x + 1.0f), (int)(this.y + 1.0f));
    }

    public Vector2f floor() {
        return new Vector2f(this.getFloorX(), this.getFloorY());
    }

    public Vector2f round() {
        return new Vector2f(Math.round(this.x), Math.round(this.y));
    }

    public Vector2f abs() {
        return new Vector2f(Math.abs(this.x), Math.abs(this.y));
    }

    public Vector2f multiply(float f2) {
        return new Vector2f(this.x * f2, this.y * f2);
    }

    public Vector2f divide(float f2) {
        return new Vector2f(this.x / f2, this.y / f2);
    }

    public double distance(float f2) {
        return this.distance(f2, 0.0f);
    }

    public double distance(float f2, float f3) {
        return Math.sqrt(this.distanceSquared(f2, f3));
    }

    public double distance(Vector2f vector2f) {
        return Math.sqrt(this.distanceSquared(vector2f.x, vector2f.y));
    }

    public double distanceSquared(float f2) {
        return this.distanceSquared(f2, 0.0f);
    }

    public double distanceSquared(float f2, float f3) {
        return Math.pow(this.x - f2, 2.0) + Math.pow(this.y - f3, 2.0);
    }

    public double distanceSquared(Vector2f vector2f) {
        return this.distanceSquared(vector2f.x, vector2f.y);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public Vector2f normalize() {
        float f2 = this.lengthSquared();
        if (f2 != 0.0f) {
            return this.divide((float)Math.sqrt(f2));
        }
        return new Vector2f(0.0f, 0.0f);
    }

    public float dot(Vector2f vector2f) {
        return this.x * vector2f.x + this.y * vector2f.y;
    }

    public String toString() {
        return "Vector2f(x=" + this.x + ",y=" + this.y + ')';
    }
}

