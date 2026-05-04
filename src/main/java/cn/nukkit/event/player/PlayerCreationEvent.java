package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.network.SourceInterface;

import java.net.InetSocketAddress;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class PlayerCreationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final SourceInterface interfaz;
    private final Long clientId;
    private final InetSocketAddress socketAddress;
    private Class<? extends Player> baseClass;
    private Class<? extends Player> playerClass;

    public PlayerCreationEvent(SourceInterface interfaz, Class<? extends Player> baseClass, Class<? extends Player> playerClass, Long clientId, InetSocketAddress socketAddress) {
        this.interfaz = interfaz;
        this.clientId = clientId;
        this.socketAddress = socketAddress;

        this.baseClass = baseClass;
        this.playerClass = playerClass;
    }

    public void setBaseClass(Class<? extends Player> baseClass) {
        this.baseClass = baseClass;
    }

    public void setPlayerClass(Class<? extends Player> playerClass) {
        this.playerClass = playerClass;
    }

    public String getAddress() {
        return this.socketAddress.getAddress().toString();
    }

    public Class<? extends Player> getBaseClass() {
        return baseClass;
    }

    public Long getClientId() {
        return clientId;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SourceInterface getInterface() {
        return interfaz;
    }

    public Class<? extends Player> getPlayerClass() {
        return playerClass;
    }

    public int getPort() {
        return this.socketAddress.getPort();
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }
}
