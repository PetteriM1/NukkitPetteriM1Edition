/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;

public class ChunkPosition {
    public final int x;
    public final int y;
    public final int z;

    public ChunkPosition(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
    }

    public ChunkPosition(Vector3 vector3) {
        this(MathHelper.floor(vector3.x), MathHelper.floor(vector3.y), MathHelper.floor(vector3.z));
    }

    public boolean equals(Object object) {
        if (!(object instanceof ChunkPosition)) {
            return false;
        }
        ChunkPosition chunkPosition = (ChunkPosition)object;
        return chunkPosition.x == this.x && chunkPosition.y == this.y && chunkPosition.z == this.z;
    }

    public int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}

