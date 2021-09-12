package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityOwnable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityTameable extends EntityWalkingAnimal implements EntityOwnable {

    public EntityTameable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public String getOwnerName() {
        return this.namedTag.getString(cn.nukkit.entity.EntityTameable.NAMED_TAG_OWNER);
    }

    @Override
    public void setOwnerName(String name) {
        this.namedTag.putString(cn.nukkit.entity.EntityTameable.NAMED_TAG_OWNER, name);
    }

    @Override
    public Player getOwner() {
        return this.server.getPlayerExact(this.getOwnerName());
    }
}
