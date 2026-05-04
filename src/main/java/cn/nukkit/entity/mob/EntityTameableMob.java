package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityTameable;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.Optional;
import java.util.UUID;

public abstract class EntityTameableMob extends EntityWalkingMob implements EntityTameable {

    private boolean sitting;

    public EntityTameableMob(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void setOwner(Player player) {
        this.setOwnerUUID(player.getUniqueId().toString());
        this.setDataProperty(new LongEntityData(DATA_OWNER_EID, player.getId()));
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, true);
    }

    @Override
    public void setOwnerUUID(String ownerUUID) {
        this.namedTag.putString(NAMED_TAG_OWNER_UUID, ownerUUID);
    }

    @Override
    public void setSitting(boolean sit) {
        this.sitting = sit;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SITTING, sit);

        this.motionX = this.motionZ = 0;
    }

    @Override
    public String getName() {
        String name = this.getNameTag();
        return name.isEmpty() ? super.getName() : name;
    }

    @Override
    public Player getOwner() {
        String owner = this.getOwnerUUID();
        if (owner != null && !owner.isEmpty()) {
            Optional<Player> player = this.getServer().getPlayer(UUID.fromString(owner));
            if (player.isPresent()) {
                Player p = player.get();

                if (this.getDataPropertyLong(DATA_OWNER_EID) != p.getId()) { // Update
                    this.setDataProperty(new LongEntityData(DATA_OWNER_EID, p.getId()));
                    this.setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, true);
                }
                return p;
            }
        }

        return null;
    }

    @Override
    public String getOwnerUUID() {
        return this.namedTag.getString(NAMED_TAG_OWNER_UUID);
    }

    @Override
    public boolean isSitting() {
        return this.sitting;
    }

    @Override
    public boolean hasOwner() {
        return this.namedTag.contains(NAMED_TAG_OWNER_UUID);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag != null) {
            String owner = this.getOwnerUUID();
            if (owner != null && !owner.isEmpty()) {
                Optional<Player> player = this.getServer().getPlayer(UUID.fromString(owner));
                player.ifPresent(this::setOwner);

                this.setSitting(this.namedTag.getBoolean(NAMED_TAG_SITTING));

                this.pitch = 0;
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean(NAMED_TAG_SITTING, this.sitting);
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (this.isSitting()) {
            return null;
        }

        return super.updateMove(tickDiff);
    }
}
