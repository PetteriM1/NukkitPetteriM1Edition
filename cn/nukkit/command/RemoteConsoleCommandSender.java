/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.TextContainer;

public class RemoteConsoleCommandSender
extends ConsoleCommandSender {
    private final StringBuilder a = new StringBuilder();

    @Override
    public void sendMessage(String string) {
        string = this.getServer().getLanguage().translateString(string);
        this.a.append(string.trim()).append('\n');
    }

    @Override
    public void sendMessage(TextContainer textContainer) {
        this.sendMessage(this.getServer().getLanguage().translate(textContainer));
    }

    public String getMessages() {
        return this.a.toString();
    }

    public void clearMessages() {
        this.a.delete(0, this.a.length());
    }

    @Override
    public String getName() {
        return "Rcon";
    }
}

