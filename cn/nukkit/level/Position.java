/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.LevelException;

public class Position
extends Vector3 {
    public Level level;

    public Position() {
        this(0.0, 0.0, 0.0, null);
    }

    public Position(double d2) {
        this(d2, 0.0, 0.0, null);
    }

    public Position(double d2, double d3) {
        this(d2, d3, 0.0, null);
    }

    public Position(double d2, double d3, double d4) {
        this(d2, d3, d4, null);
    }

    public Position(double d2, double d3, double d4, Level level) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
        this.level = level;
    }

    public static Position fromObject(Vector3 vector3) {
        return Position.fromObject(vector3, null);
    }

    public static Position fromObject(Vector3 vector3, Level level) {
        return new Position(vector3.x, vector3.y, vector3.z, level);
    }

    public Level getLevel() {
        return this.level;
    }

    public Position setLevel(Level level) {
        this.level = level;
        return this;
    }

    public boolean isValid() {
        return this.level != null;
    }

    public boolean setStrong() {
        return false;
    }

    public boolean setWeak() {
        return false;
    }

    @Override
    public Position getSide(BlockFace blockFace) {
        return this.getSide(blockFace, 1);
    }

    @Override
    public Position getSide(BlockFace blockFace, int n) {
        if (!this.isValid()) {
            throw new LevelException("Undefined Level reference");
        }
        return Position.fromObject(super.getSide(blockFace, n), this.level);
    }

    @Override
    public String toString() {
        return "Position(level=" + (this.isValid() ? this.level.getName() : "null") + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ')';
    }

    @Override
    public Position setComponents(double d2, double d3, double d4) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
        return this;
    }

    public Block getLevelBlock() {
        if (this.isValid()) {
            return this.level.getBlock(this);
        }
        throw new LevelException("Undefined Level reference");
    }

    public Location getLocation() {
        if (this.isValid()) {
            return new Location(this.x, this.y, this.z, 0.0, 0.0, this.level);
        }
        throw new LevelException("Undefined Level reference");
    }

    @Override
    public Position add(double d2) {
        return this.add(d2, 0.0, 0.0);
    }

    @Override
    public Position add(double d2, double d3) {
        return this.add(d2, d3, 0.0);
    }

    @Override
    public Position add(double d2, double d3, double d4) {
        return new Position(this.x + d2, this.y + d3, this.z + d4, this.level);
    }

    @Override
    public Position add(Vector3 vector3) {
        return new Position(this.x + vector3.getX(), this.y + vector3.getY(), this.z + vector3.getZ(), this.level);
    }

    @Override
    public Position subtract() {
        return this.subtract(0.0, 0.0, 0.0);
    }

    @Override
    public Position subtract(double d2) {
        return this.subtract(d2, 0.0, 0.0);
    }

    @Override
    public Position subtract(double d2, double d3) {
        return this.subtract(d2, d3, 0.0);
    }

    @Override
    public Position subtract(double d2, double d3, double d4) {
        return this.add(-d2, -d3, -d4);
    }

    @Override
    public Position subtract(Vector3 vector3) {
        return this.add(-vector3.getX(), -vector3.getY(), -vector3.getZ());
    }

    @Override
    public Position multiply(double d2) {
        return new Position(this.x * d2, this.y * d2, this.z * d2, this.level);
    }

    @Override
    public Position divide(double d2) {
        return new Position(this.x / d2, this.y / d2, this.z / d2, this.level);
    }

    @Override
    public Position ceil() {
        return new Position((int)Math.ceil(this.x), (int)Math.ceil(this.y), (int)Math.ceil(this.z), this.level);
    }

    @Override
    public Position floor() {
        return new Position(this.getFloorX(), this.getFloorY(), this.getFloorZ(), this.level);
    }

    @Override
    public Position round() {
        return new Position(Math.round(this.x), Math.round(this.y), Math.round(this.z), this.level);
    }

    @Override
    public Position abs() {
        return new Position((int)Math.abs(this.x), (int)Math.abs(this.y), (int)Math.abs(this.z), this.level);
    }

    @Override
    public Position clone() {
        return (Position)super.clone();
    }

    public FullChunk getChunk() {
        return this.isValid() ? this.level.getChunk(this.getChunkX(), this.getChunkZ()) : null;
    }

    private static LevelException b(LevelException levelException) {
        return levelException;
    }
}

