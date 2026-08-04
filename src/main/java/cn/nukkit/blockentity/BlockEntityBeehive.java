package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBeehive;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityBee;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityBeehive extends BlockEntity {

    private List<Occupant> occupants;

    @Setter
    private Player interactingPlayer;

    public BlockEntityBeehive(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public static final class Occupant {

        private int ticksLeftToStay;
        private final CompoundTag saveData;
        private final boolean hasNectar;

        public Occupant(int ticksLeftToStay, CompoundTag saveData, boolean hasNectar) {
            this.ticksLeftToStay = ticksLeftToStay;
            this.saveData = saveData;
            this.hasNectar = hasNectar;
        }

        private Occupant(CompoundTag saved) {
            this.ticksLeftToStay = saved.getInt("TicksLeftToStay");
            this.saveData = saved.getCompound("SaveData").copy();
            this.hasNectar = saved.getBoolean("HasNectar");
        }

        public CompoundTag saveNBT() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag
                    .putInt("TicksLeftToStay", ticksLeftToStay)
                    .putCompound("SaveData", saveData)
                    .putBoolean("HasNectar", hasNectar);
            return compoundTag;
        }
    }

    public void setHoneyLevel(int honeyLevel) {
        Block block = getBlock();
        if (block instanceof BlockBeehive) {
            BlockBeehive hive = (BlockBeehive) block;
            hive.setHoneyLevel(honeyLevel);
            block.getLevel().setBlock(block, block, true, true);
        }
    }

    public int getHoneyLevel() {
        Block block = getBlock();
        if (block instanceof BlockBeehive) {
            BlockBeehive hive = (BlockBeehive) block;
            return hive.getHoneyLevel();
        } else {
            return 0;
        }
    }

    public Occupant[] getOccupants() {
        return occupants.toArray(new Occupant[0]);
    }

    public int getOccupantsCount() {
        return occupants.size();
    }

    @Override
    public boolean isBlockEntityValid() {
        int id = level.getBlockIdAt(chunk, (int) x, (int) y, (int) z);
        return id == BlockID.BEEHIVE || id == BlockID.BEE_NEST;
    }

    public boolean isEmpty() {
        return occupants.isEmpty();
    }

    public boolean isHoneyEmpty() {
        return getHoneyLevel() == 0;
    }

    public boolean isHoneyFull() {
        return getHoneyLevel() == 5;
    }

    public Occupant addOccupant(Entity entity) {
        boolean hasNectar = ((EntityBee) entity).hasNectar();
        int ticksLeftToStay = hasNectar ? 2400 : 600;
        entity.saveNBT();
        Occupant occupant = new Occupant(ticksLeftToStay, entity.namedTag.copy(), hasNectar);
        entity.close();
        occupants.add(occupant);
        scheduleUpdate();
        level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BLOCK_BEEHIVE_ENTER);
        return occupant;
    }

    public void angerBees(Player player) {
        if (!isEmpty()) {
            List<BlockFace> validFaces = scanValidSpawnFaces(false);
            if (isSpawnFaceValid(BlockFace.UP)) {
                validFaces.add(BlockFace.UP);
            }
            if (isSpawnFaceValid(BlockFace.DOWN)) {
                validFaces.add(BlockFace.DOWN);
            }
            for (BlockEntityBeehive.Occupant occupant : getOccupants()) {
                Entity entity = spawnOccupant(occupant, validFaces);
                if (entity instanceof EntityBee) {
                    EntityBee bee = (EntityBee) entity;
                    if (player != null) {
                        bee.setAngry(player);
                    } else {
                        bee.setAngry(2400);
                    }
                }
            }
        }
    }

    @Override
    protected void initBlockEntity() {
        super.initBlockEntity();

        this.occupants = new ArrayList<>(4);
        if (!this.namedTag.contains("Occupants")) {
            this.namedTag.putList(new ListTag<>("Occupants"));

            // We somehow lost the data
            for (int i = 0; i < Utils.rand(2, 3); i++) {
                this.occupants.add(new Occupant(new CompoundTag()));
            }
        } else {
            ListTag<CompoundTag> occupantsTag = namedTag.getList("Occupants", CompoundTag.class);
            for (int i = 0; i < occupantsTag.size(); i++) {
                this.occupants.add(new Occupant(occupantsTag.get(i)));
            }
        }

        if (!isEmpty()) {
            scheduleUpdate();
        }
    }

    private boolean isSpawnFaceValid(BlockFace face) {
        Block side = getSide(face).getLevelBlock();
        return side.canPassThrough() && !(side instanceof BlockLiquid);
    }

    @Override
    public void onBreak(/*boolean isSilkTouch*/) {
        if (interactingPlayer != null /*&& !isSilkTouch*/ && (interactingPlayer.isSurvival() || interactingPlayer.isAdventure())) {
            for (Occupant occupant : getOccupants()) {
                Entity bee;
                if ((bee = spawnOccupant(occupant, Collections.singletonList(BlockFace.UP))) instanceof EntityBee) {
                    ((EntityBee) bee).setAngry(interactingPlayer);
                }
            }

            for (Entity entity : level.getNearbyEntities(new SimpleAxisAlignedBB(this, this).grow(8, 8, 8))) {
                if (entity instanceof EntityBee) {
                    ((EntityBee) entity).setAngry(interactingPlayer);
                }
            }
        }
        super.onBreak();
    }

    @Override
    public boolean onUpdate() {
        if (this.closed || isEmpty()) {
            return false;
        }

        List<BlockFace> validSpawnFaces = null;
        boolean spawnedBee = false;

        // getOccupants will avoid ConcurrentModificationException if plugins changes the contents while iterating
        for (Occupant occupant : getOccupants()) {
            if (spawnedBee && occupant.ticksLeftToStay < 10) {
                occupant.ticksLeftToStay += 10; // Don't spawn all bees at once
            }

            if (--occupant.ticksLeftToStay <= 0 && !(getLevel().isRaining() || !getLevel().isAnimalSpawningAllowedByTime())) {
                if (validSpawnFaces == null) {
                    validSpawnFaces = scanValidSpawnFaces(true);
                }

                if (spawnOccupant(occupant, validSpawnFaces) == null) {
                    occupant.ticksLeftToStay = 600;
                } else {
                    spawnedBee = true;
                }
            } else if (ThreadLocalRandom.current().nextDouble() < 0.005) {
                level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BLOCK_BEEHIVE_WORK);
            }
        }

        return true;
    }

    public boolean removeOccupant(Occupant occupant) {
        return occupants.remove(occupant);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        ListTag<CompoundTag> occupantsTag = new ListTag<>();
        for (Occupant occupant : occupants) {
            occupantsTag.add(occupant.saveNBT());
        }
        occupantsTag.setName("Occupants");
        this.namedTag.putList(occupantsTag);
    }

    private List<BlockFace> scanValidSpawnFaces(boolean preferFront) {
        if (preferFront) {
            Block block = getBlock();
            if (block instanceof BlockBeehive) {
                BlockFace beehiveFace = ((BlockBeehive) block).getBlockFace();
                if (isSpawnFaceValid(beehiveFace)) {
                    return Collections.singletonList(beehiveFace);
                }
            }
        }

        List<BlockFace> validFaces = new ArrayList<>(4);
        for (BlockFace face : BlockFace.Plane.HORIZONTAL) {
            if (isSpawnFaceValid(face)) {
                validFaces.add(face);
            }
        }

        return validFaces;
    }

    private Entity spawnOccupant(Occupant occupant, List<BlockFace> validFaces) {
        if (validFaces != null && validFaces.isEmpty()) {
            return null;
        }

        CompoundTag saveData = occupant.saveData.copy();

        Position lookAt;
        Position spawnPosition;
        if (validFaces != null) {
            BlockFace face = validFaces.get(ThreadLocalRandom.current().nextInt(validFaces.size()));
            spawnPosition = add(
                    face.getXOffset() + 0.5,
                    face.getYOffset() + 0.5,
                    face.getZOffset() + 0.5
            );

            saveData.putList(new ListTag<DoubleTag>("Pos")
                    .add(new DoubleTag("", spawnPosition.x))
                    .add(new DoubleTag("", spawnPosition.y))
                    .add(new DoubleTag("", spawnPosition.z))
            );

            saveData.putList(new ListTag<DoubleTag>("Motion")
                    .add(new DoubleTag("", 0))
                    .add(new DoubleTag("", 0))
                    .add(new DoubleTag("", 0))
            );

            lookAt = getSide(face, 2);
        } else {
            spawnPosition = add(ThreadLocalRandom.current().nextDouble(), 0.2, ThreadLocalRandom.current().nextDouble());
            lookAt = spawnPosition.add(ThreadLocalRandom.current().nextDouble(), 0, ThreadLocalRandom.current().nextDouble());
        }

        double dx = lookAt.getX() - spawnPosition.getX();
        double dz = lookAt.getZ() - spawnPosition.getZ();
        float yaw = 0;

        if (dx != 0) {
            if (dx < 0) {
                yaw = (float) (1.5 * Math.PI);
            } else {
                yaw = (float) (0.5 * Math.PI);
            }
            yaw = yaw - (float) Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = (float) Math.PI;
        }

        yaw = -yaw * 180f / (float) Math.PI;

        saveData.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("", yaw))
                .add(new FloatTag("", 0))
        );

        saveData.putDouble("hiveX", (int) x);
        saveData.putDouble("hiveY", (int) y);
        saveData.putDouble("hiveZ", (int) z);

        Entity entity = Entity.createEntity(EntityBee.NETWORK_ID, spawnPosition.getChunk(), saveData);
        if (entity != null) {
            removeOccupant(occupant);
            level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BLOCK_BEEHIVE_EXIT);
        }

        EntityBee bee = entity instanceof EntityBee ? (EntityBee) entity : null;

        if (occupant.hasNectar && occupant.ticksLeftToStay <= 0) {
            if (!isHoneyFull()) {
                setHoneyLevel(getHoneyLevel() + 1);
            }
            if (bee != null) {
                //bee.nectarDelivered(this);
                bee.setNectar(false);
            }
        }/* else {
            if (bee != null) {
                bee.leftBeehive(this);
            }
        }*/

        if (entity != null) {
            entity.spawnToAll();
        }

        return entity;
    }
}
