/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import cn.nukkit.scheduler.AsyncTask;
import java.util.function.Consumer;

class c
extends AsyncTask {
    private PlayerAsyncPreLoginEvent d;
    final Skin val$skin;
    final Player val$playerInstance;
    final Player this$0;

    c(Player player, Skin skin, Player player2) {
        this.this$0 = player;
        this.val$skin = skin;
        this.val$playerInstance = player2;
    }

    @Override
    public void onRun() {
        this.d = new PlayerAsyncPreLoginEvent(this.this$0.username, Player.access$000(this.this$0), Player.access$100(this.this$0), this.val$skin, this.val$playerInstance.getAddress(), this.val$playerInstance.getPort());
        Player.access$200(this.this$0).getPluginManager().callEvent(this.d);
    }

    @Override
    public void onCompletion(Server server) {
        if (!this.val$playerInstance.connected) {
            return;
        }
        if (this.d.getLoginResult() == PlayerAsyncPreLoginEvent.LoginResult.KICK) {
            this.val$playerInstance.close(this.d.getKickMessage(), this.d.getKickMessage());
        } else if (this.val$playerInstance.shouldLogin) {
            this.val$playerInstance.setSkin(this.d.getSkin());
            this.val$playerInstance.completeLoginSequence();
            for (Consumer<Server> consumer : this.d.getScheduledActions()) {
                consumer.accept(server);
            }
        }
    }
}

