/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.block.Block;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.d;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class Rail {
    public static boolean isRailBlock(Block block) {
        Objects.requireNonNull(block, "Rail block predicate can not accept null block");
        return Rail.isRailBlock(block.getId());
    }

    public static boolean isRailBlock(int n) {
        switch (n) {
            case 27: 
            case 28: 
            case 66: 
            case 126: {
                return true;
            }
        }
        return false;
    }

    private Rail() {
    }

    public static enum Orientation {
        STRAIGHT_NORTH_SOUTH(0, State.STRAIGHT, BlockFace.NORTH, BlockFace.SOUTH, null),
        STRAIGHT_EAST_WEST(1, State.STRAIGHT, BlockFace.EAST, BlockFace.WEST, null),
        ASCENDING_EAST(2, State.ASCENDING, BlockFace.EAST, BlockFace.WEST, BlockFace.EAST),
        ASCENDING_WEST(3, State.ASCENDING, BlockFace.EAST, BlockFace.WEST, BlockFace.WEST),
        ASCENDING_NORTH(4, State.ASCENDING, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.NORTH),
        ASCENDING_SOUTH(5, State.ASCENDING, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SOUTH),
        CURVED_SOUTH_EAST(6, State.CURVED, BlockFace.SOUTH, BlockFace.EAST, null),
        CURVED_SOUTH_WEST(7, State.CURVED, BlockFace.SOUTH, BlockFace.WEST, null),
        CURVED_NORTH_WEST(8, State.CURVED, BlockFace.NORTH, BlockFace.WEST, null),
        CURVED_NORTH_EAST(9, State.CURVED, BlockFace.NORTH, BlockFace.EAST, null);

        private static final Orientation[] f;
        private final int b;
        private final State d;
        private final List<BlockFace> a;
        private final BlockFace e;

        private Orientation(int n2, State state, BlockFace blockFace, BlockFace blockFace2, BlockFace blockFace3) {
            this.b = n2;
            this.d = state;
            this.a = Arrays.asList(blockFace, blockFace2);
            this.e = blockFace3;
        }

        public static Orientation byMetadata(int n) {
            if (n < 0 || n >= f.length) {
                n = 0;
            }
            return f[n];
        }

        public static Orientation straight(BlockFace blockFace) {
            switch (cn.nukkit.utils.d.a[blockFace.ordinal()]) {
                case 1: 
                case 2: {
                    return STRAIGHT_NORTH_SOUTH;
                }
                case 3: 
                case 4: {
                    return STRAIGHT_EAST_WEST;
                }
            }
            return STRAIGHT_NORTH_SOUTH;
        }

        public static Orientation ascending(BlockFace blockFace) {
            switch (cn.nukkit.utils.d.a[blockFace.ordinal()]) {
                case 1: {
                    return ASCENDING_NORTH;
                }
                case 2: {
                    return ASCENDING_SOUTH;
                }
                case 3: {
                    return ASCENDING_EAST;
                }
                case 4: {
                    return ASCENDING_WEST;
                }
            }
            return ASCENDING_EAST;
        }

        public static Orientation curved(BlockFace blockFace, BlockFace blockFace2) {
            for (Orientation orientation : new Orientation[]{CURVED_SOUTH_EAST, CURVED_SOUTH_WEST, CURVED_NORTH_WEST, CURVED_NORTH_EAST}) {
                if (!orientation.a.contains((Object)blockFace) || !orientation.a.contains((Object)blockFace2)) continue;
                return orientation;
            }
            return CURVED_SOUTH_EAST;
        }

        public static Orientation straightOrCurved(BlockFace blockFace, BlockFace blockFace2) {
            for (Orientation orientation : new Orientation[]{STRAIGHT_NORTH_SOUTH, STRAIGHT_EAST_WEST, CURVED_SOUTH_EAST, CURVED_SOUTH_WEST, CURVED_NORTH_WEST, CURVED_NORTH_EAST}) {
                if (!orientation.a.contains((Object)blockFace) || !orientation.a.contains((Object)blockFace2)) continue;
                return orientation;
            }
            return STRAIGHT_NORTH_SOUTH;
        }

        public int metadata() {
            return this.b;
        }

        public boolean hasConnectingDirections(BlockFace ... blockFaceArray) {
            return Stream.of(blockFaceArray).allMatch(this.a::contains);
        }

        public List<BlockFace> connectingDirections() {
            return this.a;
        }

        public Optional<BlockFace> ascendingDirection() {
            return Optional.ofNullable(this.e);
        }

        public boolean isStraight() {
            return this.d == State.STRAIGHT;
        }

        public boolean isAscending() {
            return this.d == State.ASCENDING;
        }

        public boolean isCurved() {
            return this.d == State.CURVED;
        }

        static {
            f = new Orientation[Orientation.values().length];
            Orientation[] orientationArray = Orientation.values();
            int n = orientationArray.length;
            for (int k = 0; k < n; ++k) {
                Orientation orientation;
                Orientation.f[orientation.b] = orientation = orientationArray[k];
            }
        }

        public static enum State {
            STRAIGHT,
            ASCENDING,
            CURVED;

        }
    }
}

