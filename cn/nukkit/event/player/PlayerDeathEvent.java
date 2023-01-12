/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TextContainer;

public class PlayerDeathEvent
extends EntityDeathEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private TextContainer d;
    private boolean f = false;
    private boolean h = false;
    private int g;

    public static HandlerList getHandlers() {
        return e;
    }

    public PlayerDeathEvent(Player player, Item[] itemArray, TextContainer textContainer, int n) {
        super(player, itemArray);
        this.d = textContainer;
        this.g = n;
    }

    public PlayerDeathEvent(Player player, Item[] itemArray, String string, int n) {
        this(player, itemArray, new TextContainer(string), n);
    }

    @Override
    public Player getEntity() {
        return (Player)super.getEntity();
    }

    public TextContainer getDeathMessage() {
        return this.d;
    }

    public void setDeathMessage(TextContainer textContainer) {
        this.d = textContainer;
    }

    public void setDeathMessage(String string) {
        this.d = new TextContainer(string);
    }

    public boolean getKeepInventory() {
        return this.f;
    }

    public void setKeepInventory(boolean bl) {
        this.f = bl;
    }

    public boolean getKeepExperience() {
        return this.h;
    }

    public void setKeepExperience(boolean bl) {
        this.h = bl;
    }

    public int getExperience() {
        return this.g;
    }

    public void setExperience(int n) {
        this.g = n;
    }
}

