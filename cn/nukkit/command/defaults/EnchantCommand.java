/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class EnchantCommand
extends VanillaCommand {
    public EnchantCommand(String string) {
        super(string, "%nukkit.command.enchant.description", "%commands.enchant.usage");
        this.setPermission("nukkit.command.enchant");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("enchantment ID", CommandParamType.INT, false), new CommandParameter("level", CommandParamType.INT, true)});
        this.commandParameters.put("byName", new CommandParameter[]{new CommandParameter("player", "target", false), new CommandParameter("id", false, "enchantmentType"), new CommandParameter("level", CommandParamType.INT, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        int n;
        int n2;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Player player = commandSender.getServer().getPlayer(stringArray[0].replace("@s", commandSender.getName()));
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            return true;
        }
        try {
            n2 = this.getIdByName(stringArray[1]);
            n = stringArray.length == 3 ? Integer.parseInt(stringArray[2]) : 1;
        }
        catch (NumberFormatException numberFormatException) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Enchantment enchantment = Enchantment.getEnchantment(n2);
        if (enchantment == null) {
            commandSender.sendMessage(new TranslationContainer("commands.enchant.notFound", String.valueOf(n2)));
            return true;
        }
        enchantment.setLevel(n);
        Item item = player.getInventory().getItemInHand();
        if (item.getId() <= 0) {
            commandSender.sendMessage(new TranslationContainer("commands.enchant.noItem"));
            return true;
        }
        item.addEnchantment(enchantment);
        player.getInventory().setItemInHand(item);
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("%commands.enchant.success"));
        return true;
    }

    public int getIdByName(String string) throws NumberFormatException {
        switch (string = string.toLowerCase()) {
            case "protection": {
                return 0;
            }
            case "fire_protection": {
                return 1;
            }
            case "feather_falling": {
                return 2;
            }
            case "blast_protection": {
                return 3;
            }
            case "projectile_projection": {
                return 4;
            }
            case "thorns": {
                return 5;
            }
            case "respiration": {
                return 6;
            }
            case "depth_strider": {
                return 7;
            }
            case "aqua_affinity": {
                return 8;
            }
            case "sharpness": {
                return 9;
            }
            case "smite": {
                return 10;
            }
            case "bane_of_arthropods": {
                return 11;
            }
            case "knockback": {
                return 12;
            }
            case "fire_aspect": {
                return 13;
            }
            case "looting": {
                return 14;
            }
            case "efficiency": {
                return 15;
            }
            case "silk_touch": {
                return 16;
            }
            case "durability": 
            case "unbreaking": {
                return 17;
            }
            case "fortune": {
                return 18;
            }
            case "power": {
                return 19;
            }
            case "punch": {
                return 20;
            }
            case "flame": {
                return 21;
            }
            case "infinity": {
                return 22;
            }
            case "luck_of_the_sea": {
                return 23;
            }
            case "lure": {
                return 24;
            }
            case "frost_walker": {
                return 25;
            }
            case "mending": {
                return 26;
            }
            case "binding_curse": {
                return 27;
            }
            case "vanishing_curse": {
                return 28;
            }
            case "impaling": {
                return 29;
            }
            case "riptide": {
                return 30;
            }
            case "loyalty": {
                return 31;
            }
            case "channeling": {
                return 32;
            }
            case "multishot": {
                return 33;
            }
            case "piercing": {
                return 34;
            }
            case "quick_charge": {
                return 35;
            }
            case "soul_speed": {
                return 36;
            }
            case "swift_sneak": {
                return 37;
            }
        }
        return Integer.parseInt(string);
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

