/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockTNT
extends BlockSolid {
    @Override
    public String getName() {
        return "TNT";
    }

    @Override
    public int getId() {
        return 46;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getBurnChance() {
        return 15;
    }

    @Override
    public int getBurnAbility() {
        return 100;
    }

    public void prime() {
        this.prime(80);
    }

    public void prime(int n) {
        this.prime(n, null);
    }

    public void prime(int n, Entity entity) {
        this.getLevel().setBlock(this, Block.get(0), true);
        double d2 = (double)Utils.nukkitRandom.nextSignedFloat() * (Math.PI * 2);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", this.x + 0.5)).add(new DoubleTag("", this.y)).add(new DoubleTag("", this.z + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(d2) * 0.02)).add(new DoubleTag("", 0.2)).add(new DoubleTag("", -Math.cos(d2) * 0.02))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putShort("Fuse", n);
        EntityPrimedTNT entityPrimedTNT = new EntityPrimedTNT(this.getLevel().getChunk(this.getChunkX(), this.getChunkZ()), compoundTag, entity);
        entityPrimedTNT.spawnToAll();
        this.getLevel().addLevelEvent(this, 1005);
    }

    @Override
    public int onUpdate(int n) {
        if ((n == 1 || n == 6) && this.level.isBlockPowered(this.getLocation())) {
            this.prime();
        }
        return 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 259) {
            item.useOn(this);
            this.prime(80, player);
            return true;
        }
        if (item.getId() == 385) {
            if (!player.isCreative()) {
                --item.count;
            }
            this.level.addSound((Vector3)this, Sound.MOB_GHAST_FIREBALL);
            this.prime(80, player);
            return true;
        }
        if (item instanceof ItemTool && item.hasEnchantment(13)) {
            item.useOn(this);
            this.prime(80, player);
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.TNT_BLOCK_COLOR;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof EntityArrow && entity.isOnFire()) {
            entity.close();
            this.prime();
        }
    }
}

