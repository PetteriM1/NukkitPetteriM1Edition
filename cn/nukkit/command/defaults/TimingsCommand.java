/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsExport;

public class TimingsCommand
extends VanillaCommand {
    public TimingsCommand(String string) {
        super(string, "%nukkit.command.timings.description", "%nukkit.command.timings.usage");
        this.setPermission("nukkit.command.timings");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("on|off|paste", "string", false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length != 1) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        String string2 = stringArray[0].toLowerCase();
        if (string2.equals("on")) {
            Timings.setTimingsEnabled(true);
            Timings.reset();
            commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.enable"));
            return true;
        }
        if (string2.equals("off")) {
            Timings.setTimingsEnabled(false);
            commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.disable"));
            return true;
        }
        if (!Timings.isTimingsEnabled()) {
            commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.timingsDisabled"));
            return true;
        }
        switch (string2) {
            case "verbon": {
                commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.verboseEnable"));
                Timings.setVerboseEnabled(true);
                break;
            }
            case "verboff": {
                commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.verboseDisable"));
                Timings.setVerboseEnabled(false);
                break;
            }
            case "reset": {
                Timings.reset();
                commandSender.sendMessage(new TranslationContainer("nukkit.command.timings.reset"));
                break;
            }
            case "report": 
            case "paste": {
                TimingsExport.reportTimings(commandSender);
            }
        }
        return true;
    }
}

