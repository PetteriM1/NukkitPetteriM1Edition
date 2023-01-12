/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Map;

public class BlockEntityBeacon
extends BlockEntitySpawnable {
    private long c = 0L;
    private static final int d = 4;
    private static final IntSet b = new IntOpenHashSet(new int[]{0, 1, 3, 11, 8, 5, 10});

    public BlockEntityBeacon(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("Lock")) {
            this.namedTag.putString("Lock", "");
        }
        if (!this.namedTag.contains("Levels")) {
            this.namedTag.putInt("Levels", 0);
        }
        if (!this.namedTag.contains("Primary")) {
            this.namedTag.putInt("Primary", 0);
        }
        if (!this.namedTag.contains("Secondary")) {
            this.namedTag.putInt("Secondary", 0);
        }
        this.scheduleUpdate();
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 138;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "Beacon").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putString("Lock", this.namedTag.getString("Lock")).putInt("Levels", this.namedTag.getInt("Levels")).putInt("Primary", this.namedTag.getInt("Primary")).putInt("Secondary", this.namedTag.getInt("Secondary"));
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        if (this.c++ % 80L != 0L) {
            return true;
        }
        int n = this.getPowerLevel();
        this.setPowerLevel(this.b());
        if (this.getPowerLevel() < 1 || !this.a()) {
            if (n > 0) {
                this.getLevel().addLevelSoundEvent(this, 231);
            }
            return true;
        }
        if (n < 1) {
            this.getLevel().addLevelSoundEvent(this, 229);
        } else {
            this.getLevel().addLevelSoundEvent(this, 230);
        }
        int n2 = this.getPowerLevel();
        int n3 = (9 + (n2 << 1)) * 20;
        for (Map.Entry<Long, Player> entry : this.level.getPlayers().entrySet()) {
            Effect effect;
            Player player = entry.getValue();
            if (!(player.distance(this) < (double)(10 + n2 * 10))) continue;
            if (this.getPrimaryPower() != 0) {
                effect = Effect.getEffect(this.getPrimaryPower());
                effect.setDuration(n3);
                if (this.getSecondaryPower() == this.getPrimaryPower()) {
                    effect.setAmplifier(1);
                } else {
                    effect.setAmplifier(0);
                }
                effect.setVisible(false);
                player.addEffect(effect);
            }
            if (this.getSecondaryPower() == 10) {
                effect = Effect.getEffect(10);
                effect.setDuration(n3);
                effect.setAmplifier(0);
                effect.setVisible(false);
                player.addEffect(effect);
            }
            if (n2 < 4) continue;
            player.awardAchievement("fullBeacon");
        }
        return true;
    }

    private boolean a() {
        for (int k = this.getFloorY() + 1; k <= 255; ++k) {
            int n = this.level.getBlockIdAt(this.chunk, this.getFloorX(), k, this.getFloorZ());
            if (Block.transparent[n]) continue;
            return false;
        }
        return true;
    }

    private int b() {
        for (int k = 1; k <= 4; ++k) {
            int n = this.getFloorY() - k;
            for (int i2 = this.getFloorX() - k; i2 <= this.getFloorX() + k; ++i2) {
                for (int i3 = this.getFloorZ() - k; i3 <= this.getFloorZ() + k; ++i3) {
                    int n2 = this.level.getBlockIdAt(i2, n, i3);
                    if (n2 == 42 || n2 == 41 || n2 == 133 || n2 == 57) continue;
                    return k - 1;
                }
            }
        }
        return 4;
    }

    public int getPowerLevel() {
        return this.namedTag.getInt("Level");
    }

    public void setPowerLevel(int n) {
        int n2 = this.getPowerLevel();
        if (n != n2) {
            this.namedTag.putInt("Level", n);
            this.setDirty();
            this.spawnToAll();
        }
    }

    public int getPrimaryPower() {
        return this.namedTag.getInt("Primary");
    }

    public void setPrimaryPower(int n) {
        int n2 = this.getPrimaryPower();
        if (n != n2) {
            this.namedTag.putInt("Primary", n);
            this.setDirty();
            this.spawnToAll();
        }
    }

    public int getSecondaryPower() {
        return this.namedTag.getInt("Secondary");
    }

    public void setSecondaryPower(int n) {
        int n2 = this.getSecondaryPower();
        if (n != n2) {
            this.namedTag.putInt("Secondary", n);
            this.setDirty();
            this.spawnToAll();
        }
    }

    @Override
    public boolean updateCompoundTag(CompoundTag compoundTag, Player player) {
        if (!compoundTag.getString("id").equals("Beacon")) {
            return false;
        }
        int n = compoundTag.getInt("primary");
        if (b.contains(n)) {
            this.setPrimaryPower(n);
        } else {
            Server.getInstance().getLogger().debug(player.getName() + " tried to set an invalid primary effect to a beacon: " + n);
        }
        int n2 = compoundTag.getInt("secondary");
        if (b.contains(n2)) {
            this.setSecondaryPower(n2);
        } else {
            Server.getInstance().getLogger().debug(player.getName() + " tried to set an invalid secondary effect to a beacon: " + n2);
        }
        this.getLevel().addLevelSoundEvent(this, 232);
        player.getWindowById(4).setItem(0, Item.get(0));
        return true;
    }
}

