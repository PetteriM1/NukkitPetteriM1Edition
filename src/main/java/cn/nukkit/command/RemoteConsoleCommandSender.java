package cn.nukkit.command;

import cn.nukkit.lang.TextContainer;

/**
 * Represents an RCON command sender.
 *
 * @author Tee7even
 */
public class RemoteConsoleCommandSender extends ConsoleCommandSender {

    private final StringBuilder messages = new StringBuilder();

    public String getMessages() {
        return messages.toString();
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    public void clearMessages() {
        messages.delete(0, messages.length());
    }

    @Override
    public void sendMessage(TextContainer message) {
        this.sendMessage(this.getServer().getLanguage().translate(message));
    }

    @Override
    public void sendMessage(String message) {
        message = this.getServer().getLanguage().translateString(message);
        this.messages.append(message.trim()).append('\n');
    }
}
