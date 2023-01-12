/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerMessageEvent;
import cn.nukkit.permission.Permissible;
import java.util.HashSet;
import java.util.Set;

public class PlayerChatEvent
extends PlayerMessageEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected String format;
    protected Set<CommandSender> recipients = new HashSet<CommandSender>();

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerChatEvent(Player player, String string) {
        this(player, string, "chat.type.text", null);
    }

    public PlayerChatEvent(Player player, String string, String string2, Set<CommandSender> set) {
        this.player = player;
        this.message = string;
        this.format = string2;
        if (set == null) {
            for (Permissible permissible : Server.getInstance().getPluginManager().getPermissionSubscriptions("nukkit.broadcast.user")) {
                if (!(permissible instanceof CommandSender)) continue;
                this.recipients.add((CommandSender)permissible);
            }
        } else {
            this.recipients = set;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String string) {
        this.format = string;
    }

    public Set<CommandSender> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<CommandSender> set) {
        this.recipients = set;
    }
}

