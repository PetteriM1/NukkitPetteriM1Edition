/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;

public abstract class DefaultPermissions {
    public static final String ROOT = "nukkit";

    public static Permission registerPermission(Permission permission) {
        return DefaultPermissions.registerPermission(permission, null);
    }

    public static Permission registerPermission(Permission permission, Permission permission2) {
        if (permission2 != null) {
            permission2.getChildren().put(permission.getName(), true);
        }
        Server.getInstance().getPluginManager().addPermission(permission);
        return Server.getInstance().getPluginManager().getPermission(permission.getName());
    }

    public static void registerCorePermissions() {
        Permission permission = DefaultPermissions.registerPermission(new Permission("nukkit", "Allows using all Nukkit commands and utilities"));
        Permission permission2 = DefaultPermissions.registerPermission(new Permission("nukkit.broadcast", "Allows the user to receive all broadcast messages"), permission);
        DefaultPermissions.registerPermission(new Permission("nukkit.broadcast.admin", "Allows the user to receive administrative broadcasts", "op"), permission2);
        DefaultPermissions.registerPermission(new Permission("nukkit.broadcast.user", "Allows the user to receive user broadcasts", "true"), permission2);
        permission2.recalculatePermissibles();
        Permission permission3 = DefaultPermissions.registerPermission(new Permission("nukkit.command", "Allows using all Nukkit commands"), permission);
        Permission permission4 = DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist", "Allows the user to modify the server whitelist", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.add", "Allows the user to add a player to the server whitelist"), permission4);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.remove", "Allows the user to remove a player to the server whitelist"), permission4);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.reload", "Allows the user to reload the server whitelist"), permission4);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.enable", "Allows the user to enable the server whitelist"), permission4);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.disable", "Allows the user to disable the server whitelist"), permission4);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.whitelist.list", "Allows the user to list all the players on the server whitelist"), permission4);
        permission4.recalculatePermissibles();
        Permission permission5 = DefaultPermissions.registerPermission(new Permission("nukkit.command.ban", "Allows the user to ban people", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.ban.player", "Allows the user to ban players"), permission5);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.ban.ip", "Allows the user to ban IP addresses"), permission5);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.ban.list", "Allows the user to list all the banned ips or players"), permission5);
        permission5.recalculatePermissibles();
        Permission permission6 = DefaultPermissions.registerPermission(new Permission("nukkit.command.unban", "Allows the user to unban people", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.unban.player", "Allows the user to unban players"), permission6);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.unban.ip", "Allows the user to unban IP addresses"), permission6);
        permission6.recalculatePermissibles();
        Permission permission7 = DefaultPermissions.registerPermission(new Permission("nukkit.command.op", "Allows the user to change operators", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.op.give", "Allows the user to give a player operator status"), permission7);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.op.take", "Allows the user to take a players operator status"), permission7);
        permission7.recalculatePermissibles();
        Permission permission8 = DefaultPermissions.registerPermission(new Permission("nukkit.command.save", "Allows the user to save the worlds", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.save.enable", "Allows the user to enable automatic saving"), permission8);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.save.disable", "Allows the user to disable automatic saving"), permission8);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.save.perform", "Allows the user to perform a manual save"), permission8);
        permission8.recalculatePermissibles();
        Permission permission9 = DefaultPermissions.registerPermission(new Permission("nukkit.command.time", "Allows the user to alter the time", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.time.add", "Allows the user to fast-forward time"), permission9);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.time.set", "Allows the user to change the time"), permission9);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.time.start", "Allows the user to restart the time"), permission9);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.time.stop", "Allows the user to stop the time"), permission9);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.time.query", "Allows the user query the time"), permission9);
        permission9.recalculatePermissibles();
        Permission permission10 = DefaultPermissions.registerPermission(new Permission("nukkit.command.kill", "Allows the user to kill players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.kill.self", "Allows the user to commit suicide", "true"), permission10);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.kill.other", "Allows the user to kill other players"), permission10);
        permission10.recalculatePermissibles();
        Permission permission11 = DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode", "Allows the user to change the gamemode of players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode.survival", "Allows the user to change the gamemode to survival", "op"), permission11);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode.creative", "Allows the user to change the gamemode to creative", "op"), permission11);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode.adventure", "Allows the user to change the gamemode to adventure", "op"), permission11);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode.spectator", "Allows the user to change the gamemode to spectator", "op"), permission11);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamemode.other", "Allows the user to change the gamemode of other players", "op"), permission11);
        permission11.recalculatePermissibles();
        DefaultPermissions.registerPermission(new Permission("nukkit.command.me", "Allows the user to perform a chat action", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.tell", "Allows the user to privately message another player", "true"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.say", "Allows the user to talk as the console", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.give", "Allows the user to give items to players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.effect", "Allows the user to give/take potion effects", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.particle", "Allows the user to create particle effects", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.teleport", "Allows the user to teleport players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.kick", "Allows the user to kick players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.stop", "Allows the user to stop the server", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.list", "Allows the user to list all online players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.help", "Allows the user to view the help menu", "true"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.plugins", "Allows the user to view the list of plugins", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.reload", "Allows the user to reload the server settings", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.version", "Allows the user to view the version of the server", "true"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.version.plugins", "Allows the user to view the version of the plugins", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.defaultgamemode", "Allows the user to change the default gamemode", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.status", "Allows the user to view the server performance", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gc", "Allows the user to fire garbage collection tasks", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.timings", "Allows the user to records timings for all plugin events", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.title", "Allows the user to send titles to players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.spawnpoint", "Allows the user to change player's spawnpoint", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.setworldspawn", "Allows the user to change the world spawn", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.weather", "Allows the user to change the weather", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.xp", "Allows the user to give experience", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.transferserver", "Allows the user to transfer to other server", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.summon", "Allows the user to summon entities", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.world", "Allows the user to switch world", Server.getInstance().suomiCraftPEMode() ? "true" : "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.world.others", "Allows the user to switch world for other players", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.generateworld", "Allows the user to generate new world", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.seed", "Allows the user to see world's seed", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.playsound", "Allows the user to play sounds", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.debug.perform", "Allows the user to create debug paste", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.gamerule", "Allows the user to change game rules", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.spawn", "Allows the user to use spawn command", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.enchant", "Allows the user to enchant items", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.command.difficulty", "Allows the user to change difficulty", "op"), permission3);
        DefaultPermissions.registerPermission(new Permission("nukkit.textcolor", "Allows the user to write colored text", "op"), permission3);
        permission3.recalculatePermissibles();
        permission.recalculatePermissibles();
    }
}

