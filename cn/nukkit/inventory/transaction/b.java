/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.scheduler.Task;

class b
extends Task {
    final ContainerClosePacket val$pk;
    final CraftingTransaction this$0;

    b(CraftingTransaction craftingTransaction, ContainerClosePacket containerClosePacket) {
        this.this$0 = craftingTransaction;
        this.val$pk = containerClosePacket;
    }

    @Override
    public void onRun(int n) {
        this.this$0.source.dataPacket(this.val$pk);
    }
}

