/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class SummonCommand
extends Command {
    public SummonCommand(String string) {
        super(string, "%nukkit.command.summon.description", "%nukkit.command.summon.usage");
        this.setPermission("nukkit.command.summon");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("entityType", false, "entityType"), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0 || stringArray.length == 1 && !(commandSender instanceof Player)) {
            return false;
        }
        String string2 = Character.toUpperCase(stringArray[0].charAt(0)) + stringArray[0].substring(1);
        int n = string2.length() - 1;
        for (int k = 2; k < n; ++k) {
            if (string2.charAt(k) != '_') continue;
            string2 = string2.substring(0, k) + Character.toUpperCase(string2.charAt(k + 1)) + string2.substring(k + 2);
        }
        Player player = stringArray.length == 2 ? Server.getInstance().getPlayer(stringArray[1].replace("@s", commandSender.getName())) : (Player)commandSender;
        if (player != null) {
            Position position = player.getPosition().floor().add(0.5, 0.0, 0.5);
            Entity entity = Entity.createEntity(string2, position, new Object[0]);
            if (entity != null) {
                entity.spawnToAll();
                commandSender.sendMessage("\u00a76Spawned " + string2 + " to " + player.getName());
            } else {
                commandSender.sendMessage("\u00a7cUnable to spawn " + string2);
            }
        } else {
            commandSender.sendMessage("\u00a7cUnknown player " + (stringArray.length == 2 ? stringArray[1] : commandSender.getName()));
        }
        return true;
    }
}

