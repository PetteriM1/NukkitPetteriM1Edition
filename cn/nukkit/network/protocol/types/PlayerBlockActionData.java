/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.types.PlayerActionType;

public class PlayerBlockActionData {
    private PlayerActionType a;
    private BlockVector3 b;
    private int c;

    public PlayerActionType getAction() {
        return this.a;
    }

    public BlockVector3 getPosition() {
        return this.b;
    }

    public int getFacing() {
        return this.c;
    }

    public void setAction(PlayerActionType playerActionType) {
        this.a = playerActionType;
    }

    public void setPosition(BlockVector3 blockVector3) {
        this.b = blockVector3;
    }

    public void setFacing(int n) {
        this.c = n;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof PlayerBlockActionData)) {
            return false;
        }
        PlayerBlockActionData playerBlockActionData = (PlayerBlockActionData)object;
        if (!playerBlockActionData.canEqual(this)) {
            return false;
        }
        PlayerActionType playerActionType = this.getAction();
        PlayerActionType playerActionType2 = playerBlockActionData.getAction();
        if (playerActionType == null ? playerActionType2 != null : !((Object)((Object)playerActionType)).equals((Object)playerActionType2)) {
            return false;
        }
        BlockVector3 blockVector3 = this.getPosition();
        BlockVector3 blockVector32 = playerBlockActionData.getPosition();
        if (blockVector3 == null ? blockVector32 != null : !((Object)blockVector3).equals(blockVector32)) {
            return false;
        }
        return this.getFacing() == playerBlockActionData.getFacing();
    }

    protected boolean canEqual(Object object) {
        return object instanceof PlayerBlockActionData;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        PlayerActionType playerActionType = this.getAction();
        n2 = n2 * 59 + (playerActionType == null ? 43 : ((Object)((Object)playerActionType)).hashCode());
        BlockVector3 blockVector3 = this.getPosition();
        n2 = n2 * 59 + (blockVector3 == null ? 43 : ((Object)blockVector3).hashCode());
        n2 = n2 * 59 + this.getFacing();
        return n2;
    }

    public String toString() {
        return "PlayerBlockActionData(action=" + (Object)((Object)this.getAction()) + ", position=" + this.getPosition() + ", facing=" + this.getFacing() + ")";
    }

    public PlayerBlockActionData(PlayerActionType playerActionType, BlockVector3 blockVector3, int n) {
        this.a = playerActionType;
        this.b = blockVector3;
        this.c = n;
    }
}

