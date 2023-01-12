/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;

public enum BlockFace {
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, new Vector3(0.0, -1.0, 0.0)),
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, new Vector3(0.0, 1.0, 0.0)),
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, new Vector3(0.0, 0.0, -1.0)),
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, new Vector3(0.0, 0.0, 1.0)),
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, new Vector3(-1.0, 0.0, 0.0)),
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, new Vector3(1.0, 0.0, 0.0));

    private static final BlockFace[] d;
    private static final BlockFace[] i;
    private final int g;
    private final int h;
    private final int a;
    private final String c;
    private Axis f;
    private final AxisDirection e;
    private final Vector3 j;

    private BlockFace(int n2, int n3, int n4, String string2, AxisDirection axisDirection, Vector3 vector3) {
        this.g = n2;
        this.h = n3;
        this.a = n4;
        this.c = string2;
        this.e = axisDirection;
        this.j = vector3;
    }

    public static BlockFace fromIndex(int n) {
        return d[MathHelper.abs(n % d.length)];
    }

    public static BlockFace fromHorizontalIndex(int n) {
        return i[MathHelper.abs(n % i.length)];
    }

    public static BlockFace fromHorizontalAngle(double d2) {
        return BlockFace.fromHorizontalIndex(NukkitMath.floorDouble(d2 / 90.0 + 0.5) & 3);
    }

    public static BlockFace fromAxis(AxisDirection axisDirection, Axis axis) {
        for (BlockFace blockFace : d) {
            if (blockFace.e != axisDirection || blockFace.f != axis) continue;
            return blockFace;
        }
        throw new RuntimeException("Unable to get face from axis: " + (Object)((Object)axisDirection) + ' ' + axis);
    }

    public static BlockFace random(Random random) {
        return d[random.nextInt(d.length)];
    }

    public int getIndex() {
        return this.g;
    }

    public int getHorizontalIndex() {
        return this.a;
    }

    public float getHorizontalAngle() {
        return (this.a & 3) * 90;
    }

    public String getName() {
        return this.c;
    }

    public Axis getAxis() {
        return this.f;
    }

    public AxisDirection getAxisDirection() {
        return this.e;
    }

    public Vector3 getUnitVector() {
        return this.j;
    }

    public int getXOffset() {
        return this.f == Axis.X ? this.e.getOffset() : 0;
    }

    public int getYOffset() {
        return this.f == Axis.Y ? this.e.getOffset() : 0;
    }

    public int getZOffset() {
        return this.f == Axis.Z ? this.e.getOffset() : 0;
    }

    public BlockFace getOpposite() {
        return BlockFace.fromIndex(this.h);
    }

    public BlockFace rotateY() {
        switch (this) {
            case NORTH: {
                return EAST;
            }
            case EAST: {
                return SOUTH;
            }
            case SOUTH: {
                return WEST;
            }
            case WEST: {
                return NORTH;
            }
        }
        throw new RuntimeException("Unable to get Y-rotated face of " + (Object)((Object)this));
    }

    public BlockFace rotateYCCW() {
        switch (this) {
            case NORTH: {
                return WEST;
            }
            case EAST: {
                return NORTH;
            }
            case SOUTH: {
                return EAST;
            }
            case WEST: {
                return SOUTH;
            }
        }
        throw new RuntimeException("Unable to get counter-clockwise Y-rotated face of " + (Object)((Object)this));
    }

    public String toString() {
        return this.c;
    }

    static {
        d = new BlockFace[6];
        i = new BlockFace[4];
        BlockFace.DOWN.f = Axis.Y;
        BlockFace.UP.f = Axis.Y;
        BlockFace.NORTH.f = Axis.Z;
        BlockFace.SOUTH.f = Axis.Z;
        BlockFace.WEST.f = Axis.X;
        BlockFace.EAST.f = Axis.X;
        BlockFace[] blockFaceArray = BlockFace.values();
        int n = blockFaceArray.length;
        for (int k = 0; k < n; ++k) {
            BlockFace blockFace;
            BlockFace.d[blockFace.g] = blockFace = blockFaceArray[k];
            if (!blockFace.f.isHorizontal()) continue;
            BlockFace.i[blockFace.a] = blockFace;
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static enum Plane implements Predicate<BlockFace>,
    Iterable<BlockFace>
    {
        HORIZONTAL,
        VERTICAL;

        private BlockFace[] b;

        public BlockFace random(NukkitRandom nukkitRandom) {
            return this.b[nukkitRandom.nextBoundedInt(this.b.length)];
        }

        @Override
        public boolean test(BlockFace blockFace) {
            return blockFace != null && blockFace.getAxis().getPlane() == this;
        }

        @Override
        public Iterator<BlockFace> iterator() {
            return Iterators.forArray(this.b);
        }

        static {
            Plane.HORIZONTAL.b = new BlockFace[]{NORTH, EAST, SOUTH, WEST};
            Plane.VERTICAL.b = new BlockFace[]{UP, DOWN};
        }
    }

    public static enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int b;
        private final String a;

        private AxisDirection(int n2, String string2) {
            this.b = n2;
            this.a = string2;
        }

        public int getOffset() {
            return this.b;
        }

        public String toString() {
            return this.a;
        }
    }

    public static enum Axis implements Predicate<BlockFace>
    {
        X("x"),
        Y("y"),
        Z("z");

        private final String c;
        private Plane b;

        private Axis(String string2) {
            this.c = string2;
        }

        public boolean isVertical() {
            return this.b == Plane.VERTICAL;
        }

        public boolean isHorizontal() {
            return this.b == Plane.HORIZONTAL;
        }

        public Plane getPlane() {
            return this.b;
        }

        public String getName() {
            return this.c;
        }

        @Override
        public boolean test(BlockFace blockFace) {
            return blockFace != null && blockFace.getAxis() == this;
        }

        public String toString() {
            return this.c;
        }

        static {
            Axis.X.b = Plane.HORIZONTAL;
            Axis.Y.b = Plane.VERTICAL;
            Axis.Z.b = Plane.HORIZONTAL;
        }
    }
}

