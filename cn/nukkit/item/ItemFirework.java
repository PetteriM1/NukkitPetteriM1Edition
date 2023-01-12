/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemElytra;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.DyeColor;
import java.util.ArrayList;
import java.util.List;

public class ItemFirework
extends Item {
    public ItemFirework() {
        this((Integer)0);
    }

    public ItemFirework(Integer n) {
        this(n, 1);
    }

    public ItemFirework(Integer n, int n2) {
        super(401, n, n2, "Fireworks");
        CompoundTag compoundTag;
        if (!(this.hasCompoundTag() && this.getNamedTag().contains("Fireworks") || (compoundTag = this.getNamedTag()) != null)) {
            compoundTag = new CompoundTag();
            CompoundTag compoundTag2 = new CompoundTag().putByteArray("FireworkColor", new byte[]{(byte)DyeColor.BLACK.getDyeData()}).putByteArray("FireworkFade", new byte[0]).putBoolean("FireworkFlicker", false).putBoolean("FireworkTrail", false).putByte("FireworkType", FireworkExplosion.ExplosionType.CREEPER_SHAPED.ordinal());
            compoundTag.putCompound("Fireworks", new CompoundTag("Fireworks").putList(new ListTag<CompoundTag>("Explosions").add(compoundTag2)).putByte("Flight", 1));
            this.setNamedTag(compoundTag);
        }
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (player.isAdventure() && !player.getServer().suomiCraftPEMode()) {
            return false;
        }
        if (block.canPassThrough()) {
            this.a(level, block);
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        if (player.getInventory().getChestplateFast() instanceof ItemElytra && player.isGliding()) {
            this.a(player.getLevel(), player);
            if (!player.isCreative()) {
                --this.count;
            }
            player.onFireworkBoost();
            int n = this.getFlight();
            double d2 = 1.0 + 0.25 * (n < 1 ? 0.25 : (double)n);
            player.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(player.yaw)) * Math.cos(FastMathLite.toRadians(player.pitch)) * d2, -Math.sin(FastMathLite.toRadians(player.pitch)) * d2, Math.cos(FastMathLite.toRadians(player.yaw)) * Math.cos(FastMathLite.toRadians(player.pitch)) * d2));
            return true;
        }
        return false;
    }

    public void addExplosion(FireworkExplosion fireworkExplosion) {
        List<DyeColor> list = fireworkExplosion.getColors();
        List<DyeColor> list2 = fireworkExplosion.getFades();
        if (list.isEmpty()) {
            return;
        }
        byte[] byArray = new byte[list.size()];
        for (int k = 0; k < byArray.length; ++k) {
            byArray[k] = (byte)list.get(k).getDyeData();
        }
        byte[] byArray2 = new byte[list2.size()];
        for (int k = 0; k < byArray2.length; ++k) {
            byArray2[k] = (byte)list2.get(k).getDyeData();
        }
        ListTag<CompoundTag> listTag = this.getNamedTag().getCompound("Fireworks").getList("Explosions", CompoundTag.class);
        CompoundTag compoundTag = new CompoundTag().putByteArray("FireworkColor", byArray).putByteArray("FireworkFade", byArray2).putBoolean("FireworkFlicker", fireworkExplosion.a).putBoolean("FireworkTrail", fireworkExplosion.c).putByte("FireworkType", fireworkExplosion.e.ordinal());
        listTag.add(compoundTag);
    }

    public void clearExplosions() {
        this.getNamedTag().getCompound("Fireworks").putList(new ListTag("Explosions"));
    }

    private void a(Level level, Vector3 vector3) {
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector3.x + 0.5)).add(new DoubleTag("", vector3.y + 0.5)).add(new DoubleTag("", vector3.z + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putCompound("FireworkItem", NBTIO.putItemHelper(this));
        EntityFirework entityFirework = new EntityFirework(level.getChunk(vector3.getChunkX(), vector3.getChunkZ()), compoundTag);
        entityFirework.spawnToAll();
    }

    public int getFlight() {
        int n = 0;
        Tag tag = this.getNamedTag();
        if (tag != null && (tag = ((CompoundTag)tag).get("Fireworks")) instanceof CompoundTag) {
            n = ((CompoundTag)tag).getByte("Flight");
        }
        return n;
    }

    public void setFlight(int n) {
        CompoundTag compoundTag = this.getNamedTag();
        compoundTag.putCompound("Fireworks", compoundTag.getCompound("Fireworks").putByte("Flight", n));
        this.setNamedTag(compoundTag);
    }

    public static class FireworkExplosion {
        private final List<DyeColor> d = new ArrayList<DyeColor>();
        private final List<DyeColor> b = new ArrayList<DyeColor>();
        private boolean a = false;
        private boolean c = false;
        private ExplosionType e = ExplosionType.CREEPER_SHAPED;

        public List<DyeColor> getColors() {
            return this.d;
        }

        public List<DyeColor> getFades() {
            return this.b;
        }

        public boolean hasFlicker() {
            return this.a;
        }

        public boolean hasTrail() {
            return this.c;
        }

        public ExplosionType getType() {
            return this.e;
        }

        public FireworkExplosion setFlicker(boolean bl) {
            this.a = bl;
            return this;
        }

        public FireworkExplosion setTrail(boolean bl) {
            this.c = bl;
            return this;
        }

        public FireworkExplosion type(ExplosionType explosionType) {
            this.e = explosionType;
            return this;
        }

        public FireworkExplosion addColor(DyeColor dyeColor) {
            this.d.add(dyeColor);
            return this;
        }

        public FireworkExplosion addFade(DyeColor dyeColor) {
            this.b.add(dyeColor);
            return this;
        }

        public static enum ExplosionType {
            SMALL_BALL,
            LARGE_BALL,
            STAR_SHAPED,
            CREEPER_SHAPED,
            BURST;

        }
    }
}

