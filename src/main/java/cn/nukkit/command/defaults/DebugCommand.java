package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;

/**
 * Created on 2015/11/12 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class DebugCommand extends VanillaCommand {

    public DebugCommand(String name) {
        super(name, "%nukkit.command.debug.description", "%commands.debug.usage");
        this.setPermission("nukkit.command.op");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("%commands.debug.usage", this.usageMessage));
            return false;
        }

        Nukkit.DEBUG = args[0].equals("off")? 1 : args[0].equals("censored")?  2 : 3;
        if (Nukkit.DEBUG > 1){
            sender.sendMessage("§eDebug was enabled!");
        }else {
            sender.sendMessage("§eDebug was disabled!");
        }

        org.apache.logging.log4j.Level currentLevel = Nukkit.getLogLevel();
        for (org.apache.logging.log4j.Level level : org.apache.logging.log4j.Level.values()) {
            if (level.intLevel() == (Nukkit.DEBUG + 3) * 100  && level.intLevel() > currentLevel.intLevel()) {
                Nukkit.setLogLevel(level);
                break;
            }
        }
        return true;
    }
}
