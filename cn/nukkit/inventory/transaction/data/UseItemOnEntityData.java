/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.data;

import cn.nukkit.inventory.transaction.data.TransactionData;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class UseItemOnEntityData
implements TransactionData {
    public long entityRuntimeId;
    public int actionType;
    public int hotbarSlot;
    public Item itemInHand;
    public Vector3 playerPos;
    public Vector3 clickPos;
}

