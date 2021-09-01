package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import cn.nukkit.scheduler.AsyncTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PreLoginTask extends AsyncTask {

    private final PlayerAsyncPreLoginEvent event;
    private final Player player;
    private final Consumer<PlayerAsyncPreLoginEvent> callback;

    public PreLoginTask(PlayerAsyncPreLoginEvent event, Player player, Consumer<PlayerAsyncPreLoginEvent> callback) {
        this.event = event;
        this.player = player;
        this.callback = callback;
    }

    @Override
    public void onRun() {
        this.player.getServer().getPluginManager().callEvent(this.event);
    }

    @Override
    public void onCompletion(Server server) {
        if (this.player.isClosed() || !this.player.shouldLogin()) {
            return;
        }

        if (this.event.getCombiner() == null) {
            this.callback.accept(this.event);
            return;
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        future.whenComplete((ignore, ex) -> {
            if (ex != null) {
                server.getLogger().error("PlayerAsyncPreLoginEvent future failed", ex);
            } else {
                server.getScheduler().scheduleTask(() -> this.callback.accept(this.event));
            }
        });
        this.event.getCombiner().setPromise(future);
    }
}
