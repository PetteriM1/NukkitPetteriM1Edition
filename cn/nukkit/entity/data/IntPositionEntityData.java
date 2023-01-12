/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.EntityData;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;

public class IntPositionEntityData
extends EntityData<BlockVector3> {
    public int x;
    public int y;
    public int z;

    public IntPositionEntityData(int n, int n2, int n3, int n4) {
        super(n);
        this.x = n2;
        this.y = n3;
        this.z = n4;
    }

    public IntPositionEntityData(int n, Vector3 vector3) {
        this(n, (int)vector3.x, (int)vector3.y, (int)vector3.z);
    }

    @Override
    public BlockVector3 getData() {
        return new BlockVector3(this.x, this.y, this.z);
    }

    @Override
    public void setData(BlockVector3 blockVector3) {
        if (blockVector3 != null) {
            this.x = blockVector3.x;
            this.y = blockVector3.y;
            this.z = blockVector3.z;
        }
    }

    @Override
    public int getType() {
        return 6;
    }
}

