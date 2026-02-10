package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.BlockSponge;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityPotionEffectEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityElderGuardian extends EntitySwimmingMob {

    public static final int NETWORK_ID = 50;

    public EntityElderGuardian(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.PRISMARINE_SHARD, 0, 1));
        }

        if (Utils.rand(1, 100) <= 20) {
            drops.add(Item.get(Item.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) this.lastDamageCause).getDamager() instanceof Player) {
                drops.add(Item.get(Item.SPONGE, BlockSponge.WET, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        return 1.9975f;
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Elder Guardian";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.9975f;
    }

    @Override
    public void attackEntity(Entity player) {
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (!this.closed && this.ticksLived % 1200 == 0 && this.isAlive()) {
            for (Player p : this.level.getPlayersList()) {
                if (p.getGamemode() % 2 == 0 && p.distanceSquared(this) < 2500 && !p.hasEffect(Effect.MINING_FATIGUE)) { // 50 blocks
                    p.addEffect(Effect.getEffect(Effect.MINING_FATIGUE).setAmplifier(2).setDuration(6000), EntityPotionEffectEvent.Cause.ELDER_GUARDIAN);

                    // Send only to players who get the effect
                    LevelEventPacket pk = new LevelEventPacket();
                    pk.evid = LevelEventPacket.EVENT_GUARDIAN_CURSE;
                    pk.x = (float) this.x;
                    pk.y = (float) this.y;
                    pk.z = (float) this.z;
                    p.dataPacket(pk);
                }
            }
        }
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(80);
        super.initEntity();

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_ELDER, true);
        this.setDamage(new int[]{0, 5, 8, 12});
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return false;
    }
}
