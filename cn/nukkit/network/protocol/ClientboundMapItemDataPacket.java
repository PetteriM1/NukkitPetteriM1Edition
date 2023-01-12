/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Utils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ClientboundMapItemDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = 67;
    public long[] eids = new long[0];
    public long mapId;
    public int update;
    public byte scale;
    public boolean isLocked;
    public int width;
    public int height;
    public int offsetX;
    public int offsetZ;
    public byte dimensionId;
    public BlockVector3 origin = new BlockVector3();
    public MapDecorator[] decorators = new MapDecorator[0];
    public MapTrackedObject[] trackedEntities = new MapTrackedObject[0];
    public int[] colors = new int[0];
    public BufferedImage image = null;
    public static final int TEXTURE_UPDATE = 2;
    public static final int DECORATIONS_UPDATE = 4;
    public static final int ENTITIES_UPDATE = 8;

    @Override
    public byte pid() {
        return 67;
    }

    @Override
    public void decode() {
        this.b();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void encode() {
        int n;
        int n2;
        this.reset();
        this.putEntityUniqueId(this.mapId);
        int n3 = 0;
        if (this.eids.length > 0) {
            n3 |= 8;
        }
        if (this.decorators.length > 0) {
            n3 |= 4;
        }
        if (this.image != null || this.colors.length > 0) {
            n3 |= 2;
        }
        this.putUnsignedVarInt(n3);
        this.putByte(this.dimensionId);
        if (this.protocol >= 354) {
            this.putBoolean(this.isLocked);
            if (this.protocol >= 544) {
                this.putSignedBlockPosition(this.origin);
            }
        }
        if ((n3 & 8) != 0) {
            this.putUnsignedVarInt(this.eids.length);
            long[] objectArray = this.eids;
            n2 = objectArray.length;
            for (n = 0; n < n2; ++n) {
                long l = objectArray[n];
                this.putEntityUniqueId(l);
            }
        }
        if ((n3 & 0xE) != 0) {
            this.putByte(this.scale);
        }
        if ((n3 & 4) != 0) {
            this.putUnsignedVarInt(this.trackedEntities.length);
            for (MapTrackedObject mapTrackedObject : this.trackedEntities) {
                this.putLInt(mapTrackedObject.type);
                if (mapTrackedObject.type == 1) {
                    this.putBlockVector3(mapTrackedObject.x, mapTrackedObject.y, mapTrackedObject.z);
                    continue;
                }
                if (mapTrackedObject.type == 0) {
                    this.putEntityUniqueId(mapTrackedObject.entityUniqueId);
                    continue;
                }
                throw new IllegalArgumentException("Unknown map object type " + mapTrackedObject.type);
            }
            this.putUnsignedVarInt(this.decorators.length);
            MapDecorator[] mapDecoratorArray = this.decorators;
            n2 = mapDecoratorArray.length;
            for (n = 0; n < n2; ++n) {
                MapDecorator mapDecorator = mapDecoratorArray[n];
                this.putByte(mapDecorator.icon);
                this.putByte(mapDecorator.rotation);
                this.putByte(mapDecorator.offsetX);
                this.putByte(mapDecorator.offsetZ);
                this.putString(mapDecorator.label);
                this.putUnsignedVarInt(mapDecorator.color.getRGB());
            }
        }
        if ((n3 & 2) != 0) {
            this.putVarInt(this.width);
            this.putVarInt(this.height);
            this.putVarInt(this.offsetX);
            this.putVarInt(this.offsetZ);
            this.putUnsignedVarInt((long)this.width * (long)this.height);
            if (this.image != null) {
                void var2_7;
                boolean bl = false;
                while (var2_7 < this.width) {
                    for (n2 = 0; n2 < this.height; ++n2) {
                        this.putUnsignedVarInt(Utils.toABGR(this.image.getRGB(n2, (int)var2_7)));
                    }
                    ++var2_7;
                }
                this.image.flush();
            } else if (this.colors.length > 0) {
                for (int n4 : this.colors) {
                    this.putUnsignedVarInt(n4);
                }
            }
        }
    }

    public String toString() {
        return "ClientboundMapItemDataPacket(eids=" + Arrays.toString(this.eids) + ", mapId=" + this.mapId + ", update=" + this.update + ", scale=" + this.scale + ", isLocked=" + this.isLocked + ", width=" + this.width + ", height=" + this.height + ", offsetX=" + this.offsetX + ", offsetZ=" + this.offsetZ + ", dimensionId=" + this.dimensionId + ", origin=" + this.origin + ", decorators=" + Arrays.deepToString(this.decorators) + ", trackedEntities=" + Arrays.deepToString(this.trackedEntities) + ", colors=" + Arrays.toString(this.colors) + ", image=" + this.image + ")";
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }

    public static class MapTrackedObject {
        public static final int TYPE_ENTITY = 0;
        public static final int TYPE_BLOCK = 1;
        public int type;
        public long entityUniqueId;
        public int x;
        public int y;
        public int z;
    }

    public static class MapDecorator {
        public byte rotation;
        public byte icon;
        public byte offsetX;
        public byte offsetZ;
        public String label;
        public Color color;
    }
}

