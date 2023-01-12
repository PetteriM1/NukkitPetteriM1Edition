/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class PlayerActionPacket
extends DataPacket {
    public static final byte NETWORK_ID = 36;
    public static final int ACTION_START_BREAK = 0;
    public static final int ACTION_ABORT_BREAK = 1;
    public static final int ACTION_STOP_BREAK = 2;
    public static final int ACTION_GET_UPDATED_BLOCK = 3;
    public static final int ACTION_DROP_ITEM = 4;
    public static final int ACTION_START_SLEEPING = 5;
    public static final int ACTION_STOP_SLEEPING = 6;
    public static final int ACTION_RESPAWN = 7;
    public static final int ACTION_JUMP = 8;
    public static final int ACTION_START_SPRINT = 9;
    public static final int ACTION_STOP_SPRINT = 10;
    public static final int ACTION_START_SNEAK = 11;
    public static final int ACTION_STOP_SNEAK = 12;
    public static final int ACTION_DIMENSION_CHANGE_REQUEST = 13;
    public static final int ACTION_CREATIVE_PLAYER_DESTROY_BLOCK = 13;
    public static final int ACTION_DIMENSION_CHANGE_ACK = 14;
    public static final int ACTION_START_GLIDE = 15;
    public static final int ACTION_STOP_GLIDE = 16;
    public static final int ACTION_BUILD_DENIED = 17;
    public static final int ACTION_CONTINUE_BREAK = 18;
    public static final int ACTION_CHANGE_SKIN = 19;
    public static final int ACTION_SET_ENCHANTMENT_SEED = 20;
    public static final int ACTION_START_SWIMMING = 21;
    public static final int ACTION_STOP_SWIMMING = 22;
    public static final int ACTION_START_SPIN_ATTACK = 23;
    public static final int ACTION_STOP_SPIN_ATTACK = 24;
    public static final int ACTION_INTERACT_BLOCK = 25;
    public static final int ACTION_PREDICT_DESTROY_BLOCK = 26;
    public static final int ACTION_CONTINUE_DESTROY_BLOCK = 27;
    public static final int ACTION_START_ITEM_USE_ON = 28;
    public static final int ACTION_STOP_ITEM_USE_ON = 29;
    public long entityId;
    public int action;
    public int x;
    public int y;
    public int z;
    public BlockVector3 resultPosition;
    public int face;

    @Override
    public void decode() {
        this.entityId = this.getEntityRuntimeId();
        this.action = this.getVarInt();
        BlockVector3 blockVector3 = this.getBlockVector3();
        this.x = blockVector3.x;
        this.y = blockVector3.y;
        this.z = blockVector3.z;
        if (this.protocol >= 524) {
            this.resultPosition = this.getBlockVector3();
        }
        this.face = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityId);
        this.putVarInt(this.action);
        this.putBlockVector3(this.x, this.y, this.z);
        if (this.protocol >= 524) {
            if (this.resultPosition == null) {
                this.putBlockVector3(new BlockVector3());
            } else {
                this.putBlockVector3(this.resultPosition);
            }
        }
        this.putVarInt(this.face);
    }

    @Override
    public byte pid() {
        return 36;
    }

    public String toString() {
        return "PlayerActionPacket(entityId=" + this.entityId + ", action=" + this.action + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", resultPosition=" + this.resultPosition + ", face=" + this.face + ")";
    }
}

