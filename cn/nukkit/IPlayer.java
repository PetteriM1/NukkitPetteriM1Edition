/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.permission.ServerOperator;
import java.util.UUID;

public interface IPlayer
extends ServerOperator,
Metadatable {
    public boolean isOnline();

    public String getName();

    public UUID getUniqueId();

    public boolean isBanned();

    public void setBanned(boolean var1);

    public boolean isWhitelisted();

    public void setWhitelisted(boolean var1);

    public Player getPlayer();

    public Server getServer();

    public Long getFirstPlayed();

    public Long getLastPlayed();

    public boolean hasPlayedBefore();
}

