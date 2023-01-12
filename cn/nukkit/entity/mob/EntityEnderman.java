/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EndermanBlockPickUpEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.HashMap;

public class EntityEnderman
extends EntityWalkingMob {
    public static final int NETWORK_ID = 38;
    private int w = 0;
    private boolean C;
    private boolean D;
    private Item A;
    private static final IntArrayList B = new IntArrayList(new int[]{3, 2, 243, 110, 4, 12, 13});

    public EntityEnderman(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 38;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 2.9f;
    }

    @Override
    public double getSpeed() {
        return this.isAngry() ? 1.4 : 1.21;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();
        this.setDamage(new int[]{0, 4, 7, 10});
        if (this.namedTag.contains("Block")) {
            this.A = NBTIO.getItemHelper(this.namedTag.getCompound("Block"));
            this.setDataProperty(new IntEntityData(23, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, this.A.getId(), this.A.getDamage())));
        }
        this.D = !this.server.suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.0) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled()) {
            if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && !this.isAngry()) {
                this.setAngry(2400);
            }
            if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                if (!this.isAngry()) {
                    this.setAngry(2400);
                }
                entityDamageEvent.setCancelled(true);
                this.teleport();
                return false;
            }
            if (!this.C && Utils.rand(1, 10) == 1) {
                this.teleport();
            }
        }
        return true;
    }

    @Override
    public Item[] getDrops() {
        if (this.A != null) {
            return new Item[]{Item.get(368, 0, Utils.rand(0, 1)), this.A};
        }
        return new Item[]{Item.get(368, 0, Utils.rand(0, 1))};
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int n) {
        Vector3 vector3;
        Block block;
        int n2;
        if (this.closed) {
            return false;
        }
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return false;
        }
        this.C = false;
        if (this.w > 0) {
            if (this.w == 1) {
                this.setAngry(0);
            } else {
                --this.w;
            }
        }
        if (Block.hasWater(n2 = this.level.getBlockIdAt(this.chunk, NukkitMath.floorDouble(this.x), (int)this.y, NukkitMath.floorDouble(this.z))) || this.level.isRaining() && Utils.rand() && this.canSeeSky()) {
            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.DROWNING, 1.0f));
            this.setAngry(0);
            this.teleport();
        } else if (Utils.rand(0, 500) == 20) {
            this.setAngry(0);
            this.teleport();
        } else if (this.D && this.A == null && this.age != 0 && this.age % 300 == 0 && Utils.rand(0, 20) == 5 && B.contains((block = this.level.getBlock(vector3 = new Vector3(NukkitMath.floorDouble(this.x), (int)this.y - 1, NukkitMath.floorDouble(this.z)))).getId())) {
            this.a(block);
        }
        return super.entityBaseTick(n);
    }

    private void a(Block block) {
        if (!block.isValid()) {
            return;
        }
        EndermanBlockPickUpEvent endermanBlockPickUpEvent = new EndermanBlockPickUpEvent(this, block);
        this.getServer().getPluginManager().callEvent(endermanBlockPickUpEvent);
        if (endermanBlockPickUpEvent.isCancelled()) {
            return;
        }
        this.level.setBlock(block, Block.get(0));
        this.A = block.toItem();
        this.setDataProperty(new IntEntityData(23, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, block.getId(), block.getDamage())));
    }

    public void teleport() {
        Location location = this.b();
        if (location != null) {
            this.level.addLevelEvent(this, 1018);
            if (this.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
                this.level.addLevelEvent(this, 1018);
                this.C = true;
            }
        }
    }

    private Location b() {
        double d2 = this.x + (double)Utils.rand(-16, 16);
        double d3 = this.z + (double)Utils.rand(-16, 16);
        Vector3 vector3 = new Vector3(Math.floor(d2), (int)Math.floor(this.y + 0.1) + 16, Math.floor(d3));
        BaseFullChunk baseFullChunk = this.level.getChunk((int)vector3.x >> 4, (int)vector3.z >> 4, false);
        int n = (int)vector3.x & 0xF;
        int n2 = (int)vector3.z & 0xF;
        int n3 = -1;
        int n4 = -1;
        if (baseFullChunk != null && baseFullChunk.isGenerated()) {
            int n5 = Math.min(255, (int)vector3.y);
            while (n5 >= 0) {
                if (n3 > -1 && n4 > -1 && Block.solid[baseFullChunk.getBlockId(n, n5, n2)] && baseFullChunk.getBlockId(n, n3, n2) == 0 && baseFullChunk.getBlockId(n, n4, n2) == 0) {
                    return new Location(vector3.x + 0.5, (double)n3 + 0.1, vector3.z + 0.5, this.level);
                }
                n4 = n3;
                n3 = n5--;
            }
        }
        return null;
    }

    @Override
    public boolean canDespawn() {
        if (this.getLevel().getDimension() == 2) {
            return false;
        }
        return (this.A == null || !this.D) && super.canDespawn();
    }

    public boolean isAngry() {
        return this.w > 0;
    }

    public void setAngry(int n) {
        if (this.w != n) {
            this.w = n;
            this.setDataFlag(0, 25, n > 0);
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (!this.isAngry()) {
            return false;
        }
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && d2 <= 1024.0;
        }
        return entityCreature.isAlive() && !entityCreature.closed && d2 <= 1024.0;
    }

    public void stareToAngry() {
        if (!this.isAngry()) {
            this.setAngry(2400);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.c();
    }

    private void c() {
        if (this.A != null) {
            this.namedTag.put("Block", NBTIO.putItemHelper(this.A));
        }
    }
}

