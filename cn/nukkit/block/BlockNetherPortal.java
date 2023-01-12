/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockNetherPortal
extends BlockFlowable
implements Faceable {
    public BlockNetherPortal() {
        this(0);
    }

    public BlockNetherPortal(int n) {
        super(0);
    }

    @Override
    public String getName() {
        return "Nether Portal Block";
    }

    @Override
    public int getId() {
        return 90;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public int getLightLevel() {
        return 11;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(0));
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        boolean bl = super.onBreak(item);
        for (BlockFace blockFace : BlockFace.values()) {
            Block block = this.getSide(blockFace);
            if (!(block instanceof BlockNetherPortal)) continue;
            bl &= block.onBreak(item);
        }
        return bl;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    public static boolean trySpawnPortal(Level level, Vector3 vector3) {
        return BlockNetherPortal.trySpawnPortal(level, vector3, false);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    public static boolean trySpawnPortal(Level level, Vector3 vector3, boolean bl) {
        PortalBuilder portalBuilder = new PortalBuilder(level, vector3, BlockFace.Axis.X, bl);
        if (portalBuilder.isValid() && portalBuilder.c == 0) {
            portalBuilder.placePortalBlocks();
            return true;
        }
        portalBuilder = new PortalBuilder(level, vector3, BlockFace.Axis.Z, bl);
        if (portalBuilder.isValid() && portalBuilder.c == 0) {
            portalBuilder.placePortalBlocks();
            return true;
        }
        return false;
    }

    public static Position getSafePortal(Position position) {
        Level level = position.getLevel();
        FullChunk fullChunk = position.getChunk();
        Vector3 vector3 = position.down();
        while (level.getBlockIdAt(fullChunk, vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ()) == 90) {
            vector3 = vector3.down();
        }
        return Position.fromObject(vector3.up(), position.getLevel());
    }

    public static Position findNearestPortal(Position position, boolean bl) {
        int n;
        int n2;
        int n3;
        int n4;
        Level level = position.getLevel();
        Vector3 vector3 = null;
        int n5 = bl ? 127 : 255;
        for (int k = -16; k <= 16; ++k) {
            block1: for (n4 = -16; n4 <= 16; ++n4) {
                for (n3 = 0; n3 < n5; ++n3) {
                    n2 = position.getFloorX() + k;
                    if (level.getBlockIdAt(n2, n3, n = position.getFloorZ() + n4) != 90) continue;
                    vector3 = new Position(n2, n3, n, level);
                    continue block1;
                }
            }
        }
        if (vector3 == null) {
            return null;
        }
        Vector3 vector32 = vector3.up();
        n4 = vector32.getFloorX();
        n = level.getBlockIdAt(n4, n3 = vector32.getFloorY(), n2 = vector32.getFloorZ());
        if (n != 0 && n != 49 && n != 90) {
            for (int k = -1; k < 4; ++k) {
                for (int i2 = 1; i2 < 4; ++i2) {
                    for (int i3 = -1; i3 < 3; ++i3) {
                        level.setBlockAt(n4 + k, n3 + i2, n2 + i3, 0);
                    }
                }
            }
        }
        return vector3;
    }

    public static void spawnPortal(Position position) {
        Level level = position.level;
        int n = position.getFloorX();
        int n2 = position.getFloorY();
        int n3 = position.getFloorZ();
        for (int k = -1; k < 4; ++k) {
            for (int i2 = 1; i2 < 4; ++i2) {
                for (int i3 = -1; i3 < 3; ++i3) {
                    level.setBlockAt(n + k, n2 + i2, n3 + i3, 0);
                }
            }
        }
        level.setBlockAt(n + 1, n2, n3, 49);
        level.setBlockAt(n + 2, n2, n3, 49);
        level.setBlockAt(n, n2, ++n3, 49);
        level.setBlockAt(n + 1, n2, n3, 49);
        level.setBlockAt(n + 2, n2, n3, 49);
        level.setBlockAt(n + 3, n2, n3, 49);
        level.setBlockAt(n + 1, n2, ++n3, 49);
        level.setBlockAt(n + 2, n2, n3, 49);
        level.setBlockAt(n, ++n2, --n3, 49);
        level.setBlockAt(n + 1, n2, n3, 90);
        level.setBlockAt(n + 2, n2, n3, 90);
        level.setBlockAt(n + 3, n2, n3, 49);
        level.setBlockAt(n, ++n2, n3, 49);
        level.setBlockAt(n + 1, n2, n3, 90);
        level.setBlockAt(n + 2, n2, n3, 90);
        level.setBlockAt(n + 3, n2, n3, 49);
        level.setBlockAt(n, ++n2, n3, 49);
        level.setBlockAt(n + 1, n2, n3, 90);
        level.setBlockAt(n + 2, n2, n3, 90);
        level.setBlockAt(n + 3, n2, n3, 49);
        level.setBlockAt(n, ++n2, n3, 49);
        level.setBlockAt(n + 1, n2, n3, 49);
        level.setBlockAt(n + 2, n2, n3, 49);
        level.setBlockAt(n + 3, n2, n3, 49);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    public static class PortalBuilder {
        private final Level a;
        private final BlockFace.Axis f;
        private final BlockFace i;
        private final BlockFace g;
        private int c;
        private Vector3 h;
        private int d;
        private int b;
        private final boolean e;

        public PortalBuilder(Level level, Vector3 vector3, BlockFace.Axis axis, boolean bl) {
            this.a = level;
            this.f = axis;
            this.e = bl;
            if (axis == BlockFace.Axis.X) {
                this.g = BlockFace.EAST;
                this.i = BlockFace.WEST;
            } else {
                this.g = BlockFace.NORTH;
                this.i = BlockFace.SOUTH;
            }
            Vector3 vector32 = vector3;
            while (vector3.getY() > vector32.getY() - 21.0 && vector3.getY() > 0.0 && this.isEmptyBlock(this.a(vector3.getSideVec(BlockFace.DOWN)))) {
                vector3 = vector3.down();
            }
            int n = this.getDistanceUntilEdge(vector3, this.g) - 1;
            if (n >= 0) {
                this.h = vector3.getSide(this.g, n);
                this.b = this.getDistanceUntilEdge(this.h, this.i);
                if (this.b < 2 || this.b > 21) {
                    this.h = null;
                    this.b = 0;
                }
            }
            if (this.h != null) {
                this.d = this.calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(Vector3 vector3, BlockFace blockFace) {
            Vector3 vector32;
            int n;
            for (n = 0; n < 22 && this.isEmptyBlock(this.a(vector32 = vector3.getSide(blockFace, n))) && this.a(vector32.getSideVec(BlockFace.DOWN)) == 49; ++n) {
            }
            return this.a(vector3.getSide(blockFace, n)) == 49 ? n : 0;
        }

        public int getHeight() {
            return this.d;
        }

        public int getWidth() {
            return this.b;
        }

        protected int calculatePortalHeight() {
            int n;
            this.d = 0;
            block0: while (this.d < 21) {
                for (n = 0; n < this.b; ++n) {
                    Vector3 vector3 = this.h.getSide(this.i, n).up(this.d);
                    int n2 = this.a(vector3);
                    if (!this.isEmptyBlock(n2)) break block0;
                    if (n2 == 90) {
                        ++this.c;
                    }
                    if (n == 0 ? (n2 = this.a(vector3.getSideVec(this.g))) != 49 : n == this.b - 1 && (n2 = this.a(vector3.getSideVec(this.i))) != 49) break block0;
                }
                ++this.d;
            }
            for (n = 0; n < this.b; ++n) {
                if (this.a(this.h.getSide(this.i, n).up(this.d)) == 49) continue;
                this.d = 0;
                break;
            }
            if (this.d <= 21 && this.d >= 3) {
                return this.d;
            }
            this.h = null;
            this.b = 0;
            this.d = 0;
            return 0;
        }

        private int a(Vector3 vector3) {
            return this.a.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
        }

        protected boolean isEmptyBlock(int n) {
            return this.e || n == 0 || n == 51 || n == 90;
        }

        public boolean isValid() {
            return this.h != null && this.b >= 2 && this.b <= 21 && this.d >= 3 && this.d <= 21;
        }

        public void placePortalBlocks() {
            for (int k = 0; k < this.b; ++k) {
                Vector3 vector3 = this.h.getSide(this.i, k);
                for (int i2 = 0; i2 < this.d; ++i2) {
                    this.a.setBlock(vector3.up(i2), Block.get(90, this.f == BlockFace.Axis.X ? 1 : (this.f == BlockFace.Axis.Z ? 2 : 0)));
                }
            }
        }
    }
}

