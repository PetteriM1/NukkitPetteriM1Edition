/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Server;
import cn.nukkit.event.server.PlayerDataSerializeEvent;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;

class g
extends Task {
    boolean b;
    final PlayerDataSerializeEvent val$event;
    final CompoundTag val$tag;
    final String val$nameLower;
    final Server this$0;

    g(Server server, PlayerDataSerializeEvent playerDataSerializeEvent, CompoundTag compoundTag, String string) {
        this.this$0 = server;
        this.val$event = playerDataSerializeEvent;
        this.val$tag = compoundTag;
        this.val$nameLower = string;
        this.b = false;
    }

    @Override
    public void onRun(int n) {
        this.onCancel();
    }

    @Override
    public void onCancel() {
        if (!this.b) {
            this.b = true;
            Server.access$400(this.this$0, this.val$event.getSerializer(), this.val$tag, this.val$nameLower, this.val$event.getUuid().orElse(null));
        }
    }
}

