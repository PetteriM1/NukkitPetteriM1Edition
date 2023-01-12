/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerGameModeChangeEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final int gamemode;
    protected AdventureSettings newAdventureSettings;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerGameModeChangeEvent(Player player, int n, AdventureSettings adventureSettings) {
        this.player = player;
        this.gamemode = n;
        this.newAdventureSettings = adventureSettings;
    }

    public int getNewGamemode() {
        return this.gamemode;
    }

    public AdventureSettings getNewAdventureSettings() {
        return this.newAdventureSettings;
    }

    public void setNewAdventureSettings(AdventureSettings adventureSettings) {
        this.newAdventureSettings = adventureSettings;
    }
}

