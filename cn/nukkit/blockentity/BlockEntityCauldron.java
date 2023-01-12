/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.utils.BlockColor;

public class BlockEntityCauldron
extends BlockEntitySpawnable {
    public static final int POTION_TYPE_EMPTY = 65535;
    public static final int POTION_TYPE_NORMAL = 0;
    public static final int POTION_TYPE_SPLASH = 1;
    public static final int POTION_TYPE_LINGERING = 2;
    public static final int POTION_TYPE_LAVA = 61851;

    public BlockEntityCauldron(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        int n;
        int n2;
        if (!this.namedTag.contains("PotionId")) {
            this.namedTag.putShort("PotionId", 65535);
        }
        int n3 = n2 = ((n = this.namedTag.getShort("PotionId")) & 0xFFFF) == 65535 ? 65535 : 0;
        if (this.namedTag.getBoolean("SplashPotion")) {
            n2 = 1;
            this.namedTag.remove("SplashPotion");
        }
        if (!this.namedTag.contains("PotionType")) {
            this.namedTag.putShort("PotionType", n2);
        }
        super.initBlockEntity();
    }

    public int getPotionId() {
        return this.namedTag.getShort("PotionId");
    }

    public void setPotionId(int n) {
        this.namedTag.putShort("PotionId", n);
        this.spawnToAll();
    }

    public boolean hasPotion() {
        return this.getPotionId() != 65535;
    }

    public void setPotionType(int n) {
        this.namedTag.putShort("PotionType", n & 0xFFFF);
    }

    public int getPotionType() {
        return this.namedTag.getShort("PotionType") & 0xFFFF;
    }

    public boolean isSplashPotion() {
        return this.namedTag.getShort("PotionType") == 1;
    }

    public void setSplashPotion(boolean bl) {
        this.namedTag.putShort("PotionType", bl ? 1 : 0);
    }

    public BlockColor getCustomColor() {
        if (this.isCustomColor()) {
            int n = this.namedTag.getInt("CustomColor");
            int n2 = n >> 16 & 0xFF;
            int n3 = n >> 8 & 0xFF;
            int n4 = n & 0xFF;
            return new BlockColor(n2, n3, n4);
        }
        return null;
    }

    public boolean isCustomColor() {
        return this.namedTag.contains("CustomColor");
    }

    public void setCustomColor(BlockColor blockColor) {
        this.setCustomColor(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public void setCustomColor(int n, int n2, int n3) {
        int n4 = (n << 16 | n2 << 8 | n3) & 0xFFFFFF;
        if (n4 != this.namedTag.getInt("CustomColor")) {
            Player[] playerArray;
            this.namedTag.putInt("CustomColor", n4);
            Block block = this.getBlock();
            for (Player player : playerArray = this.level.getChunkPlayers(this.getChunkX(), this.getChunkZ()).values().toArray(new Player[0])) {
                UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, 0);
                updateBlockPacket.flags = 11;
                updateBlockPacket.x = (int)this.x;
                updateBlockPacket.y = (int)this.y;
                updateBlockPacket.z = (int)this.z;
                UpdateBlockPacket updateBlockPacket2 = (UpdateBlockPacket)updateBlockPacket.clone();
                updateBlockPacket2.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, block.getId(), block.getDamage());
                player.dataPacket(updateBlockPacket);
                player.dataPacket(updateBlockPacket2);
            }
            this.spawnToAll();
        }
    }

    public void clearCustomColor() {
        this.namedTag.remove("CustomColor");
        this.spawnToAll();
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 118 || n == 465;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Cauldron").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("PotionId", this.namedTag.getShort("PotionId")).putByte("PotionType", this.namedTag.getShort("PotionType"));
        if (this.namedTag.contains("CustomColor")) {
            compoundTag.putInt("CustomColor", this.namedTag.getInt("CustomColor"));
        }
        return compoundTag;
    }
}

