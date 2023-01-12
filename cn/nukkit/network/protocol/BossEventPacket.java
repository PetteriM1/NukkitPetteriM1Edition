/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class BossEventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 74;
    public static final int TYPE_SHOW = 0;
    public static final int TYPE_REGISTER_PLAYER = 1;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_HIDE = 2;
    public static final int TYPE_UNREGISTER_PLAYER = 3;
    public static final int TYPE_HEALTH_PERCENT = 4;
    public static final int TYPE_TITLE = 5;
    public static final int TYPE_UPDATE_PROPERTIES = 6;
    public static final int TYPE_TEXTURE = 7;
    public static final int TYPE_QUERY = 8;
    public long bossEid;
    public int type;
    public long playerEid;
    public float healthPercent;
    public String title = "";
    public short unknown;
    public int color;
    public int overlay;

    @Override
    public byte pid() {
        return 74;
    }

    @Override
    public void decode() {
        this.bossEid = this.getEntityUniqueId();
        this.type = (int)this.getUnsignedVarInt();
        switch (this.type) {
            case 1: 
            case 3: 
            case 8: {
                this.playerEid = this.getEntityUniqueId();
                break;
            }
            case 0: {
                this.title = this.getString();
                this.healthPercent = this.getLFloat();
            }
            case 6: {
                this.unknown = (short)this.getShort();
            }
            case 7: {
                this.color = (int)this.getUnsignedVarInt();
                this.overlay = (int)this.getUnsignedVarInt();
                break;
            }
            case 4: {
                this.healthPercent = this.getLFloat();
                break;
            }
            case 5: {
                this.title = this.getString();
            }
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.bossEid);
        this.putUnsignedVarInt(this.type);
        switch (this.type) {
            case 1: 
            case 3: 
            case 8: {
                this.putEntityUniqueId(this.playerEid);
                break;
            }
            case 0: {
                this.putString(this.title);
                this.putLFloat(this.healthPercent);
            }
            case 6: {
                this.putShort(this.unknown);
            }
            case 7: {
                this.putUnsignedVarInt(this.color);
                this.putUnsignedVarInt(this.overlay);
                break;
            }
            case 4: {
                this.putLFloat(this.healthPercent);
                break;
            }
            case 5: {
                this.putString(this.title);
            }
        }
    }

    public String toString() {
        return "BossEventPacket(bossEid=" + this.bossEid + ", type=" + this.type + ", playerEid=" + this.playerEid + ", healthPercent=" + this.healthPercent + ", title=" + this.title + ", unknown=" + this.unknown + ", color=" + this.color + ", overlay=" + this.overlay + ")";
    }
}

