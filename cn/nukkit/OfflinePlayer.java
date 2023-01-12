/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;
import java.util.List;
import java.util.UUID;

public class OfflinePlayer
implements IPlayer {
    private final Server a;
    private final CompoundTag b;

    public OfflinePlayer(Server server, UUID uUID) {
        this(server, uUID, null);
    }

    public OfflinePlayer(Server server, String string) {
        this(server, null, string);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public OfflinePlayer(Server server, UUID uUID, String string) {
        CompoundTag compoundTag;
        this.a = server;
        if (server.savePlayerDataByUuid) {
            if (uUID != null) {
                compoundTag = this.a.getOfflinePlayerData(uUID, false);
            } else {
                if (string == null) throw new IllegalArgumentException("Name and UUID cannot both be null");
                compoundTag = this.a.getOfflinePlayerData(string, false);
            }
        } else if (string != null) {
            compoundTag = this.a.getOfflinePlayerData(string, false);
        } else {
            if (uUID == null) throw new IllegalArgumentException("Name and UUID cannot both be null");
            compoundTag = this.a.getOfflinePlayerData(uUID, false);
        }
        if (compoundTag == null) {
            compoundTag = new CompoundTag();
        }
        this.b = compoundTag;
        if (uUID != null) {
            this.b.putLong("UUIDMost", uUID.getMostSignificantBits());
            this.b.putLong("UUIDLeast", uUID.getLeastSignificantBits());
        }
        if (string == null || uUID != null && this.b.contains("NameTag")) return;
        this.b.putString("NameTag", string);
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public String getName() {
        if (this.b != null && this.b.contains("NameTag")) {
            return this.b.getString("NameTag");
        }
        return null;
    }

    @Override
    public UUID getUniqueId() {
        if (this.b != null) {
            long l = this.b.getLong("UUIDLeast");
            long l2 = this.b.getLong("UUIDMost");
            if (l != 0L && l2 != 0L) {
                return new UUID(l2, l);
            }
        }
        return null;
    }

    @Override
    public Server getServer() {
        return this.a;
    }

    @Override
    public boolean isOp() {
        return this.a.isOp(this.getName().toLowerCase());
    }

    @Override
    public void setOp(boolean bl) {
        if (bl == this.isOp()) {
            return;
        }
        if (bl) {
            this.a.addOp(this.getName().toLowerCase());
        } else {
            this.a.removeOp(this.getName().toLowerCase());
        }
    }

    @Override
    public boolean isBanned() {
        return this.a.getNameBans().isBanned(this.getName());
    }

    @Override
    public void setBanned(boolean bl) {
        if (bl) {
            this.a.getNameBans().addBan(this.getName(), null, null, null);
        } else {
            this.a.getNameBans().remove(this.getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.a.isWhitelisted(this.getName().toLowerCase());
    }

    @Override
    public void setWhitelisted(boolean bl) {
        if (bl) {
            this.a.addWhitelist(this.getName().toLowerCase());
        } else {
            this.a.removeWhitelist(this.getName().toLowerCase());
        }
    }

    @Override
    public Player getPlayer() {
        return this.a.getPlayerExact(this.getName());
    }

    @Override
    public Long getFirstPlayed() {
        return this.b != null ? Long.valueOf(this.b.getLong("firstPlayed")) : null;
    }

    @Override
    public Long getLastPlayed() {
        return this.b != null ? Long.valueOf(this.b.getLong("lastPlayed")) : null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.b != null;
    }

    @Override
    public void setMetadata(String string, MetadataValue metadataValue) {
        this.a.getPlayerMetadata().setMetadata(this, string, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return this.a.getPlayerMetadata().getMetadata(this, string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return this.a.getPlayerMetadata().hasMetadata(this, string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        this.a.getPlayerMetadata().removeMetadata(this, string, plugin);
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

