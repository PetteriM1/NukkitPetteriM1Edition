/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.inventory.transaction.data.ReleaseItemData;
import cn.nukkit.inventory.transaction.data.TransactionData;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.types.NetworkInventoryAction;
import java.util.Arrays;

public class InventoryTransactionPacket
extends DataPacket {
    public static final byte NETWORK_ID = 30;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MISMATCH = 1;
    public static final int TYPE_USE_ITEM = 2;
    public static final int TYPE_USE_ITEM_ON_ENTITY = 3;
    public static final int TYPE_RELEASE_ITEM = 4;
    public static final int USE_ITEM_ACTION_CLICK_BLOCK = 0;
    public static final int USE_ITEM_ACTION_CLICK_AIR = 1;
    public static final int USE_ITEM_ACTION_BREAK_BLOCK = 2;
    public static final int RELEASE_ITEM_ACTION_RELEASE = 0;
    public static final int RELEASE_ITEM_ACTION_CONSUME = 1;
    public static final int USE_ITEM_ON_ENTITY_ACTION_INTERACT = 0;
    public static final int USE_ITEM_ON_ENTITY_ACTION_ATTACK = 1;
    public static final int ACTION_MAGIC_SLOT_DROP_ITEM = 0;
    public static final int ACTION_MAGIC_SLOT_PICKUP_ITEM = 1;
    public static final int ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM = 0;
    public static final int ACTION_MAGIC_SLOT_CREATIVE_CREATE_ITEM = 1;
    public int transactionType;
    public NetworkInventoryAction[] actions;
    public TransactionData transactionData;
    public boolean hasNetworkIds = false;
    public int legacyRequestId;
    public boolean isCraftingPart = false;
    public boolean isEnchantingPart = false;
    public boolean isRepairItemPart = false;

    @Override
    public byte pid() {
        return 30;
    }

    @Override
    public void encode() {
        this.reset();
        if (this.protocol >= 407) {
            this.putVarInt(this.legacyRequestId);
            if (this.legacyRequestId <= 0 || this.protocol >= 407) {
                // empty if block
            }
        }
        this.putUnsignedVarInt(this.transactionType);
        if (this.protocol >= 407) {
            this.putBoolean(this.hasNetworkIds);
        }
        this.putUnsignedVarInt(this.actions.length);
        for (NetworkInventoryAction networkInventoryAction : this.actions) {
            networkInventoryAction.write(this);
        }
        switch (this.transactionType) {
            case 0: 
            case 1: {
                break;
            }
            case 2: {
                UseItemData useItemData = (UseItemData)this.transactionData;
                this.putUnsignedVarInt(useItemData.actionType);
                this.putBlockVector3(useItemData.blockPos);
                this.putBlockFace(useItemData.face);
                this.putVarInt(useItemData.hotbarSlot);
                this.putSlot(this.protocol, useItemData.itemInHand);
                this.putVector3f(useItemData.playerPos.asVector3f());
                this.putVector3f(useItemData.clickPos);
                if (this.protocol < 340) break;
                this.putUnsignedVarInt(useItemData.blockRuntimeId);
                break;
            }
            case 3: {
                UseItemOnEntityData useItemOnEntityData = (UseItemOnEntityData)this.transactionData;
                this.putEntityRuntimeId(useItemOnEntityData.entityRuntimeId);
                this.putUnsignedVarInt(useItemOnEntityData.actionType);
                this.putVarInt(useItemOnEntityData.hotbarSlot);
                this.putSlot(this.protocol, useItemOnEntityData.itemInHand);
                this.putVector3f(useItemOnEntityData.playerPos.asVector3f());
                this.putVector3f(useItemOnEntityData.clickPos.asVector3f());
                break;
            }
            case 4: {
                ReleaseItemData releaseItemData = (ReleaseItemData)this.transactionData;
                this.putUnsignedVarInt(releaseItemData.actionType);
                this.putVarInt(releaseItemData.hotbarSlot);
                this.putSlot(this.protocol, releaseItemData.itemInHand);
                this.putVector3f(releaseItemData.headRot.asVector3f());
                break;
            }
            default: {
                throw new RuntimeException("Unknown transaction type " + this.transactionType);
            }
        }
    }

    @Override
    public void decode() {
        int n;
        if (this.protocol >= 407) {
            this.legacyRequestId = this.getVarInt();
            if (this.legacyRequestId < -1 && (this.legacyRequestId & 1) == 0) {
                n = (int)this.getUnsignedVarInt();
                for (int k = 0; k < n; ++k) {
                    this.getByte();
                    int n2 = (int)this.getUnsignedVarInt();
                    this.get(n2);
                }
            }
        }
        this.transactionType = (int)this.getUnsignedVarInt();
        if (this.protocol >= 407 && this.protocol < 431) {
            this.hasNetworkIds = this.getBoolean();
        }
        this.actions = new NetworkInventoryAction[Math.min((int)this.getUnsignedVarInt(), 4096)];
        for (n = 0; n < this.actions.length; ++n) {
            this.actions[n] = new NetworkInventoryAction().read(this);
        }
        switch (this.transactionType) {
            case 0: 
            case 1: {
                break;
            }
            case 2: {
                UseItemData useItemData = new UseItemData();
                useItemData.actionType = (int)this.getUnsignedVarInt();
                useItemData.blockPos = this.getBlockVector3();
                useItemData.face = this.getBlockFace();
                useItemData.hotbarSlot = this.getVarInt();
                useItemData.itemInHand = this.getSlot(this.protocol);
                useItemData.playerPos = this.getVector3f().asVector3();
                useItemData.clickPos = this.getVector3f();
                if (this.protocol >= 340) {
                    useItemData.blockRuntimeId = (int)this.getUnsignedVarInt();
                }
                this.transactionData = useItemData;
                break;
            }
            case 3: {
                UseItemOnEntityData useItemOnEntityData = new UseItemOnEntityData();
                useItemOnEntityData.entityRuntimeId = this.getEntityRuntimeId();
                useItemOnEntityData.actionType = (int)this.getUnsignedVarInt();
                useItemOnEntityData.hotbarSlot = this.getVarInt();
                useItemOnEntityData.itemInHand = this.getSlot(this.protocol);
                useItemOnEntityData.playerPos = this.getVector3f().asVector3();
                useItemOnEntityData.clickPos = this.getVector3f().asVector3();
                this.transactionData = useItemOnEntityData;
                break;
            }
            case 4: {
                ReleaseItemData releaseItemData = new ReleaseItemData();
                releaseItemData.actionType = (int)this.getUnsignedVarInt();
                releaseItemData.hotbarSlot = this.getVarInt();
                releaseItemData.itemInHand = this.getSlot(this.protocol);
                releaseItemData.headRot = this.getVector3f().asVector3();
                this.transactionData = releaseItemData;
                break;
            }
            default: {
                throw new RuntimeException("Unknown transaction type " + this.transactionType);
            }
        }
    }

    public String toString() {
        return "InventoryTransactionPacket(transactionType=" + this.transactionType + ", actions=" + Arrays.deepToString(this.actions) + ", transactionData=" + this.transactionData + ", hasNetworkIds=" + this.hasNetworkIds + ", legacyRequestId=" + this.legacyRequestId + ", isCraftingPart=" + this.isCraftingPart + ", isEnchantingPart=" + this.isEnchantingPart + ", isRepairItemPart=" + this.isRepairItemPart + ")";
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

