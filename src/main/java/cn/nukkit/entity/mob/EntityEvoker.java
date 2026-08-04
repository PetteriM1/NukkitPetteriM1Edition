package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityEvoker extends EntityWalkingMob {

    public static final int NETWORK_ID = 104;

    private int vexCooldown = Utils.rand(0, 17 * 20);

    public EntityEvoker(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(24);
        super.initEntity();
        this.setDamage(new int[]{0, 0, 0, 0});
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.distanceSquared(player) <= 144) { // 12 blocks
            boolean vex = this.vexCooldown++ > 340; // 17 seconds

            if (this.attackDelay < 10 && Utils.rand()) {
                this.attackDelay--;
                return;
            }

            if (this.attackDelay == 100 + 20) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_EVOKER_SPELL, true);
                this.level.addSound(this, vex ? Sound.MOB_EVOCATION_ILLAGER_PREPARE_SUMMON : Sound.MOB_EVOCATION_ILLAGER_PREPARE_ATTACK);
            }

            if (this.attackDelay > 100 + 20 + (vex ? 59 : 40)) { // attackDelay max 200
                this.attackDelay = 0;
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_EVOKER_SPELL, false);

                if (vex) {
                    int count = 0;
                    for (Entity entity : level.getEntities()) {
                        if (entity instanceof EntityVex && entity.distanceSquared(this) < 256) { // fewer than 8 within 16 blocks
                            count++;
                            if (count >= 8) {
                                vex = false;
                                break;
                            }
                        }
                    }
                }

                if (vex) {
                    this.vexCooldown = 0;

                    for (int i = 0; i < 3; i++) {
                        Entity summon = Entity.createEntity("Vex", this.add(Utils.rand(-3, 3), this.getHeight() + Utils.rand(0, 3), Utils.rand(-3, 3)));
                        if (summon != null && !summon.isInsideOfSolid()) {
                            summon.spawnToAll();
                            this.level.addSound(this, Sound.MOB_EVOCATION_ILLAGER_CAST_SPELL);
                        }
                    }
                } else {
                    Entity shot = Entity.createEntity("EvocationFangs", player.getLocation(), this);
                    if (shot != null) {
                        shot.spawnToAll();
                        this.level.addSound(this, Sound.MOB_EVOCATION_ILLAGER_CAST_SPELL);
                    }
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.EMERALD, 0, Utils.rand(0, 1)), Item.get(Item.TOTEM, 0, 1)};
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 10 : 1;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }
}
