/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

public class Vector2 {
    public final double x;
    public final double y;

    public Vector2() {
        this(0.0, 0.0);
    }

    public Vector2(double d2) {
        this(d2, 0.0);
    }

    public Vector2(double d2, double d3) {
        this.x = d2;
        this.y = d3;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getFloorX() {
        return (int)Math.floor(this.x);
    }

    public int getFloorY() {
        return (int)Math.floor(this.y);
    }

    public Vector2 add(double d2) {
        return this.add(d2, 0.0);
    }

    public Vector2 add(double d2, double d3) {
        return new Vector2(this.x + d2, this.y + d3);
    }

    public Vector2 add(Vector2 vector2) {
        return this.add(vector2.x, vector2.y);
    }

    public Vector2 subtract(double d2) {
        return this.subtract(d2, 0.0);
    }

    public Vector2 subtract(double d2, double d3) {
        return this.add(-d2, -d3);
    }

    public Vector2 subtract(Vector2 vector2) {
        return this.add(-vector2.x, -vector2.y);
    }

    public Vector2 ceil() {
        return new Vector2((int)(this.x + 1.0), (int)(this.y + 1.0));
    }

    public Vector2 floor() {
        return new Vector2((int)Math.floor(this.x), (int)Math.floor(this.y));
    }

    public Vector2 round() {
        return new Vector2(Math.round(this.x), Math.round(this.y));
    }

    public Vector2 abs() {
        return new Vector2(Math.abs(this.x), Math.abs(this.y));
    }

    public Vector2 multiply(double d2) {
        return new Vector2(this.x * d2, this.y * d2);
    }

    public Vector2 divide(double d2) {
        return new Vector2(this.x / d2, this.y / d2);
    }

    public double distance(double d2) {
        return this.distance(d2, 0.0);
    }

    public double distance(double d2, double d3) {
        return Math.sqrt(this.distanceSquared(d2, d3));
    }

    public double distance(Vector2 vector2) {
        return Math.sqrt(this.distanceSquared(vector2.x, vector2.y));
    }

    public double distanceSquared(double d2) {
        return this.distanceSquared(d2, 0.0);
    }

    public double distanceSquared(double d2, double d3) {
        return Math.pow(this.x - d2, 2.0) + Math.pow(this.y - d3, 2.0);
    }

    public double distanceSquared(Vector2 vector2) {
        return this.distanceSquared(vector2.x, vector2.y);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public Vector2 normalize() {
        double d2 = this.lengthSquared();
        if (d2 != 0.0) {
            return this.divide(Math.sqrt(d2));
        }
        return new Vector2(0.0, 0.0);
    }

    public double dot(Vector2 vector2) {
        return this.x * vector2.x + this.y * vector2.y;
    }

    public String toString() {
        return "Vector2(x=" + this.x + ",y=" + this.y + ')';
    }
}

