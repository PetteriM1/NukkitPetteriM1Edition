/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.command.defaults.a;

public class DebugPasteCommand
extends VanillaCommand {
    public DebugPasteCommand(String string) {
        super(string, "%nukkit.command.debug.description", "%commands.debug.usage");
        this.setPermission("nukkit.command.debug.perform");
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        Server server = Server.getInstance();
        commandSender.sendMessage("Uploading...");
        server.getScheduler().scheduleAsyncTask(new a(server, commandSender));
        return true;
    }
}

