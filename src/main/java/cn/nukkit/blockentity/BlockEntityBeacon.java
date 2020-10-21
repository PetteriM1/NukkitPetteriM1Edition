package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.potion.Effect;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Rover656
 */
public class BlockEntityBeacon extends BlockEntitySpawnable {

    public BlockEntityBeacon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (!namedTag.contains("Lock")) {
            namedTag.putString("Lock", "");
        }

        if (!namedTag.contains("Levels")) {
            namedTag.putInt("Levels", 0);
        }

        if (!namedTag.contains("Primary")) {
            namedTag.putInt("Primary", 0);
        }

        if (!namedTag.contains("Secondary")) {
            namedTag.putInt("Secondary", 0);
        }

        this.scheduleUpdate();

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        int blockID = getBlock().getId();
        return blockID == Block.BEACON;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag()
                .putString("id", BlockEntity.BEACON)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z)
                .putString("Lock", this.namedTag.getString("Lock"))
                .putInt("Levels", this.namedTag.getInt("Levels"))
                .putInt("Primary", this.namedTag.getInt("Primary"))
                .putInt("Secondary", this.namedTag.getInt("Secondary"));
    }

    private long currentTick = 0;

    @Override
    public boolean onUpdate() {
        //Only apply effects every 4 secs
        if (currentTick++ % 80 != 0) {
            return true;
        }

        int oldPowerLevel = this.getPowerLevel();
        //Get the power level based on the pyramid
        setPowerLevel(calculatePowerLevel());

        //Skip beacons that do not have a pyramid or sky access
        if (this.getPowerLevel() < 1 || !hasSkyAccess()) {
            if (oldPowerLevel > 0) {
                this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BEACON_DEACTIVATE);
            }
            return true;
        } else if (oldPowerLevel < 1) {
            this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BEACON_ACTIVATE);
        } else {
            this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BEACON_AMBIENT);
        }

        int duration = 9 + (getPowerLevel() << 1);

        for (Map.Entry<Long, Player> entry : this.level.getPlayers().entrySet()) {
            Player p = entry.getValue();

            //If the player is in range
            if (p.distance(this) < 10 + getPowerLevel() * 10) {
                Effect e;

                if (getPrimaryPower() != 0) {
                    //Apply the primary power
                    e = Effect.getEffect(getPrimaryPower());

                    //Set duration
                    e.setDuration(duration * 20);

                    //If secondary is selected as the primary too, apply 2 amplification
                    if (getSecondaryPower() == getPrimaryPower()) {
                        e.setAmplifier(1);
                    } else {
                        e.setAmplifier(0);
                    }

                    //Hide particles
                    e.setVisible(false);

                    //Add the effect
                    p.addEffect(e);
                }

                //If we have a secondary power as regen, apply it
                if (getSecondaryPower() == Effect.REGENERATION) {
                    //Get the regen effect
                    e = Effect.getEffect(Effect.REGENERATION);

                    //Set duration
                    e.setDuration(duration * 20);

                    //Regen I
                    e.setAmplifier(0);

                    //Hide particles
                    e.setVisible(false);

                    //Add effect
                    p.addEffect(e);
                }
            }
        }

        return true;
    }

    private static final int POWER_LEVEL_MAX = 4;

    private boolean hasSkyAccess() {
        //Check every block from our y coord to the top of the world
        for (int y = getFloorY() + 1; y <= 255; y++) {
            int testBlockId = level.getBlockIdAt(getFloorX(), y, getFloorZ());
            if (!Block.transparent[testBlockId]) {
                //There is no sky access
                return false;
            }
        }

        return true;
    }

    private int calculatePowerLevel() {
        //The power level that we're testing for
        for (int powerLevel = 1; powerLevel <= POWER_LEVEL_MAX; powerLevel++) {
            int queryY = getFloorY() - powerLevel; //Layer below the beacon block

            for (int queryX = getFloorX() - powerLevel; queryX <= getFloorX() + powerLevel; queryX++) {
                for (int queryZ = getFloorZ() - powerLevel; queryZ <= getFloorZ() + powerLevel; queryZ++) {

                    int testBlockId = level.getBlockIdAt(queryX, queryY, queryZ);
                    if (
                            testBlockId != Block.IRON_BLOCK &&
                                    testBlockId != Block.GOLD_BLOCK &&
                                    testBlockId != Block.EMERALD_BLOCK &&
                                    testBlockId != Block.DIAMOND_BLOCK
                            ) {
                        return powerLevel - 1;
                    }
                }
            }
        }

        return POWER_LEVEL_MAX;
    }

    public int getPowerLevel() {
        return namedTag.getInt("Level");
    }

    public void setPowerLevel(int level) {
        int currentLevel = getPowerLevel();
        if (level != currentLevel) {
            namedTag.putInt("Level", level);
            setDirty();
            this.spawnToAll();
        }
    }

    public int getPrimaryPower() {
        return namedTag.getInt("Primary");
    }

    public void setPrimaryPower(int power) {
        int currentPower = getPrimaryPower();
        if (power != currentPower) {
            namedTag.putInt("Primary", power);
            setDirty();
            this.spawnToAll();
        }
    }

    public int getSecondaryPower() {
        return namedTag.getInt("Secondary");
    }

    public void setSecondaryPower(int power) {
        int currentPower = getSecondaryPower();
        if (power != currentPower) {
            namedTag.putInt("Secondary", power);
            setDirty();
            this.spawnToAll();
        }
    }

    private static final List<Integer> allowedEffects = Arrays.asList(Effect.SPEED, Effect.HASTE, Effect.DAMAGE_RESISTANCE, Effect.JUMP, Effect.STRENGTH, Effect.REGENERATION);

    @Override
    public boolean updateCompoundTag(CompoundTag nbt, Player player) {
        if (!nbt.getString("id").equals(BlockEntity.BEACON)) {
            return false;
        }

        int primary = nbt.getInt("primary");
        if (allowedEffects.contains(primary)) {
            this.setPrimaryPower(primary);
        } else {
            Server.getInstance().getLogger().debug(player.getName() + " tried to set an invalid primary effect to a beacon: " + primary);
        }

        int secondary = nbt.getInt("secondary");
        if (allowedEffects.contains(secondary)) {
            this.setSecondaryPower(secondary);
        } else {
            Server.getInstance().getLogger().debug(player.getName() + " tried to set an invalid secondary effect to a beacon: " + secondary);
        }

        this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BEACON_POWER);

        player.getWindowById(Player.BEACON_WINDOW_ID).setItem(0, Item.get(Item.AIR));
        return true;
    }
}
