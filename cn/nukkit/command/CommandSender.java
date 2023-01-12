/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.Server;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.permission.Permissible;

public interface CommandSender
extends Permissible {
    public void sendMessage(String var1);

    public void sendMessage(TextContainer var1);

    public Server getServer();

    public String getName();

    public boolean isPlayer();
}

