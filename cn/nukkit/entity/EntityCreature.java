/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityCreature
extends EntityLiving {
    public EntityCreature(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 421 && !player.isAdventure()) {
            return this.applyNameTag(player, item);
        }
        return false;
    }

    protected boolean applyNameTag(Player player, Item item) {
        return false;
    }
}

