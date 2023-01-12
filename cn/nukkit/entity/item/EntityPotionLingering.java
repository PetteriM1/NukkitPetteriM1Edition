/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityAreaEffectCloud;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class EntityPotionLingering
extends EntityPotion {
    public static final int NETWORK_ID = 101;

    public EntityPotionLingering(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityPotionLingering(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setDataFlag(0, 47, true);
    }

    @Override
    protected void splash(Entity entity) {
        super.splash(entity);
        this.saveNBT();
        ListTag listTag = (ListTag)this.namedTag.getList("Pos", CompoundTag.class).copy();
        EntityAreaEffectCloud entityAreaEffectCloud = (EntityAreaEffectCloud)Entity.createEntity("AreaEffectCloud", this.getChunk(), new CompoundTag().putList(listTag).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putShort("PotionId", this.potionId), new Object[0]);
        Effect effect = Potion.getEffect(this.potionId, true);
        if (effect != null && entityAreaEffectCloud != null) {
            entityAreaEffectCloud.cloudEffects.add(effect.setDuration(1).setVisible(false).setAmbient(false));
            entityAreaEffectCloud.spawnToAll();
        }
    }
}

