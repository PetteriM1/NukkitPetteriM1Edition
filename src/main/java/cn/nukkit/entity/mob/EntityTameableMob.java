package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityTameable;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityTameableMob extends EntityWalkingMob implements EntityTameable {

    private Player owner = null;

    private String ownerUUID = "";

    private boolean sitting = false;

    public EntityTameableMob(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag != null) {
            String ownerName = namedTag.getString(NAMED_TAG_OWNER);
            if (ownerName != null && !ownerName.isEmpty()) {
                Player player = this.getServer().getPlayerExact(ownerName);
                if (player != null) {
                    this.setOwner(player);
                }
                this.setSitting(namedTag.getBoolean(NAMED_TAG_SITTING));
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putBoolean(NAMED_TAG_SITTING, this.sitting);
        if (this.owner != null) {
            namedTag.putString(NAMED_TAG_OWNER, this.owner.getName());
            namedTag.putString(NAMED_TAG_OWNER_UUID, owner.getUniqueId().toString());
        }
    }

    @Override
    public Player getOwner() {
        this.checkOwner();
        return this.owner;
    }

    @Override
    public boolean hasOwner() {
        return hasOwner(true);
    }

    public boolean hasOwner(boolean checkOnline) {
        if (checkOnline) {
            this.checkOwner();
            return this.owner != null;
        } else {
            if (this.namedTag != null) {
                String ownerName = namedTag.getString(NAMED_TAG_OWNER);
                return ownerName != null && !ownerName.isEmpty();
            }
            return false;
        }
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;
        this.setDataProperty(new LongEntityData(DATA_OWNER_EID, player.getId()));
        this.setTamed(true);
    }

    @Override
    public boolean isSitting() {
        return this.sitting;
    }

    @Override
    public void setSitting(boolean sit) {
        this.sitting = sit;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SITTING, sit);
    }

    public void setTamed(boolean tamed) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, tamed);
    }

    @Override
    public String getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (this.sitting) {
            return this.target;
        }

        return super.updateMove(tickDiff);
    }

    /**
     * If the owner is online, set owner properly
     */
    public void checkOwner() {
        if (this.owner == null && this.namedTag != null) {
            String ownerName = namedTag.getString(NAMED_TAG_OWNER);
            if (ownerName != null && !ownerName.isEmpty()) {
                Player player = this.getServer().getPlayerExact(ownerName);
                if (player != null) {
                    this.setOwner(player);
                }
            }
        }
    }
}
