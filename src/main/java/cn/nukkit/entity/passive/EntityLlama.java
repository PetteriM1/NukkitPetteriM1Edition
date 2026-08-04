package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.projectile.EntityLlamaSpit;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityLlama extends EntityHorseBase {

    public static final int NETWORK_ID = 29;

    private int variant;

    private static final int[] VARIANTS = {0, 1, 2, 3};

    private int attackTicks;
    private Entity damagedBy;

    public EntityLlama(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.935f;
        }
        return 1.87f;
    }

    @Override
    public boolean canBeSaddled() {
        return false;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(15);
        super.initEntity();

        if (this.namedTag.contains("Variant")) {
            this.variant = this.namedTag.getInt("Variant");
        } else {
            this.variant = getRandomVariant();
        }

        this.setDataProperty(new IntEntityData(DATA_VARIANT, this.variant));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.variant);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (ev instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) ev).getDamager();
            if (damager instanceof Player && (((Player) damager).isSurvival() || ((Player) damager).isAdventure())) {
                if (this.attackTicks <= 0) {
                    this.attackTicks = 60;
                    this.damagedBy = damager;
                }
            }
        }

        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (!this.closed) {
            if (this.attackTicks > 0) {
                this.attackTicks--;
                this.moveTime = 0;
                this.stayTime = 60;
                if (this.damagedBy != null) {
                    if (this.attackTicks == 0) {
                        if (this.distanceSquared(this.damagedBy) < 100) {
                            EntityLlamaSpit shot = (EntityLlamaSpit) Entity.createEntity("LlamaSpit", this.add(0, this.getEyeHeight(), 0), this);

                            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                                shot.close();
                                return super.onUpdate(currentTick);
                            }

                            shot.setMotion(this.damagedBy.subtract(this).normalize().multiply(1.2));

                            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(shot);
                            this.server.getPluginManager().callEvent(launch);
                            if (launch.isCancelled()) {
                                shot.close();
                            } else {
                                shot.spawnToAll();
                                this.getLevel().addSound(this, Sound.MOB_LLAMA_SPIT);
                            }
                        }
                    }
                }
            }
        }
        return super.onUpdate(currentTick);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.LEATHER, 0, Utils.rand(0, 2))}; // TODO: carpet, chest & items
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean canTarget = super.targetOption(creature, distance);

        if (canTarget && (creature instanceof Player)) {
            Player player = (Player) creature;
            return player.isAlive() && !player.closed && this.isFeedItem(player.getInventory().getItemInHandFast()) && distance <= 49;
        }

        return false;
    }

    @Override
    public boolean isFeedItem(Item item) {
        return item.getId() == Item.WHEAT;
    }

    @Override
    public void onPlayerInput(Player player, double strafe, double forward) {
        // can't be controlled
    }

    private static int getRandomVariant() {
        return VARIANTS[Utils.rand(0, VARIANTS.length - 1)];
    }

    @Override
    public void onJump(Player player, int duration) {
        // can't be controlled
    }
}
