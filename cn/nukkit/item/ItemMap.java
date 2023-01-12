/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ClientboundMapItemDataPacket;
import cn.nukkit.utils.MainLogger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class ItemMap
extends Item {
    public static long mapCount = 0L;
    private BufferedImage x;

    public ItemMap() {
        this((Integer)0, 1);
    }

    public ItemMap(Integer n) {
        this(n, 1);
    }

    public ItemMap(Integer n, int n2) {
        super(358, n, n2, "Map");
    }

    public void setImage(File file) throws IOException {
        this.setImage(ImageIO.read(file));
    }

    public void setImage(BufferedImage bufferedImage) {
        try {
            Object object;
            if (this.getMapId() == 0L) {
                Server.getInstance().getLogger().debug("Uninitialized map", new Throwable());
                this.initItem();
            }
            if (bufferedImage.getHeight() != 128 || bufferedImage.getWidth() != 128) {
                this.x = new BufferedImage(128, 128, bufferedImage.getType());
                object = this.x.createGraphics();
                ((Graphics)object).drawImage(bufferedImage, 0, 0, 128, 128, null);
                ((Graphics)object).dispose();
            } else {
                this.x = bufferedImage;
            }
            object = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)this.x, "png", (OutputStream)object);
            this.setNamedTag(this.getNamedTag().putByteArray("Colors", ((ByteArrayOutputStream)object).toByteArray()));
            ((ByteArrayOutputStream)object).close();
        }
        catch (IOException iOException) {
            MainLogger.getLogger().logException(iOException);
        }
    }

    protected BufferedImage loadImageFromNBT() {
        try {
            byte[] byArray = this.getNamedTag().getByteArray("Colors");
            this.x = ImageIO.read(new ByteArrayInputStream(byArray));
            return this.x;
        }
        catch (IOException iOException) {
            MainLogger.getLogger().logException(iOException);
            return null;
        }
    }

    public long getMapId() {
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag == null) {
            return 0L;
        }
        return compoundTag.getLong("map_uuid");
    }

    public void sendImage(Player player) {
        BufferedImage bufferedImage = this.x != null ? this.x : this.loadImageFromNBT();
        ClientboundMapItemDataPacket clientboundMapItemDataPacket = new ClientboundMapItemDataPacket();
        clientboundMapItemDataPacket.mapId = this.getMapId();
        clientboundMapItemDataPacket.update = 2;
        clientboundMapItemDataPacket.scale = 0;
        clientboundMapItemDataPacket.width = 128;
        clientboundMapItemDataPacket.height = 128;
        clientboundMapItemDataPacket.offsetX = 0;
        clientboundMapItemDataPacket.offsetZ = 0;
        clientboundMapItemDataPacket.image = bufferedImage;
        if (player.protocol >= 560) {
            clientboundMapItemDataPacket.eids = new long[]{clientboundMapItemDataPacket.mapId};
        }
        player.dataPacket(clientboundMapItemDataPacket);
        if (player.protocol >= 544) {
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> player.dataPacket(clientboundMapItemDataPacket), 20);
        }
    }

    public boolean trySendImage(Player player) {
        BufferedImage bufferedImage;
        BufferedImage bufferedImage2 = bufferedImage = this.x != null ? this.x : this.loadImageFromNBT();
        if (bufferedImage == null) {
            return false;
        }
        ClientboundMapItemDataPacket clientboundMapItemDataPacket = new ClientboundMapItemDataPacket();
        clientboundMapItemDataPacket.mapId = this.getMapId();
        clientboundMapItemDataPacket.update = 2;
        clientboundMapItemDataPacket.scale = 0;
        clientboundMapItemDataPacket.width = 128;
        clientboundMapItemDataPacket.height = 128;
        clientboundMapItemDataPacket.offsetX = 0;
        clientboundMapItemDataPacket.offsetZ = 0;
        clientboundMapItemDataPacket.image = bufferedImage;
        if (player.protocol >= 560) {
            clientboundMapItemDataPacket.eids = new long[]{clientboundMapItemDataPacket.mapId};
        }
        player.dataPacket(clientboundMapItemDataPacket);
        if (player.protocol >= 544) {
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> player.dataPacket(clientboundMapItemDataPacket), 20);
        }
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public Item initItem() {
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag == null || !compoundTag.contains("map_uuid")) {
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putLong("map_uuid", ++mapCount);
            compoundTag2.putInt("map_name_index", (int)mapCount);
            this.setNamedTag(compoundTag2);
        } else {
            long l = this.getMapId();
            if (l > mapCount) {
                mapCount = l;
            }
            if (!(compoundTag = this.getNamedTag()).contains("map_name_index")) {
                compoundTag.putInt("map_name_index", (int)l);
                this.setNamedTag(compoundTag);
            }
        }
        return super.initItem();
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

