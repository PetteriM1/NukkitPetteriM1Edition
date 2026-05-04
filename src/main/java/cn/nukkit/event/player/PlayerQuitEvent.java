package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.lang.TextContainer;

public class PlayerQuitEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    protected TextContainer quitMessage;
    protected boolean autoSave;
    protected String reason;
    public PlayerQuitEvent(Player player, TextContainer quitMessage, String reason) {
        this(player, quitMessage, true, reason);
    }

    public PlayerQuitEvent(Player player, TextContainer quitMessage) {
        this(player, quitMessage, true);
    }

    public PlayerQuitEvent(Player player, String quitMessage, String reason) {
        this(player, quitMessage, true, reason);
    }

    public PlayerQuitEvent(Player player, String quitMessage) {
        this(player, quitMessage, true);
    }

    public PlayerQuitEvent(Player player, String quitMessage, boolean autoSave, String reason) {
        this(player, new TextContainer(quitMessage), autoSave, reason);
    }

    public PlayerQuitEvent(Player player, String quitMessage, boolean autoSave) {
        this(player, new TextContainer(quitMessage), autoSave);
    }

    public PlayerQuitEvent(Player player, TextContainer quitMessage, boolean autoSave) {
        this(player, quitMessage, autoSave, "No reason");
    }

    public PlayerQuitEvent(Player player, TextContainer quitMessage, boolean autoSave, String reason) {
        this.player = player;
        this.quitMessage = quitMessage;
        this.autoSave = autoSave;
        this.reason = reason;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public void setQuitMessage(String quitMessage) {
        this.setQuitMessage(new TextContainer(quitMessage));
    }

    public void setQuitMessage(TextContainer quitMessage) {
        this.quitMessage = quitMessage;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public TextContainer getQuitMessage() {
        return quitMessage;
    }

    public String getReason() {
        return reason;
    }

    public void setAutoSave() {
        this.setAutoSave(true);
    }
}
