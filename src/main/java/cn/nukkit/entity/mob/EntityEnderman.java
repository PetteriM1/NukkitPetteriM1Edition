package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EndermanBlockPickUpEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.HashMap;

public class EntityEnderman extends EntityWalkingMob {

    public static final int NETWORK_ID = 38;

    private int angry = 0;

    private boolean teleported;

    private boolean pickupBlocks;

    private Item block;

    private static final IntOpenHashSet canPickup = new IntOpenHashSet(new int[]{BlockID.DIRT, BlockID.GRASS, BlockID.PODZOL, BlockID.MYCELIUM, BlockID.COBBLESTONE, BlockID.SAND, BlockID.GRAVEL});

    public EntityEnderman(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (ev.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            setAngry(2400);
            ev.setCancelled(true);
            this.teleport();
            return false;
        }

        if (super.attack(ev) && !ev.isCancelled()) {
            if (ev.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                setAngry(2400);
            }

            if (!this.teleported && Utils.rand(1, 10) == 1) {
                this.teleport();
            }
        }
        return true;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared(player) < 1) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

            if (player instanceof Player) {
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += this.getArmorPoints(i.getId());
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    @Override
    public boolean canDespawn() {
        if (this.getLevel().getDimension() == Level.DIMENSION_THE_END) {
            return false;
        }

        return (this.block == null || !this.pickupBlocks) && super.canDespawn();
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (this.closed) {
            return false;
        }

        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return false;
        }

        this.teleported = false;

        if (this.angry > 0) {
            if (this.angry == 1) {
                this.setAngry(0); // Reset flag
            } else {
                this.angry--;
            }
        }

        int b = level.getBlockIdAt(chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ());
        if (Block.isWater(b) || (this.level.isRaining() && Utils.rand() && this.canSeeSky() && Biome.getBiome(level.getBiomeId((int) x, (int) z)).canRain())) {
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.DROWNING, 1));
            this.setAngry(0);
            this.teleport();
        } else if (Utils.rand(0, 500) == 20) {
            this.setAngry(0);
            this.teleport();
        } else if (this.pickupBlocks && block == null && this.age != 0 && this.onGround && this.age % 300 == 0 && Utils.rand(0, 20) == 5) {
            Block block = level.getBlock(chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ(), false);
            if (canPickup.contains(block.getId())) {
                this.pickupBlock(block);
            }
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public Item[] getDrops() {
        if (this.block != null) {
            return new Item[]{Item.get(Item.ENDER_PEARL, 0, Utils.rand(0, 1)), block};
        }
        return new Item[]{Item.get(Item.ENDER_PEARL, 0, Utils.rand(0, 1))};
    }

    @Override
    public float getHeight() {
        return 2.9f;
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    private Location getSafeTpLocation() {
        double dx = this.x + Utils.rand(-16, 16);
        double dz = this.z + Utils.rand(-16, 16);
        Vector3 pos = new Vector3(Math.floor(dx), (int) Math.floor(this.y + 0.1) + 16, Math.floor(dz));
        FullChunk chunk = this.level.getChunk((int) pos.x >> 4, (int) pos.z >> 4, false);
        int x = (int) pos.x & 0x0f;
        int z = (int) pos.z & 0x0f;
        int previousY1 = -1000;
        int previousY2 = -1000;
        if (chunk != null && chunk.isGenerated()) {
            for (int y = Math.min(this.level.getMaxBlockY(), (int) pos.y); y >= this.level.getMinBlockY(); y--) {
                if (previousY1 > -1000 && previousY2 > -1000) {
                    if (Block.isBlockSolidById(chunk.getBlockId(x, y, z)) && chunk.getBlockId(x, previousY1, z) == 0 && chunk.getBlockId(x, previousY2, z) == 0) {
                        return new Location(pos.x + 0.5, previousY1 + 0.1, pos.z + 0.5, this.level);
                    }
                }
                previousY2 = previousY1;
                previousY1 = y;
            }
        }
        return null;
    }

    @Override
    public double getSpeed() {
        return this.isAngry() ? 1.6 : 1.21;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return !this.pickupBlocks && super.ignoredAsSaveReason();
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();

        this.setDamage(new int[]{0, 4, 7, 10});

        if (this.namedTag.contains("Block")) {
            this.block = NBTIO.getItemHelper(this.namedTag.getCompound("Block"));
            this.setDataProperty(new IntEntityData(DATA_ENDERMAN_HELD_RUNTIME_ID, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, this.block.getId(), this.block.getDamage())));
        }

        this.pickupBlocks = !server.suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    public void setAngry(int val) {
        if (this.angry != val) {
            this.angry = val;
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_ANGRY, val > 0);
        }
    }

    private void pickupBlock(Block block) {
        if (!block.isValid()) {
            return;
        }

        EndermanBlockPickUpEvent event = new EndermanBlockPickUpEvent(this, block);
        this.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        level.setBlock(block, Block.get(Block.AIR));
        this.block = block.toItem();
        this.setDataProperty(new IntEntityData(DATA_ENDERMAN_HELD_RUNTIME_ID, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, block.getId(), block.getDamage())));

    }

    private void saveBlock() {
        if (block != null) {
            this.namedTag.put("Block", NBTIO.putItemHelper(block));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.saveBlock();
    }

    public void stareToAngry() {
        setAngry(2400);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (!isAngry()) return false;
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 1024; // 32 blocks
        }
        return creature.isAlive() && !creature.closed && distance <= 1024;
    }

    /**
     * Teleport enderman to random location in 32x32 range
     */
    public void teleport() {
        Location to = this.getSafeTpLocation();
        if (to != null) {
            this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ENDERMAN_TELEPORT);
            if (this.teleport(to, PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
                this.level.addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ENDERMAN_TELEPORT);
                this.teleported = true;
            }
        }
    }
}
