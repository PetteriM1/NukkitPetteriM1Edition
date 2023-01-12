/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import java.util.Iterator;

public class BlockIterator
implements Iterator<Block> {
    private final int l;
    private static final int j = 0x1000000;
    private boolean m = false;
    private final Block[] d;
    private int o;
    private Block n = null;
    private int i;
    private final int e;
    private int c;
    private int a;
    private final int h;
    private final int g;
    private BlockFace b;
    private BlockFace f;
    private BlockFace k;

    public BlockIterator(Level level, Vector3 vector3, Vector3 vector32) {
        this(level, vector3, vector32, 0.0);
    }

    public BlockIterator(Level level, Vector3 vector3, Vector3 vector32, double d2) {
        this(level, vector3, vector32, d2, 0);
    }

    public BlockIterator(Level level, Vector3 vector3, Vector3 vector32, double d2, int n) {
        this.l = n;
        this.d = new Block[3];
        Vector3 vector33 = new Vector3(vector3.x, vector3.y, vector3.z);
        vector33.y += d2;
        this.i = 0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        Vector3 vector34 = new Vector3(vector33.x, vector33.y, vector33.z);
        Block block = level.getBlock(NukkitMath.floorDouble(vector34.x), NukkitMath.floorDouble(vector34.y), NukkitMath.floorDouble(vector34.z));
        if (this.e(vector32) > d3) {
            this.b = this.c(vector32);
            d3 = this.e(vector32);
            d6 = this.a(vector32, vector33, block);
            this.f = this.a(vector32);
            d4 = this.b(vector32);
            d7 = this.b(vector32, vector33, block);
            this.k = this.f(vector32);
            d5 = this.d(vector32);
            d8 = this.c(vector32, vector33, block);
        }
        if (this.b(vector32) > d3) {
            this.b = this.a(vector32);
            d3 = this.b(vector32);
            d6 = this.b(vector32, vector33, block);
            this.f = this.f(vector32);
            d4 = this.d(vector32);
            d7 = this.c(vector32, vector33, block);
            this.k = this.c(vector32);
            d5 = this.e(vector32);
            d8 = this.a(vector32, vector33, block);
        }
        if (this.d(vector32) > d3) {
            this.b = this.f(vector32);
            d3 = this.d(vector32);
            d6 = this.c(vector32, vector33, block);
            this.f = this.c(vector32);
            d4 = this.e(vector32);
            d7 = this.a(vector32, vector33, block);
            this.k = this.a(vector32);
            d5 = this.b(vector32);
            d8 = this.b(vector32, vector33, block);
        }
        double d9 = d6 / d3;
        double d10 = d7 - d4 * d9;
        double d11 = d8 - d5 * d9;
        this.c = (int)Math.floor(d10 * 1.6777216E7);
        this.h = (int)Math.round(d4 / d3 * 1.6777216E7);
        this.a = (int)Math.floor(d11 * 1.6777216E7);
        this.g = (int)Math.round(d5 / d3 * 1.6777216E7);
        if (this.c + this.h <= 0) {
            this.c = -this.h + 1;
        }
        if (this.a + this.g <= 0) {
            this.a = -this.g + 1;
        }
        Block block2 = block.getSide(this.b.getOpposite());
        if (this.c < 0) {
            this.c += 0x1000000;
            block2 = block2.getSide(this.f.getOpposite());
        }
        if (this.a < 0) {
            this.a += 0x1000000;
            block2 = block2.getSide(this.k.getOpposite());
        }
        this.c -= 0x1000000;
        this.a -= 0x1000000;
        this.d[0] = block2;
        this.o = -1;
        this.a();
        boolean bl = false;
        for (int k = this.o; k >= 0; --k) {
            if (!this.a(this.d[k], block)) continue;
            this.o = k;
            bl = true;
            break;
        }
        if (!bl) {
            throw new IllegalStateException("Start block missed in BlockIterator");
        }
        this.e = (int)Math.round((double)n / (Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / d3));
    }

    private boolean a(Block block, Block block2) {
        return block.x == block2.x && block.y == block2.y && block.z == block2.z;
    }

    private BlockFace c(Vector3 vector3) {
        return vector3.x > 0.0 ? BlockFace.EAST : BlockFace.WEST;
    }

    private BlockFace a(Vector3 vector3) {
        return vector3.y > 0.0 ? BlockFace.UP : BlockFace.DOWN;
    }

    private BlockFace f(Vector3 vector3) {
        return vector3.z > 0.0 ? BlockFace.SOUTH : BlockFace.NORTH;
    }

    private double e(Vector3 vector3) {
        return Math.abs(vector3.x);
    }

    private double b(Vector3 vector3) {
        return Math.abs(vector3.y);
    }

    private double d(Vector3 vector3) {
        return Math.abs(vector3.z);
    }

    private double a(double d2, double d3, double d4) {
        return d2 > 0.0 ? d3 - d4 : d4 + 1.0 - d3;
    }

    private double a(Vector3 vector3, Vector3 vector32, Block block) {
        return this.a(vector3.x, vector32.x, block.x);
    }

    private double b(Vector3 vector3, Vector3 vector32, Block block) {
        return this.a(vector3.y, vector32.y, block.y);
    }

    private double c(Vector3 vector3, Vector3 vector32, Block block) {
        return this.a(vector3.z, vector32.z, block.z);
    }

    @Override
    public Block next() {
        this.a();
        if (this.o <= -1) {
            throw new IndexOutOfBoundsException();
        }
        this.n = this.d[this.o--];
        return this.n;
    }

    @Override
    public boolean hasNext() {
        this.a();
        return this.o != -1;
    }

    private void a() {
        if (this.o >= 0) {
            return;
        }
        if (this.l != 0 && this.i > this.e) {
            this.m = true;
            return;
        }
        if (this.m) {
            return;
        }
        ++this.i;
        this.c += this.h;
        this.a += this.g;
        if (this.c > 0 && this.a > 0) {
            this.d[2] = this.d[0].getSide(this.b);
            if (this.h * this.a < this.g * this.c) {
                this.d[1] = this.d[2].getSide(this.f);
                this.d[0] = this.d[1].getSide(this.k);
            } else {
                this.d[1] = this.d[2].getSide(this.k);
                this.d[0] = this.d[1].getSide(this.f);
            }
            this.a -= 0x1000000;
            this.c -= 0x1000000;
            this.o = 2;
        } else if (this.c > 0) {
            this.d[1] = this.d[0].getSide(this.b);
            this.d[0] = this.d[1].getSide(this.f);
            this.c -= 0x1000000;
            this.o = 1;
        } else if (this.a > 0) {
            this.d[1] = this.d[0].getSide(this.b);
            this.d[0] = this.d[1].getSide(this.k);
            this.a -= 0x1000000;
            this.o = 1;
        } else {
            this.d[0] = this.d[0].getSide(this.b);
            this.o = 0;
        }
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

