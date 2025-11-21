package cn.nukkit.command;

import java.util.List;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface CommandMap {

    void clearCommands();

    boolean dispatch(CommandSender sender, String cmdLine);

    Command getCommand(String name);

    boolean register(String fallbackPrefix, Command command, String label);

    boolean register(String fallbackPrefix, Command command);

    void registerAll(String fallbackPrefix, List<? extends Command> commands);

    void registerSimpleCommands(Object object);
}
