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
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;
import java.util.StringJoiner;

public class KillCommand
extends VanillaCommand {
    public KillCommand(String string) {
        super(string, "%nukkit.command.kill.description", "%nukkit.command.kill.usage", new String[]{"suicide"});
        this.setPermission("nukkit.command.kill.self;nukkit.command.kill.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        EntityDamageEvent entityDamageEvent;
        Player player;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length >= 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (stringArray.length == 1) {
            if (!commandSender.hasPermission("nukkit.command.kill.other")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            Player player2 = commandSender.getServer().getPlayer(stringArray[0]);
            if (player2 != null) {
                if (player2.isCreative() || player2.isSpectator()) {
                    commandSender.sendMessage((Object)((Object)TextFormat.RED) + "No targets matched selector");
                    return true;
                }
                EntityDamageEvent entityDamageEvent2 = new EntityDamageEvent((Entity)player2, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
                commandSender.getServer().getPluginManager().callEvent(entityDamageEvent2);
                if (entityDamageEvent2.isCancelled()) {
                    return true;
                }
                player2.setLastDamageCause(entityDamageEvent2);
                player2.setHealth(0.0f);
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.kill.successful", player2.getName()));
            } else if (stringArray[0].equals("@e")) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (Level level : Server.getInstance().getLevels().values()) {
                    for (Entity entity : level.getEntities()) {
                        if (entity instanceof Player) continue;
                        EntityDamageEvent entityDamageEvent3 = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
                        commandSender.getServer().getPluginManager().callEvent(entityDamageEvent3);
                        if (entityDamageEvent3.isCancelled()) continue;
                        stringJoiner.add(entity.getName());
                        entity.setLastDamageCause(entityDamageEvent3);
                        entity.close();
                    }
                }
                String string2 = stringJoiner.toString();
                commandSender.sendMessage(new TranslationContainer("commands.kill.successful", (String)(string2.isEmpty() ? "0" : string2)));
            } else if (stringArray[0].equals("@s")) {
                if (!commandSender.hasPermission("nukkit.command.kill.self")) {
                    commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(new TranslationContainer("%commands.generic.ingame"));
                    return true;
                }
                Player player3 = (Player)commandSender;
                if (player3.isCreative() || player3.isSpectator()) {
                    commandSender.sendMessage((Object)((Object)TextFormat.RED) + "No targets matched selector");
                    return true;
                }
                EntityDamageEvent entityDamageEvent4 = new EntityDamageEvent((Entity)player3, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
                commandSender.getServer().getPluginManager().callEvent(entityDamageEvent4);
                if (entityDamageEvent4.isCancelled()) {
                    return true;
                }
                player3.setLastDamageCause(entityDamageEvent4);
                player3.setHealth(0.0f);
                commandSender.sendMessage(new TranslationContainer("commands.kill.successful", commandSender.getName()));
            } else if (stringArray[0].equals("@a")) {
                if (!commandSender.hasPermission("nukkit.command.kill.other")) {
                    commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                    return true;
                }
                for (Level level : Server.getInstance().getLevels().values()) {
                    for (Entity entity : level.getEntities()) {
                        Player player4;
                        if (!(entity instanceof Player) || (player4 = (Player)entity).isCreative() || player4.isSpectator()) continue;
                        EntityDamageEvent entityDamageEvent5 = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
                        commandSender.getServer().getPluginManager().callEvent(entityDamageEvent5);
                        if (entityDamageEvent5.isCancelled()) continue;
                        entity.setLastDamageCause(entityDamageEvent5);
                        entity.setHealth(0.0f);
                    }
                }
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.GOLD) + "%commands.kill.all.successful"));
            } else {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            }
            return true;
        }
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission("nukkit.command.kill.self")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            player = (Player)commandSender;
            if (player.isCreative() || player.isSpectator()) {
                commandSender.sendMessage((Object)((Object)TextFormat.RED) + "No targets matched selector");
                return true;
            }
            entityDamageEvent = new EntityDamageEvent((Entity)player, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
            commandSender.getServer().getPluginManager().callEvent(entityDamageEvent);
            if (entityDamageEvent.isCancelled()) {
                return true;
            }
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        player.setLastDamageCause(entityDamageEvent);
        player.setHealth(0.0f);
        commandSender.sendMessage(new TranslationContainer("commands.kill.successful", commandSender.getName()));
        return true;
    }
}

